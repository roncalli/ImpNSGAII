package controle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Tarefa;

public class SequenciamentoTarefas {
	
	public int [][][] sequenciamento_Inicial(int numIndividuos, int numMaquinas, int numTarefas, int [][]pop, Tarefa [] tarefa){
		// Sequenciamento da População
		// Formato:(sequencia da tarefa, maquina, individuo)
		int [][][] seq_pop = new int[numIndividuos][numMaquinas][numTarefas];
		for (int i=0;i<numIndividuos;i++) {
			for (int j=0; j<numMaquinas;j++) {
				for (int k=0;k<numTarefas;k++) {
					seq_pop[i][j][k]=-2;
				}				
			}
		}
		for (int j=0;j<numIndividuos;j++) {
			//Gerando uma sequencia aleatória
			List<Integer> tarefas = new ArrayList<Integer>();
			for (int k=0; k<numTarefas; k++){
				tarefas.add(k);
			}
			Collections.shuffle(tarefas);
			for (int k=0; k<numMaquinas;k++) {
				int vet_tarefa[] = new int[numTarefas]; //Tarefas executadas pela máquina k
				//int vet_peso[] = new int [numTarefas]; //Contém os pesos das tarefas executadas pela máquina k
				int cont = 0;				
				for (int i=0; i<numTarefas;i++) {
					if (pop[i][j] == k) {
						vet_tarefa[cont] =  tarefas.get(i);
						//vet_peso[cont] = tarefa[i].getPrioridade();
						cont++;
					}
					seq_pop[j][k][i] = vet_tarefa[i];
				}
			}
		
		}
		return seq_pop;
	}
	

	public int[][][] gerarSequenciaInicial(int [][] matrizTarefaMaquina, Tarefa[] tarefa, int numIndividuos, int tempoLimite){
		int [][][] seq_inicial = new int[numIndividuos][1][tarefa.length];
		int numTarefas = tarefa.length;
		int [][] ranking = new int[numTarefas][2];
		int [] rank_Atraso = new int[numTarefas];
		int [] rank_Adiantamento = new int[numTarefas];		
		int aux = 0;
		
		for (int i=0; i<numTarefas; i++) {
			aux = tarefa[i].getPenalidadeAdiantamento() * matrizTarefaMaquina[i][0];
			//Adiantamento
			ranking[i][0] = aux;
			aux = tarefa[i].getPenalidadeAtraso() * matrizTarefaMaquina[i][0];
			ranking[i][1] = aux;
		}
		int [] visitadosAdiantamento = new int[numTarefas];		
		int [] visitadosAtraso = new int[numTarefas];
		int menorValorAdiantamento = 100000;
		int menorValorAtraso = 100000;
		for (int i = 0; i<numTarefas; i++) {
			for (int j=0; j<numTarefas; j++) {
				if ((ranking[j][0]<menorValorAdiantamento)&&(visitadosAdiantamento[j] == 0)) {
					menorValorAdiantamento = ranking[j][0];
					rank_Adiantamento[i] = j;
				}
				if ((ranking[j][1]<menorValorAtraso)&&(visitadosAtraso[j] == 0)) {
					menorValorAtraso = ranking[j][1];
					rank_Atraso[i] = j;
				}
			}
			menorValorAdiantamento = 100000;
			menorValorAtraso = 100000;
			visitadosAdiantamento[rank_Adiantamento[i]] = 1;
			visitadosAtraso[rank_Atraso[i]] = 1;			
		}
		
		int [] selecionados = new int[numTarefas];
		int auxTempo = 0;
		int cont_atraso = numIndividuos-1;
		int cont_selecionados = 0;		
		for (int i=0; i<numIndividuos; i++) {
			for (int j=0; j<numTarefas; j++) {
				if ((auxTempo+matrizTarefaMaquina[rank_Adiantamento[j]][0]) < tempoLimite ) {
					if (selecionados[rank_Adiantamento[j]] == 0) {
						seq_inicial[i][0][cont_selecionados] = rank_Adiantamento[j];
						selecionados[rank_Adiantamento[j]] = 1;
						cont_selecionados++;
						auxTempo = auxTempo+matrizTarefaMaquina[rank_Adiantamento[j]][0];
					}					
				}else {
					if (selecionados[rank_Atraso[cont_atraso]] == 0) {
						seq_inicial[i][0][cont_selecionados] = rank_Atraso[cont_atraso];
						selecionados[rank_Atraso[cont_atraso]] = 1;
						auxTempo = auxTempo+matrizTarefaMaquina[rank_Atraso[cont_atraso]][0];
						cont_selecionados++;						
					}
					cont_atraso--;
				}				
			}
			for (int j=numTarefas-1; j>1; j--) {
				if (selecionados[rank_Atraso[j]] == 0) {
					seq_inicial[i][0][cont_selecionados] = rank_Atraso[j];
					selecionados[rank_Atraso[j]] = 1;
					cont_selecionados++;
					auxTempo = auxTempo+matrizTarefaMaquina[rank_Atraso[j]][0];
				}								
			}
			cont_selecionados=0;
			auxTempo = 0;
			cont_atraso = numIndividuos-1;
			for (int w=0; w<numTarefas; w++) {
				selecionados[w] = 0;
			}
		}
		Operadores auxOper = new Operadores();
		auxOper.validarSequencia(seq_inicial, numIndividuos, numTarefas);
		CalculoAdiantamentoAtraso calculoAdiantamentoAtraso = new CalculoAdiantamentoAtraso();
		long [] res = calculoAdiantamentoAtraso.calculoAdiantamentoAtraso(numIndividuos, 1, seq_inicial, tarefa, matrizTarefaMaquina, tempoLimite);
		return seq_inicial;
	}
}



