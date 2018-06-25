package controle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Maquina;
import modelo.Tarefa;

public class BuscaLocal {
	public int [][][] buscaLocal(int [][] pop, int [][][] seq_pop, int numMaquinas, Maquina[] maquina, Tarefa[] tarefa, float[][] matrizTarefaMaquina, float[][][] matrizSetup, int individuo, int numIndividuos){
		//Verificar qual máquina possui o maior makespan
		int contNaoMelhora = 0;
		int maquinaMaiorMakespan = -1;
		int maiorMakespan = 0;
		int [] pop_aux = new int[tarefa.length];
		int [][] seq_pop_aux = new int[numMaquinas][tarefa.length];
		
		//Copiando a Populacao
		for (int i=0; i<tarefa.length; i++){
			pop_aux[i] = pop[i][individuo];
		}
		
		//Copiando a Sequencia
		for (int i=0; i<numMaquinas; i++){
			for (int j=0; j<tarefa.length; j++){
				seq_pop_aux[i][j] = seq_pop[individuo][i][j];
			}
		}
		
		CalculoMakespan calculoMakespan = new CalculoMakespan();
		CalculoCusto calculoCusto = new CalculoCusto();
		//Encontrar a máquina com maior makespan
		for (int i=0; i<numMaquinas; i++) {			
			float makespan = calculoMakespan.calculoMakespanSequencia(seq_pop[individuo], tarefa, matrizTarefaMaquina, i, matrizSetup);
			if (makespan>maiorMakespan) {
				makespan = maiorMakespan;
				maquinaMaiorMakespan = i;
			}
		}
		boolean melhorou = false;
		int contSemMelhora = 0;
		if (maquinaMaiorMakespan!=-1){
			while (!melhorou) {
				//Gerar uma sequencia aleatória com a ordenação das máquinas
				List<Integer> maquinas = new ArrayList<Integer>();			
				for (int i=0; i<numMaquinas; i++) {
					maquinas.add(i);
				}
				Collections.shuffle(maquinas);
				for (int cont=0; cont<numMaquinas; cont++){
					if (maquinas.get(cont)!=maquinaMaiorMakespan) {
						int tarMaqMaior = (int)Math.floor(Math.random()*70);
						while (seq_pop[individuo][maquinaMaiorMakespan][tarMaqMaior]==-2){
							tarMaqMaior = (int)Math.floor(Math.random()*70);
						}
						int tarMaqSelecionada = (int)Math.floor(Math.random()*70);
						while (seq_pop[individuo][maquinas.get(cont)][tarMaqSelecionada]==-2){
							tarMaqSelecionada = (int)Math.floor(Math.random()*70);
						} 
						//Calculando o Makespan e Custo Antes
						float makespanAntes = calculoMakespan.calculoMakespanSequencia(seq_pop_aux, tarefa, matrizTarefaMaquina, numMaquinas, matrizSetup);
						float custoAntes = calculoCusto.calculoCustoSequencia(seq_pop_aux, tarefa, matrizTarefaMaquina, numMaquinas, maquina);
						//Efetuando a troca
						int aux = maquinaMaiorMakespan;
						pop_aux[tarMaqMaior] = maquinas.get(cont);
						pop_aux[tarMaqSelecionada] = aux;
						aux = seq_pop_aux[maquinaMaiorMakespan][tarMaqMaior];
						seq_pop_aux[maquinaMaiorMakespan][tarMaqMaior] = seq_pop_aux[maquinas.get(cont)][tarMaqSelecionada];
						seq_pop_aux[maquinas.get(cont)][tarMaqSelecionada] = aux;
						//Troca Efetuada
						//Calculando o Makespan e Custo Antes
						float makespanApos = calculoMakespan.calculoMakespanSequencia(seq_pop_aux, tarefa, matrizTarefaMaquina, numMaquinas, matrizSetup);
						float custoApos = calculoCusto.calculoCustoSequencia(seq_pop_aux, tarefa, matrizTarefaMaquina, numMaquinas, maquina);
						if (makespanApos<makespanAntes){//Melhorou
							seq_pop[individuo] = seq_pop_aux;
							for (int w=0; w<tarefa.length; w++){
								pop[w][individuo] = pop_aux[w];
							}
							melhorou = true;
							break;
						}else if(custoApos<custoAntes){
							seq_pop[individuo] = seq_pop_aux;
							for (int w=0; w<tarefa.length; w++){
								pop[w][individuo] = pop_aux[w];
							}
							melhorou = true;
							break;
						}
					}
				}
			}
		}
		return seq_pop;
	}
}
