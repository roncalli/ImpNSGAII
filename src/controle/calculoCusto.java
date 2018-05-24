package controle;

import modelo.Maquina;

public class calculoCusto {
	
	public float[] calculoCusto (int numIndividuos, int numMaquinas, int [][][] seq_pop, int[][] matrizTarefaMaquina, Maquina[] maquina) {
		float matrizCusto[][] = new float [numIndividuos][numMaquinas];
		float [] custo = new float[numIndividuos];
		for (int i=0; i<numIndividuos; i++) {
			for (int j=0; j<numMaquinas; j++){
				int cont=0;
				float aux_custo = 0;
				while (seq_pop[i][j][cont]!=-2) {
					aux_custo = aux_custo + matrizTarefaMaquina[seq_pop[i][j][cont]][j]*maquina[j].getCusto();
					cont++;
					if (cont == numIndividuos) {
						break;
					}
				}
				matrizCusto[i][j] = aux_custo;	//Custo por máquina			
			}
		}
		
		for (int i=0;i<numIndividuos;i++) {			
			float aux_custo = 0;
			for (int j=0;j<numMaquinas;j++) {									
				aux_custo = aux_custo + matrizCusto[i][j]; 				
			}
			custo[i] = aux_custo;			
		}		
		return custo;
	}

}
