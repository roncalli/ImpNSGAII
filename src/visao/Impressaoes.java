package visao;

import modelo.Tarefa;

public class Impressaoes {

	public long imprimir(int geracao,long[] makespan, int[][][]seq_pop, int numIndividuos, int[] nivelDominancia, int numMaquinas, int numTarefas) {
		long piorMakespan = 0;		
		long melhorMakespan = 10000000;
		int ind_mm= 0 ;
		int ind_mc = 0;
		System.out.println("Imprimindo os ítens da geração: "+geracao);
		System.out.println("----------------------------------------------------------------------");
//		System.out.println();
//		System.out.println("Indivíduo     -     Makespan     -    Custo");
		for (int i=0; i<numIndividuos; i++) {
			//if (nivelDominancia[i] == 1) {//Imprimindo apenas as soluções não dominadas
//				System.out.println(i+"          -          "+makespan[i]+"          -          ");
				if (makespan[i]<melhorMakespan) {
					melhorMakespan = makespan[i];
					ind_mm = i;
				}
				if (makespan[i]>piorMakespan) {
					piorMakespan = makespan[i];
				}
				
//				if (custo[i]<melhorCusto) {
//					melhorCusto= custo[i];
//					ind_mc = i;
//				}
//				if (custo[i]>piorCusto) {
//					piorCusto= custo[i];
//				}
			//}
		}
		System.out.println();
		System.out.println("Sequenciamento da solução com melhor makespan:");
		System.out.println();
		for (int i=0;i<numMaquinas; i++) {
			int j=0;
			System.out.println("Máquina: "+i);
			while (seq_pop[ind_mm][i][j]!=-2) {
				System.out.print(seq_pop[ind_mm][i][j]+"  -  ");
				if (seq_pop[ind_mm][i][j+1] == -2) {
					System.out.println();
				}
				j++;
				if (j == numTarefas-1){
					System.out.print(seq_pop[ind_mm][i][j]);
					break;
				}				
			}
		}	
		
	
		System.out.println();
		System.out.println();
		System.out.println("Melhor Penalidade Atraso+Adiantamento: "+melhorMakespan+"    -    "+"Pior Melhor Penalidade Atraso+Adiantamento: "+piorMakespan);
		System.out.println("----------------------------------------------------------------------");
		System.out.println();
		System.out.println();
		return melhorMakespan;
		
	}
	
	public void imprimir(int geracao,int[] makespan, float[]custo, int[][][]seq_pop, int numIndividuos) {
		System.out.println("Imprimindo os ítens da geração: "+geracao);
		System.out.println("----------------------------------------------------------------------");
		System.out.println();
		System.out.println("Indivíduo     -     Makespan     -    Custo");
		for (int i=0; i<numIndividuos; i++) {			
			System.out.println(i+"          -          "+makespan[i]+"          -          "+custo[i]);				
		}
		System.out.println("----------------------------------------------------------------------");
		System.out.println();
		System.out.println();
	}
}
