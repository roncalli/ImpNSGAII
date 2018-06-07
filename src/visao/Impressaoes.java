package visao;

public class Impressaoes {

	public void imprimir(int geracao,int[] makespan, int[][][]seq_pop, int numIndividuos, int[] nivelDominancia, int numMaquinas) {
		int piorMakespan = 0;		
		int melhorMakespan = 10000;
		int ind_mm= 0 ;
		float piorCusto = 0;
		float melhorCusto = 10000;
		int ind_mc = 0;
		System.out.println("Imprimindo os ítens da geração: "+geracao);
		System.out.println("----------------------------------------------------------------------");
		System.out.println();
		System.out.println("Indivíduo     -     Makespan     -    Custo");
		for (int i=0; i<numIndividuos; i++) {
			if (nivelDominancia[i] == 1) {//Imprimindo apenas as soluções não dominadas
				System.out.println(i+"          -          "+makespan[i]+"          -          ");
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
			}
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
			}
		}	
		
		System.out.println();
		System.out.println("Sequenciamento da solução com melhor Custo:");
		System.out.println();
		for (int i=0;i<numMaquinas; i++) {
			int j=0;
			System.out.println("Máquina: "+i);
			while (seq_pop[ind_mc][i][j]!=-2) {
				System.out.print(seq_pop[ind_mc][i][j]+"  -  ");
				if (seq_pop[ind_mc][i][j+1] == -2) {
					System.out.println();
				}
				j++;
			}
		}	
		System.out.println();
		System.out.println();
		System.out.println("Melhor Makespan: "+melhorMakespan+"    -    "+"Pior Makespan: "+piorMakespan);
		System.out.println("Melhor Custo: "+melhorCusto+"    -    "+"Pior Custo: "+piorCusto);
		System.out.println("----------------------------------------------------------------------");
		System.out.println();
		System.out.println();
		
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
