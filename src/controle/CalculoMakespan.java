package controle;

import javax.print.attribute.standard.NumberOfInterveningJobs;

import modelo.Tarefa;

public class CalculoMakespan {
	
	public float [] calculoMakespan (int numIndividuos, int numMaquinas, int[][][]seq_pop, Tarefa[] tarefa, float[][]matrizTarefaMaquina, float[][][]matrizSetup){
		float [][] calculoMakespan = new float[numIndividuos][numMaquinas];
		float [] makespan = new float[numIndividuos];
		for (int i=0; i<numIndividuos; i++) {
			for (int j=0; j<numMaquinas; j++){
				int cont=0;
				float aux_makespan = 0;
				while (seq_pop[i][j][cont]!=-2) {	
					if (cont+1 == tarefa.length) {
						break;
					}
					if (seq_pop[i][j][cont+1]!=-2) {
						aux_makespan = (int)aux_makespan + (int)matrizTarefaMaquina[seq_pop[i][j][cont]][j]+(int)matrizSetup[j][seq_pop[i][j][cont]][seq_pop[i][j][cont+1]];
					}else {
						aux_makespan = (int)aux_makespan + (int)matrizTarefaMaquina[seq_pop[i][j][cont]][j];
					}					
					cont++;
				}
				calculoMakespan[i][j] = aux_makespan;
			}
		}
		
		for (int i=0;i<numIndividuos;i++) {
			int indice = -1;
			float aux_makespan = 0;
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
	
//	public float calculoMakespanSequencia (int[]seq_pop, Tarefa[] tarefa, float[][] matrizTarefaMaquina, int numMaquinas, float[][][] matrizSetup){
//		float calculoMakespan = 0;
//		float makespan = 0;
//		int cont = 0;
//		for (int j=0; j<numMaquinas; j++){
//			while (seq_pop[cont]!=-2) {	
//				if (cont+1 == seq_pop.length) {
//					break;
//				}
//				if (seq_pop[cont+1]!=-2) {
//					makespan = makespan +  matrizTarefaMaquina[seq_pop[cont]][j]+matrizSetup[j][seq_pop[cont]][seq_pop[cont+1]];
//				}else {
//					makespan = makespan +  matrizTarefaMaquina[seq_pop[cont]][j];
//				}					
//				cont++;
//			}
//		}
//		return makespan;
//	}		
	
	public float calculoMakespanSequencia (int[][]seq_pop, Tarefa[] tarefa, float[][] matrizTarefaMaquina, int numMaquinas, float[][][] matrizSetup){
		float valorMakespan = 100000;
		float makespan = 0;
		for (int i=0; i<numMaquinas; i++){
			makespan = 0;
			for (int j=0; j<tarefa.length; j++){
				if (seq_pop[i][j+1]!=-2){
					 makespan = makespan + (int)matrizTarefaMaquina[seq_pop[i][j]][i] + (int)matrizSetup[i][seq_pop[i][j]][seq_pop[i][j+1]];
				}else if (seq_pop[i][j]!=-2){
					makespan = makespan + (int)matrizTarefaMaquina[seq_pop[i][j]][i];
				}else{
					break;
				}
			}
			if (makespan<valorMakespan){
				valorMakespan = makespan;
			}
		}
		return makespan;
	}
}
