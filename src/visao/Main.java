package visao;

import java.util.Random;

import controle.CalculoMakespan;
import controle.LeiaCSV;
import controle.Operadores;
import controle.RelacoesDominancia;
import controle.SequenciamentoTarefas;
import controle.calculoCusto;
import modelo.Maquina;
import modelo.Tarefa;

public class Main {
	public static void main (String[] args) {		
		LeiaCSV lerArquivos = new LeiaCSV();
		//Parâmetros do sistema
		int numTarefas = 20;
		int numMaquinas = 5;
		int numIndividuos = 100; //número de indivíduos
		int numExec = 5; //Número de execuções
		int numGer = 5; //número de gerações
		int nGetMut = 10; //numero médio de genes mutados
		int varMur = 10; //Tipo uma variância da mutação (n_mut = floor(rand*varMut)+qtdMut);
		int qtdMut =  5; //% Percentual de indivíduos mutados
		Maquina maquina[] = new Maquina[numMaquinas];
		Tarefa tarefa[] = new Tarefa[numTarefas];
		int matrizTarefaMaquina[][] = new int[numTarefas][numMaquinas];
		float matrizSetup[][][] = new float[numMaquinas][numTarefas][numTarefas];
		lerArquivos.popularTabelas(tarefa, maquina, matrizTarefaMaquina,matrizSetup,numMaquinas);
		//Fim dos parâmetros do sistema
		
		
		// pop (tarefa, individuo) - valor da célula é a máquina
		int [][] pop = new int [numTarefas][numIndividuos];		
		// Gera 100% dos indivíduos de maneira aleatória
		for (int i=0; i<numTarefas; i++) {
			for (int j=0; j<numIndividuos; j++) {
				pop[i][j] = (int) (Math.floor(Math.random()*numMaquinas));
			}
		}
//		//% Gera 5% dos indivíduos atribuindo cada tarefa a máquina que a executa no menor tempo
//		for (int j=95; j<numIndividuos; j++) { 
//			for (int i=0; i<numTarefas; i++) {
//				//Buscando qual máquina executa a tarefa em menos tempo				
//				for (int k=0; k<numTarefas; k++) {
//					float tempoExec = 1000;
//					int indice = -1;
//					for (int l=0; l<numMaquinas; l++) {
//						if (matrizTarefaMaquina[k][l]<tempoExec) {
//							indice = l;
//							tempoExec = matrizTarefaMaquina[k][l];
//						}
//					pop[i][j] = indice;
//					}
//				}				
//			}
//		}
		// Sequenciamento da População
		// Formato:(sequencia da individuo, maquina, tarefa)
		int [][][] seq_pop = new int[numIndividuos][numMaquinas][numTarefas];
		SequenciamentoTarefas sequenciamento = new SequenciamentoTarefas();
		seq_pop = sequenciamento.sequenciamento_Inicial(numIndividuos, numMaquinas, numTarefas, pop, tarefa);
		
		
		// Cálculos das funções objetivo
		// Função Objetivo 1: Cálculo do makespan
		int [] makespan = new int [numIndividuos];
		CalculoMakespan calculoMakespan = new CalculoMakespan();
		makespan = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,matrizSetup);
		
