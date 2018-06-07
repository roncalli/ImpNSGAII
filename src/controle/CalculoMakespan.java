package controle;

import javax.print.attribute.standard.NumberOfInterveningJobs;

import modelo.Tarefa;

public class CalculoMakespan {
	
	public int [] calculoMakespan (int numIndividuos, int numMaquinas, int[][][]seq_pop, Tarefa[] tarefa, int[][]matrizTarefaMaquina, float[][][]matrizSetup){
		int [][] calculoMakespan = new int[numIndividuos][numMaquinas];
		int [] makespan = new int[numIndividuos];
		for (int i=0; i<numIndividuos; i++) {
			for (int j=0; j<numMaquinas; j++){
				int cont=0;
				int aux_makespan = 0;
				while (seq_pop[i][j][cont]!=-2) {	
					if (cont+1 == numIndividuos) {
						break;
					}
					if (seq_pop[i][j][cont+1]!=-2) {
						aux_makespan = aux_makespan + matrizTarefaMaquina[seq_pop[i][j][cont]][j]+(int)matrizSetup[j][seq_pop[i][j][cont]][seq_pop[i][j][cont+1]];
					}else {
						aux_makespan = aux_makespan + matrizTarefaMaquina[seq_pop[i][j][cont]][j];
					}					
					cont++;
				}
				calculoMakespan[i][j] = aux_makespan;
			}
		}
		
		for (int i=0;i<numIndividuos;i++) {
			int indice = -1;
			int aux_makespan = 0;
			for (int j=0;j<numMaquinas;j++) {
				if (calculoMakespan[i][j]>aux_makespan) {
					indice = j;
					aux_makespan = calculoMakespan[i][j]; 
				}
			}
			makespan[i] = aux_makespan;
		}		
		return makespan;
	}
	
	public int calculoMakespanSequencia (int[]seq_pop, Tarefa[] tarefa, int[][] matrizTarefaMaquina, int numMaquinas, float[][][] matrizSetup){
		int calculoMakespan = 0;
		int makespan = 0;
		int cont = 0;
		for (int j=0; j<numMaquinas; j++){
			while (seq_pop[cont]!=-2) {	
				if (cont+1 == seq_pop.length) {
					break;
				}
				if (seq_pop[cont+1]!=-2) {
					makespan = makespan +  matrizTarefaMaquina[seq_pop[cont]][j]+(int)matrizSetup[j][seq_pop[cont]][seq_pop[cont+1]];
				}else {
					makespan = makespan +  matrizTarefaMaquina[seq_pop[cont]][j];
				}					
				cont++;
			}
		}
		return makespan;
	}		
}
