package visao;

import controle.RelacoesDominancia;

public class Impressaoes {

	public float imprimir(int geracao,float[] makespan, float[]custo, int[][][]seq_pop, int numIndividuos, int[] nivelDominancia, int numMaquinas, int numTarefas) {
		float piorMakespan = 0;		
		float melhorMakespan = 10000;
		int ind_mm= 0 ;
		float piorCusto = 0;
		float melhorCusto = 1000000;
		int ind_mc = 0;				
		System.out.println("Imprimindo os �tens da gera��o: "+geracao);
		System.out.println("----------------------------------------------------------------------");
		System.out.println();
		System.out.println("Indiv�duo     -     Makespan     -    Custo");	
		nivelDominancia = imprimirNaoDominados(makespan, custo, numIndividuos);
		for (int i=0; i<numIndividuos; i++) {
			if (nivelDominancia[i] != -2) {//Imprimindo apenas as solu��es n�o dominadas
				System.out.println(i+"          -          "+makespan[nivelDominancia[i]]+"          -          "+custo[nivelDominancia[i]]);
				if (makespan[nivelDominancia[i]]<melhorMakespan) {
					melhorMakespan = makespan[nivelDominancia[i]];
					ind_mm = nivelDominancia[i];
				}
				if (makespan[nivelDominancia[i]]>piorMakespan) {
					piorMakespan = makespan[nivelDominancia[i]];
				}
				
				if (custo[nivelDominancia[i]]<melhorCusto) {
					melhorCusto= custo[nivelDominancia[i]];
					ind_mc = nivelDominancia[i];
				}
				if (custo[nivelDominancia[i]]>piorCusto) {
					piorCusto= custo[nivelDominancia[i]];
				}
			}
		}
		System.out.println();
		System.out.println("Sequenciamento da solu��o com melhor makespan:");
		System.out.println();
		for (int i=0;i<numMaquinas; i++) {
			int j=0;
			System.out.println("M�quina: "+i);
			while ((seq_pop[ind_mm][i][j]!=-2)) {
				System.out.print(seq_pop[ind_mm][i][j]+"  -  ");
				if (j == numTarefas-1){
					break;
				}
				if (seq_pop[ind_mm][i][j+1] == -2) {
					System.out.println();
				}
				j++;
				if (j == numTarefas){
					break;
				}
			}
		}	
		
		System.out.println();
		System.out.println("Sequenciamento da solu��o com melhor Custo:");
		System.out.println();
//		for (int i=0;i<numMaquinas; i++) {
//			int j=0;
//			System.out.println("M�quina: "+i);
//			while (seq_pop[ind_mc][i][j]!=-2) {
//				System.out.print(seq_pop[ind_mc][i][j]+"  -  ");
//				if ((j == numTarefas-1)||(seq_pop[ind_mc][i][j+1] == -2)) {
//					System.out.println();
//				}
//				j++;
//				if (j == numTarefas){
//					break;
//				}
//			}
//		}	
		System.out.println();
		System.out.println();
		System.out.println("Melhor Makespan: "+melhorMakespan+"    -    "+"Pior Makespan: "+piorMakespan);
		System.out.println("Melhor Custo: "+melhorCusto+"    -    "+"Pior Custo: "+piorCusto);
		System.out.println("----------------------------------------------------------------------");
		System.out.println();
		System.out.println();
		return melhorMakespan;
	}
	
	public void imprimir(int geracao,int[] makespan, float[]custo, int[][][]seq_pop, int numIndividuos) {
		System.out.println("Imprimindo os �tens da gera��o: "+geracao);
		System.out.println("----------------------------------------------------------------------");
		System.out.println();
		System.out.println("Indiv�duo     -     Makespan     -    Custo");
		for (int i=0; i<numIndividuos; i++) {			
			System.out.println(i+"          -          "+makespan[i]+"          -          "+custo[i]);				
		}
		System.out.println("----------------------------------------------------------------------");
		System.out.println();
		System.out.println();
	}
	public int[] imprimirNaoDominados(float[] makespan, float[] custo, int numIndividuos) {
		int [] listaNaoDominados = new int[numIndividuos];
		for (int i =0; i<numIndividuos; i++) {
			listaNaoDominados[i] = -2;
		}
		int pos=0;
		for (int i=0; i<numIndividuos; i++) {
			boolean naoDominado = true;
			for (int j=0; j<numIndividuos; j++) {
				if (i!=j) {
					if ((makespan[j]<makespan[i])&&(custo[j]<custo[i])) {
						naoDominado = false;
						break;
					}
					if ((makespan[j]<=makespan[i])&&(custo[j]<custo[i])) {
						naoDominado = false;
						break;
					}
					if ((makespan[j]<makespan[i])&&(custo[j]<=custo[i])) {
						naoDominado = false;
						break;
					}
				}				
			}
			if (naoDominado) {
				listaNaoDominados[pos] = i;
				pos++;
			}
		}
		return listaNaoDominados;
	}
}
