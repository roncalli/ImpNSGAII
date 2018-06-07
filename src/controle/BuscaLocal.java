package controle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Maquina;
import modelo.Tarefa;

public class BuscaLocal {
	public void buscaLocalN1N2(int [][] pop, int [][][] seq_pop, int numMaquinas, Maquina[] maquina, Tarefa[] tarefa, int[][] matrizTarefaMaquina, float[][][] matrizSetup, int individuo){
		//Verificar qual máquina possui o maior makespan
		int contNaoMelhora = 0;
		int maquinaMaiorMakespan = -1;
		int maiorMakespan = 0;
		CalculoAdiantamentoAtraso calculoMakespan = new CalculoAdiantamentoAtraso();
		CalculoCusto calculoCusto = new CalculoCusto();
		//Encontrar a máquina com maior makespan
		for (int i=0; i<numMaquinas; i++) {			
			int makespan = calculoMakespan.calculoAdiantamentoAtrasoSequencia(seq_pop[individuo][i], tarefa, matrizTarefaMaquina, i, matrizSetup);
			if (makespan>maiorMakespan) {
				makespan = maiorMakespan;
				maquinaMaiorMakespan = i;
			}
		}
		boolean melhorou = true;
		while (melhorou) {
			//Gerar uma sequencia aleatória com a ordenação das máquinas
			List<Integer> maquinas = new ArrayList<Integer>();			
			for (int i=0; i<numMaquinas; i++) {
				maquinas.add(i);
			}
			Collections.shuffle(maquinas);
			int cont = 0;
			if (maquinas.get(cont)!=maquinaMaiorMakespan) {
				int contTarMaqMaior = 0;
				if (maquinaMaiorMakespan !=-1) {
					while (seq_pop[individuo][maquinaMaiorMakespan][contTarMaqMaior]!=-2) {
						int contTarMaqSelcionada = 0;
						while (seq_pop[individuo][maquinas.get(cont)][contTarMaqSelcionada]!=-2) {
							//Troca de tarefas
							int makespan_antes = calculoMakespan.calculoAdiantamentoAtrasoSequencia(seq_pop[individuo][maquinas.get(cont)], tarefa, matrizTarefaMaquina, numMaquinas, matrizSetup);
							float custo_antes = calculoCusto.calculoCustoSequencia(seq_pop[individuo][maquinas.get(cont)], tarefa, matrizTarefaMaquina, numMaquinas, maquina);
							int aux = seq_pop[individuo][maquinaMaiorMakespan][contTarMaqMaior];
							seq_pop[individuo][maquinaMaiorMakespan][contTarMaqMaior] = seq_pop[individuo][maquinas.get(cont)][contTarMaqSelcionada];
							seq_pop[individuo][maquinas.get(cont)][contTarMaqSelcionada] = aux;
							//Inserir Cálculo das funções objetivo
							int makespan_apos = calculoMakespan.calculoAdiantamentoAtrasoSequencia(seq_pop[individuo][maquinas.get(cont)], tarefa, matrizTarefaMaquina, numMaquinas, matrizSetup);
							float custo_apos = calculoCusto.calculoCustoSequencia(seq_pop[individuo][maquinas.get(cont)], tarefa, matrizTarefaMaquina, numMaquinas, maquina);
							if (makespan_apos<makespan_antes) {
								melhorou = true;
								int aux_pop = maquinaMaiorMakespan;
								pop[seq_pop[individuo][maquinaMaiorMakespan][contTarMaqMaior]][individuo] = maquinas.get(cont);
								pop[seq_pop[individuo][maquinas.get(cont)][contTarMaqSelcionada]][individuo] = maquinaMaiorMakespan;
								contNaoMelhora = 0;
							}else if ((makespan_antes == makespan_apos)&&(custo_apos<custo_antes)) {
								melhorou = true;	
								contNaoMelhora = 0;
							}else {
								//Desfaz a troca de tarefas nas máquinas
								aux = seq_pop[individuo][maquinaMaiorMakespan][contTarMaqMaior];
								seq_pop[individuo][maquinaMaiorMakespan][contTarMaqMaior] = seq_pop[individuo][maquinas.get(cont)][contTarMaqSelcionada];
								seq_pop[individuo][maquinas.get(cont)][contTarMaqSelcionada] = aux;
								contNaoMelhora++;
								if (contNaoMelhora == 10) {
									melhorou=false;
									break;
								}
							}	
							if (contNaoMelhora == 10) {
								break;
							}
							contTarMaqSelcionada++;
							//Caso o makespan seja igual se calcula o custo						
						}
						if (contNaoMelhora == 10) {
							break;
						}
						contTarMaqMaior++;
					}
				}
			}
			cont++;
		}		
	}
}