		//Função Objetivo 2: Cálculo do Custo
		float[] custo = new float[numIndividuos];
		calculoCusto calculoCusto = new calculoCusto();
		custo = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_pop, matrizTarefaMaquina, maquina);
		
		//Classificação por níveis de não dominância
		int[] nivelDominancia = new int[numIndividuos];
		RelacoesDominancia relacoesDominancia = new RelacoesDominancia();
		nivelDominancia = relacoesDominancia.calculaNivelDominancia(numIndividuos, makespan, custo);
		
		int g = 0; // geração
		// Imprimindo Primeira Geração
						
		while(g<numGer) {
			//Seleciona os pais utilizando torneio de multidão
			int [] resTorneio = new int[numIndividuos];
			Operadores operadores = new Operadores();
			resTorneio = operadores.operadorTorneio(numIndividuos, nivelDominancia);
			
			//População gerada pelo cruzamento
			int[][] pop_f = new int[numTarefas][numIndividuos];
			pop_f = operadores.operadorCruzamento(numTarefas, numIndividuos, resTorneio, pop);
			
			
			//População gerada pela mutação
			pop_f = operadores.operadorMutacao(numIndividuos, numMaquinas, numTarefas, qtdMut, varMur,pop_f);
			
			//Sequenciamento da População de Filhos
			int[][][] seq_Pop_filhos = new int[numIndividuos][numMaquinas][numTarefas];
			seq_Pop_filhos = sequenciamento.sequenciamento_Inicial(numIndividuos, numMaquinas, numTarefas, pop_f, tarefa);
			
			//Tenta encontrar uma melhor sequenciamento de tarefas dos filhos por busca aleatória
			//VERIFICAR SE É NECESSÁRIO			
			for (int i = 0; i<numIndividuos; i++) {
				for (int j=0; j<numMaquinas;j++) {
					int[]nova_seq = new int[numMaquinas];
					nova_seq = sequenciamento.calculoMelhorSequenciamentoMaquina(numMaquinas, numIndividuos, numTarefas, seq_pop[i][j], tarefa, matrizTarefaMaquina, j, matrizSetup);										
					for (int k=0; k<nova_seq.length; k++) {
						seq_pop[i][j][k] = nova_seq[k];
					}
				}
			}
			//VERIFICAR SE É NECESSÁRIO
			
			//Cálculo makespan da população de filhos
			int [] makespan_f = new int [numIndividuos];
			makespan_f = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_Pop_filhos, tarefa, matrizTarefaMaquina,matrizSetup);
			
			//Função Objetivo 2: Cálculo do Custo
			float[] custo_f = new float[numIndividuos];			
			custo_f = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_Pop_filhos, matrizTarefaMaquina, maquina);			
			
			//Concatenando Pais e Filhos
			int[][] pop_pai_filho = new int[numTarefas][2*numIndividuos];
			for (int i=0; i<numTarefas; i++) {			
				for (int j=0; j<2*numIndividuos;j++) {
					if (j<numIndividuos){
						pop_pai_filho[i][j] = pop[i][j]; 						
					}else {										
						pop_pai_filho[i][j] = pop_f[i][j-100];						
					}					
				}
			}
			
			int [][][]seq_pai_filho = new int[2*numIndividuos][numMaquinas][numTarefas];
			for (int i=0; i<2*numIndividuos; i++) {
				if (i<numIndividuos) {
					for (int j=0; j<numMaquinas;j++) {
						for (int k=0; k<numTarefas;k++) {
							seq_pai_filho[i][j][k] = seq_pop[i][j][k]; 
						}
					}					
				}else {
					for (int j=0; j<numMaquinas;j++) {
						for (int k=0; k<numTarefas;k++) {
							seq_pai_filho[i][j][k] = seq_Pop_filhos[i-100][j][k];
						}
					}	
				}
			}
			int[] makespan_pai_filho = new int[2*numIndividuos];
			for (int i=0; i<2*numIndividuos; i++) {
				if (i<numIndividuos) {
					makespan_pai_filho[i] = makespan[i];
				}else {
					makespan_pai_filho[i] = makespan_f[i-100];
				}
			}
			
			float[]custo_pai_filho = new float[2*numIndividuos];
			for (int i=0; i<2*numIndividuos; i++) {
				if (i<numIndividuos) {
					custo_pai_filho[i] = custo[i];
				}else {
					custo_pai_filho[i] = custo_f[i-100];
				}
			}
				
			//Ranquamento de não dominância dos 2n indivíduos
			//nível de não dominância
			nivelDominancia = relacoesDominancia.calculaNivelDominancia((2*numIndividuos), makespan_pai_filho, custo_pai_filho);
			
			//Criando a nova população
			int[][] pop_linha = new int[numTarefas][numIndividuos]; //Nova população
			int[][][] seq_pop_linha = new int[numIndividuos][numMaquinas][numTarefas]; //nova sequencia
			int nivel = 1; //nível de dominância
			int j=0; // número de indivíduos adicionados
			int ind_vet = 0;
			while (j<100) {//Enquanto não selecionar 100 indivíduos
				//Cálculo de indivíduos de nível j				
				int n_ind_nivel = 0; //número de individuos do nível X				
				//Adicionando todos os indivíduos dos níveis mais baixos
				for (int cont = 0; cont< 2*numIndividuos; cont++) {
					if (nivelDominancia[cont]==nivel) {						
						for (int k =0; k<numTarefas; k++) {
							if (nivelDominancia[cont]==nivel) {
								pop_linha[k][ind_vet] = pop_pai_filho[k][cont];								
							}
						}
						n_ind_nivel++;
						ind_vet++;
						if (j+n_ind_nivel>=100) {
							break;
						}
						//VERIFICAR CÁLCULO DA DISTÂNCIA DE MULTIDÃO
					}					
					
				}
				seq_pop_linha = sequenciamento.sequenciamento_Inicial(numIndividuos, numMaquinas, numTarefas, pop_linha, tarefa);
				j+=n_ind_nivel;
				nivel++;								
			}
			
			//% Atribui a população e a sequência dos indivíduos da população
			pop = pop_linha;
			seq_pop = seq_pop_linha;
			
			makespan = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,matrizSetup);
			custo = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_pop, matrizTarefaMaquina, maquina);
			
			nivelDominancia = relacoesDominancia.calculaNivelDominancia(numIndividuos, makespan, custo);			
			Impressaoes imprimir = new Impressaoes();
			imprimir.imprimir(g, makespan, custo, seq_pop, numIndividuos, nivelDominancia, numMaquinas);
			System.out.println("Teste");
			//Incremanta contador de gerações
			g++;
		}
		//Imprimindo resultados		
		Impressaoes imprimir = new Impressaoes();
		imprimir.imprimir(g, makespan, custo, seq_pop, numIndividuos, nivelDominancia,numMaquinas);
		System.out.println("Teste");
	}
}
