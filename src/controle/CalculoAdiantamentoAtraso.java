package controle;

import javax.print.attribute.standard.NumberOfInterveningJobs;

import modelo.Tarefa;

public class CalculoAdiantamentoAtraso {
	
	public long [] calculoAdiantamentoAtraso (int numIndividuos, int numMaquinas, int[][][]seq_pop, Tarefa[] tarefa, int[][]matrizTarefaMaquina, int entrega){
		long [][] calculoAdiantamentoAtraso = new long[numIndividuos][numMaquinas];
		long [] adiantamentoAtraso = new long[numIndividuos];
		for (int i=0; i<numIndividuos; i++) {
			int tempo = 0;			
			for (int j=0; j<numMaquinas; j++){
				int cont=0;
				long aux_adiantamentoAtraso = 0;
				while (seq_pop[i][j][cont]!=-2) {	
					if (cont+1 == tarefa.length) {
						break;
					}
					long adiantamento = tarefa[seq_pop[i][j][cont]].getPenalidadeAdiantamento()*(entrega-tempo);
					if (adiantamento<=0) {
						adiantamento = 0;
					}
					long atraso = tarefa[seq_pop[i][j][cont]].getPenalidadeAtraso()*(tempo-entrega);;
					if (atraso<=0) {
						atraso = 0;
					}
					aux_adiantamentoAtraso = aux_adiantamentoAtraso + adiantamento + atraso;					
					tempo = tempo+matrizTarefaMaquina[seq_pop[i][j][cont]][j];															
					cont++;
				}
				calculoAdiantamentoAtraso[i][j] = aux_adiantamentoAtraso;
			}
		}
		
		for (int i=0;i<numIndividuos;i++) {
			int indice = -1;
			long aux_makespan = 0;
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
	
	public long calculoAdiantamentoAtrasoSequencia (int[]seq_pop, Tarefa[] tarefa, int[][] matrizTarefaMaquina, int numMaquinas,int entrega){
		int cont = 0;
		int tempo = 0;
		long aux_adiantamentoAtraso = 0;
		for (int j=0; j<numMaquinas; j++){
			while (seq_pop[cont]!=-2) {	
				if (cont+1 == tarefa.length) {
					break;
				}
				long adiantamento = tarefa[seq_pop[cont]].getPenalidadeAdiantamento()*(entrega-tempo);
				if (adiantamento<=0) {
					adiantamento = 0;
				}
				long atraso = tarefa[seq_pop[cont]].getPenalidadeAtraso()*(tempo-entrega);;
				if (atraso<=0) {
					atraso = 0;
				}
				aux_adiantamentoAtraso = aux_adiantamentoAtraso + adiantamento + atraso;					
				tempo = tempo+matrizTarefaMaquina[seq_pop[cont]][j];															
				cont++;
			}
		}
		return aux_adiantamentoAtraso;
	}		
}
