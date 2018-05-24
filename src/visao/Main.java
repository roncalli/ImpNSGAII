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
		//Par�metros do sistema
		int numTarefas = 100;
		int numMaquinas = 10;
		int numIndividuos = 100; //n�mero de indiv�duos
		int numExec = 5; //N�mero de execu��es
		int numGer = 10000; //n�mero de gera��es
		int nGetMut = 10; //numero m�dio de genes mutados
		int varMur = 10; //Tipo uma vari�ncia da muta��o (n_mut = floor(rand*varMut)+qtdMut);
		int qtdMut =  5; //% Percentual de indiv�duos mutados
		int qtdMaisDom = 30;//Percentual de mais dominados a compor a nova popula��o
		Maquina maquina[] = new Maquina[numMaquinas];
		Tarefa tarefa[] = new Tarefa[numTarefas];
		int matrizTarefaMaquina[][] = new int[numTarefas][numMaquinas];
		float matrizSetup[][][] = new float[numMaquinas][numTarefas][numTarefas];
		lerArquivos.popularTabelas(tarefa, maquina, matrizTarefaMaquina,matrizSetup,numMaquinas);
		//Fim dos par�metros do sistema
		
		
		// pop (tarefa, individuo) - valor da c�lula � a m�quina
		int [][] pop = new int [numTarefas][numIndividuos];		
		// Gera 100% dos indiv�duos de maneira aleat�ria
		for (int i=0; i<numTarefas; i++) {
			for (int j=0; j<numIndividuos; j++) {
				pop[i][j] = (int) (Math.floor(Math.random()*numMaquinas));
			}
		}
