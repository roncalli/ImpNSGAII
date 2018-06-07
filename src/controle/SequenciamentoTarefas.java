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
	
	public int[] calculoMelhorSequenciamentoMaquina(int numMaquinas, int numIndividuos,int numTarefas, int[] tar_maq, Tarefa[] tarefa, int[][]matrizTarefaMaquina, float[][][] matrizSetup) {		
		int i=0;
		int num_tar = 0;		
		//Calculando a quantidade de tarefas 
		while (tar_maq[i]!=-2) {
			num_tar++;	
			i++;
			if (i == numIndividuos) {
				break;
			}
		}
		int[] vet_tar = new int[num_tar];
		int[] seq_pop_ms = new int[numTarefas];	
		for (int k=0; k<numTarefas;k++) {
			seq_pop_ms[k] = -2;
		}
		i=0;
		//Adicionando as tarefas ao novo vetor
		while (tar_maq[i]!=-2) {
			vet_tar[i] = tar_maq[i];
			i++;
			if (i == numIndividuos) {
				break;
			}
		}
		for (int k=0; k<num_tar; k++) {
			seq_pop_ms[k] = vet_tar[k];
		}
		//Calculando o Makespan
		CalculoAdiantamentoAtraso calculoMakespan = new CalculoAdiantamentoAtraso();
		int makespan = calculoMakespan.calculoAdiantamentoAtrasoSequencia(seq_pop_ms, tarefa,matrizTarefaMaquina, numMaquinas);
		
		int[] novo_vet_tar = new int[num_tar];
		for (i=0; i<(numMaquinas/2); i++) {
			int[] seq = new int[num_tar];
			List<Integer> numeros = new ArrayList<Integer>();
			for (int j=0; j<num_tar; j++) {
				numeros.add(j);
			}			
			Collections.shuffle(numeros);
			for (int j=0; j<num_tar; j++) {
				seq[j] = (int) numeros.get(j);
			}
			for (int j=0;j<num_tar;j++) {
				novo_vet_tar[j] = vet_tar[seq[j]];
			}
			int[] seq_pop_aux = new int[numTarefas];
			for (int k=0; k<numTarefas;k++) {
				seq_pop_aux[k] = -2;
			}
			for (int k=0; k<num_tar; k++) {
				seq_pop_aux[k] = novo_vet_tar[k];
			}			
			int novo_makespan =  calculoMakespan.calculoAdiantamentoAtrasoSequencia(seq_pop_aux, tarefa, matrizTarefaMaquina,numMaquinas);
			if (novo_makespan<makespan){				
				seq_pop_ms = novo_vet_tar;
				makespan = novo_makespan;
			}
		}		
		return seq_pop_ms;
	}
}
