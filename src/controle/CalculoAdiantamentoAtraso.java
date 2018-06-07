package controle;

import javax.print.attribute.standard.NumberOfInterveningJobs;

import modelo.Tarefa;

public class CalculoAdiantamentoAtraso {
	
	public int [] calculoAdiantamentoAtraso (int numIndividuos, int numMaquinas, int[][][]seq_pop, Tarefa[] tarefa, int[][]matrizTarefaMaquina, float[][][]matrizSetup){
		int [][] calculoAdiantamentoAtraso = new int[numIndividuos][numMaquinas];
		int [] adiantamentoAtraso = new int[numIndividuos];
		for (int i=0; i<numIndividuos; i++) {
			for (int j=0; j<numMaquinas; j++){
				int cont=0;
				int aux_adiantamentoAtraso = 0;
				while (seq_pop[i][j][cont]!=-2) {	
					if (cont+1 == numIndividuos) {
						break;
					}
					if (seq_pop[i][j][cont+1]!=-2) {
						aux_adiantamentoAtraso = aux_adiantamentoAtraso + matrizTarefaMaquina[seq_pop[i][j][cont]][j]+(int)matrizSetup[j][seq_pop[i][j][cont]][seq_pop[i][j][cont+1]];
					}else {
						aux_adiantamentoAtraso = aux_adiantamentoAtraso + matrizTarefaMaquina[seq_pop[i][j][cont]][j];
					}					
					cont++;
				}
				calculoAdiantamentoAtraso[i][j] = aux_adiantamentoAtraso;
			}
		}
		
		for (int i=0;i<numIndividuos;i++) {
			int indice = -1;
			int aux_makespan = 0;
			for (int j=0;j<numMaquinas;j++) {
				if (calculoAdiantamentoAtraso[i][j]>aux_makespan) {
					indice = j;
					aux_makespan = calculoAdiantamentoAtraso[i][j]; 
				}
			}
			adiantamentoAtraso[i] = aux_makespan;
		}		
		return adiantamentoAtraso;
	}
	
	public int calculoAdiantamentoAtrasoSequencia (int[]seq_pop, Tarefa[] tarefa, int[][] matrizTarefaMaquina, int numMaquinas, float[][][] matrizSetup){
		int calculoAdiantamentoAtraso = 0;
		int adiantamentoAtraso = 0;
		int cont = 0;
		for (int j=0; j<numMaquinas; j++){
			while (seq_pop[cont]!=-2) {	
				if (cont+1 == seq_pop.length) {
					break;
				}
				if (seq_pop[cont+1]!=-2) {
					adiantamentoAtraso = adiantamentoAtraso +  matrizTarefaMaquina[seq_pop[cont]][j]+(int)matrizSetup[j][seq_pop[cont]][seq_pop[cont+1]];
				}else {
					adiantamentoAtraso = adiantamentoAtraso +  matrizTarefaMaquina[seq_pop[cont]][j];
				}					
				cont++;
			}
		}
		return adiantamentoAtraso;
	}		
}