//		//% Gera 5% dos indiv�duos atribuindo cada tarefa a m�quina que a executa no menor tempo
//		for (int j=95; j<numIndividuos; j++) { 
//			for (int i=0; i<numTarefas; i++) {
//				//Buscando qual m�quina executa a tarefa em menos tempo				
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
		// Sequenciamento da Popula��o
		// Formato:(sequencia da individuo, maquina, tarefa)
		int [][][] seq_pop = new int[numIndividuos][numMaquinas][numTarefas];
		SequenciamentoTarefas sequenciamento = new SequenciamentoTarefas();
		seq_pop = sequenciamento.sequenciamento_Inicial(numIndividuos, numMaquinas, numTarefas, pop, tarefa);
		
		
		// C�lculos das fun��es objetivo
		// Fun��o Objetivo 1: C�lculo do makespan
		int [] makespan = new int [numIndividuos];
		CalculoMakespan calculoMakespan = new CalculoMakespan();
		makespan = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,matrizSetup);
		
		//Fun��o Objetivo 2: C�lculo do Custo
		float[] custo = new float[numIndividuos];
		calculoCusto calculoCusto = new calculoCusto();
		custo = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_pop, matrizTarefaMaquina, maquina);
		
		//Classifica��o por n�veis de n�o domin�ncia
		int[] nivelDominancia = new int[numIndividuos];
		RelacoesDominancia relacoesDominancia = new RelacoesDominancia();
		nivelDominancia = relacoesDominancia.calculaNivelDominancia(numIndividuos, makespan, custo);
		
		int g = 0; // gera��o
		// Imprimindo Primeira Gera��o
						
		while(g<numGer) {
			//Seleciona os pais utilizando torneio de multid�o
			int [] resTorneio = new int[numIndividuos];
			Operadores operadores = new Operadores();
			resTorneio = operadores.operadorTorneio(numIndividuos, nivelDominancia);
			
			//Popula��o gerada pelo cruzamento
			int[][] pop_f = new int[numTarefas][numIndividuos];
			pop_f = operadores.operadorCruzamento(numTarefas, numIndividuos, resTorneio, pop);
			
			
			//Popula��o gerada pela muta��o
			pop_f = operadores.operadorMutacao(numIndividuos, numMaquinas, numTarefas, qtdMut, varMur,pop_f);
			
			//Sequenciamento da Popula��o de Filhos
			int[][][] seq_Pop_filhos = new int[numIndividuos][numMaquinas][numTarefas];
			seq_Pop_filhos = sequenciamento.sequenciamento_Inicial(numIndividuos, numMaquinas, numTarefas, pop_f, tarefa);
			
			//Tenta encontrar uma melhor sequenciamento de tarefas dos filhos por busca aleat�ria
			//VERIFICAR SE � NECESS�RIO			
			for (int i = 0; i<numIndividuos; i++) {
				for (int j=0; j<numMaquinas;j++) {
					int[]nova_seq = new int[numMaquinas];
					nova_seq = sequenciamento.calculoMelhorSequenciamentoMaquina(numMaquinas, numIndividuos, numTarefas, seq_pop[i][j], tarefa, matrizTarefaMaquina, j, matrizSetup);										
					for (int k=0; k<nova_seq.length; k++) {
						seq_pop[i][j][k] = nova_seq[k];
					}
				}
			}
			//VERIFICAR SE � NECESS�RIO
			
			//C�lculo makespan da popula��o de filhos
			int [] makespan_f = new int [numIndividuos];
			makespan_f = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_Pop_filhos, tarefa, matrizTarefaMaquina,matrizSetup);
			
			//Fun��o Objetivo 2: C�lculo do Custo
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
				
			//Ranquamento de n�o domin�ncia dos 2n indiv�duos
			//n�vel de n�o domin�ncia
			nivelDominancia = relacoesDominancia.calculaNivelDominancia((2*numIndividuos), makespan_pai_filho, custo_pai_filho);
			
			//Criando a nova popula��o
			int[][] pop_linha = new int[numTarefas][numIndividuos]; //Nova popula��o
			int[][][] seq_pop_linha = new int[numIndividuos][numMaquinas][numTarefas]; //nova sequencia
			int nivel = 1; //n�vel de domin�ncia
			int j=0; // n�mero de indiv�duos adicionados
			int ind_vet = 0;
			//criando vetor com os mais dominados (para gerar diversidade populacional)
			int [] maisDominados = new int[((numIndividuos*qtdMaisDom)/100)];
			maisDominados = relacoesDominancia.retornarIndividuosMaisDominados(numIndividuos, nivelDominancia, qtdMaisDom);
			while (j<numIndividuos) {//Enquanto n�o selecionar 100 indiv�duos
				//C�lculo de indiv�duos de n�vel j				
				int n_ind_nivel = 0; //n�mero de individuos do n�vel X
				//Calculando o n�mero de indiv�duos em um determinado n�vel
				for (int cont = 0; cont< 2*numIndividuos; cont++) {
					if (nivelDominancia[cont] == nivel) {
						n_ind_nivel++;
					}
				}
				
				//Adicionando todos os indiv�duos dos n�veis mais baixos
				if (j+n_ind_nivel < 100) {
					for (int cont = 0; cont< 2*numIndividuos; cont++) {
						if (nivelDominancia[cont]==nivel) {
							boolean solucaoJaIncluida = false;
							if (cont>0){
								solucaoJaIncluida = operadores.verificaSolucoesIguais(makespan_pai_filho, custo_pai_filho, cont);
							}
							if (!solucaoJaIncluida){
								for (int k =0; k<numTarefas; k++) {
									if (nivelDominancia[cont]==nivel) {				
										pop_linha[k][ind_vet] = pop_pai_filho[k][cont];								
									}
								}
								ind_vet++;
							}
						}	
					}					
				}else {	//C�LCULO DA DIST�NCIA DE MULTID�O
					int [] posicao_nivel = new int[n_ind_nivel];
					int [] aux_mksp = new int[n_ind_nivel];
					float [] aux_custo = new float[n_ind_nivel];
					int pos = 0;
					for (int cont = 0; cont< 2*numIndividuos; cont++) {
						if (nivelDominancia[cont] == nivel) {
							posicao_nivel[pos] = cont;
							aux_mksp[pos] = makespan_pai_filho[cont];
							aux_custo[pos] = custo_pai_filho[cont];
							pos++;
						}
					}
					float [][] auxDist = new float[n_ind_nivel][2];
					float [] distMultidao = new float[n_ind_nivel];
					auxDist = operadores.calculoDistanciaMultidao(aux_mksp, aux_custo, 2,posicao_nivel);
					for (int w=0; w<n_ind_nivel; w++){
						distMultidao[w] = auxDist[w][0];
						posicao_nivel[w] = (int)auxDist[w][1];
					}
					float aux = 0;
					int auxPos = 0;
					//Ordenando ap�s calcular a dist�ncia de multid�o
					for (int w = 0; w<distMultidao.length; w++) {
						for (int k=w; k<distMultidao.length; k++) {
							if (distMultidao[k]>distMultidao[w]) {
								aux = distMultidao[w];
								auxPos = posicao_nivel[w];
								distMultidao[w] = distMultidao[k];
								posicao_nivel[w] = posicao_nivel[k];
								distMultidao[k] = aux;
								posicao_nivel[k] = auxPos;
							}
						}
					}
					int n = numIndividuos - j; //N�mero de indiv�duos para completar a popula��o
					for (int cont = 0; cont<n; cont++) {
						if (distMultidao[cont]!=-1) {
							boolean solucaoJaIncluida = false;
							if (cont>0){
								solucaoJaIncluida = operadores.verificaSolucoesIguais(makespan_pai_filho, custo_pai_filho, cont);
							}
							if (!solucaoJaIncluida){
								for (int k =0; k<numTarefas; k++) {								
									pop_linha[k][ind_vet] = pop_pai_filho[k][posicao_nivel[cont]];																
								}
								ind_vet++;
								if (cont+ind_vet >=100) {
									break;
								}
							}
						}
					}					
				}
				seq_pop_linha = sequenciamento.sequenciamento_Inicial(numIndividuos, numMaquinas, numTarefas, pop_linha, tarefa);
				j+=n_ind_nivel;
				nivel++;								
			}
			j = 0;
			ind_vet = numIndividuos - ((numIndividuos*qtdMaisDom)/100);
			while(j<((numIndividuos*qtdMaisDom)/100)) {
				for (int k =0; k<numTarefas; k++) {
					pop_linha[k][ind_vet] = pop_pai_filho[k][maisDominados[j]];	
				}				
				j++;
				ind_vet++;
			}
			//% Atribui a popula��o e a sequ�ncia dos indiv�duos da popula��o
			pop = pop_linha;
			seq_pop = seq_pop_linha;
			
			makespan = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,matrizSetup);
			custo = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_pop, matrizTarefaMaquina, maquina);
			
			nivelDominancia = relacoesDominancia.calculaNivelDominancia(numIndividuos, makespan, custo);			
			Impressaoes imprimir = new Impressaoes();
			imprimir.imprimir(g, makespan, custo, seq_pop, numIndividuos, nivelDominancia, numMaquinas);
			System.out.println("Teste");
			//Incremanta contador de gera��es
			g++;
		}
		//Imprimindo resultados		
		Impressaoes imprimir = new Impressaoes();
		imprimir.imprimir(g, makespan, custo, seq_pop, numIndividuos, nivelDominancia,numMaquinas);
		System.out.println("Teste");
	}
}
