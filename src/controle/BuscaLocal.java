package controle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Maquina;
import modelo.Tarefa;

public class BuscaLocal {
	public int[][][] buscaLocal(int [][][] seq_pop, Tarefa[] tarefa, int[][] matrizTarefaMaquina, int individuo, int entrega, int numIndividuos){
		//Verificar qual máquina possui o maior makespan
		int [][][] seq = new int[numIndividuos][1][tarefa.length];
		seq = seq_pop;
		int contNaoMelhora = 0;
		int maiorMakespan = 0;
		int [] seq_pop_anterior = new int [tarefa.length];
		for (int i=0; i<tarefa.length;i++) {
			seq_pop_anterior[i] = seq[individuo][0][i];
		}		
		//Encontrar uma troca de tarefa que melhore a melhor solução
		int numTarefas = tarefa.length;
		boolean melhorou = false;		
		while (!melhorou) {
			int posicao1 = (int) (Math.floor(Math.random()*numTarefas));
			int posicao2 = posicao1;		
			while (posicao1 == posicao2) {
				posicao2 = (int) (Math.floor(Math.random()*numTarefas));
			}
			
			CalculoAdiantamentoAtraso calculoAdiantamentoAtraso = new CalculoAdiantamentoAtraso();
			long makespanAnterior = calculoAdiantamentoAtraso.calculoAdiantamentoAtrasoSequencia(seq[individuo][0], tarefa, matrizTarefaMaquina, 1, entrega);
			//Efetuando a troca de Tarefas
			int aux = seq[individuo][0][posicao1];
			seq[individuo][0][posicao1] = seq[individuo][0][posicao2];
			seq[individuo][0][posicao2] = aux;
			long makespanPosterior = calculoAdiantamentoAtraso.calculoAdiantamentoAtrasoSequencia(seq[individuo][0], tarefa, matrizTarefaMaquina, 1, entrega);
			if (makespanPosterior<makespanAnterior) {
				melhorou = true;
			}else {
				for (int i=0; i<tarefa.length;i++) {
					seq[individuo][0][i] = seq_pop_anterior[i];
				}				
				contNaoMelhora++;
			}
			if (contNaoMelhora == 100) {
				break;
			}					
		}
		return seq;
	}
}
