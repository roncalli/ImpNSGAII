package visao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import controle.BuscaLocal;
import controle.CalculoCusto;
import controle.CalculoMakespan;
import controle.LeiaCSV;
import controle.Operadores;
import controle.RelacoesDominancia;
import controle.SequenciamentoTarefas;
import modelo.Maquina;
import modelo.Tarefa;

public class Main {
	public static void main (String[] args) throws IOException {	
		long tempoInicial = System.currentTimeMillis();
		LeiaCSV lerArquivos = new LeiaCSV();
		//Par�metros do sistema
		int numTarefas = 70;
		int numMaquinas = 4;
		int numIndividuos = 100; //n�mero de indiv�duos
		float melhorMakespanGeracao = 100000;
		int qtdeGerSemMelhora=0;
		int gatilhoBuscaLocal = 0;
		boolean buscaLocal = false;
		float melhorMakespan = 100000;
		int numGer = 10000; //n�mero de gera��es		
		int varMur = 10; //Tipo uma vari�ncia da muta��o (n_mut = floor(rand*varMut)+qtdMut);
		int qtdMut =  10; //% Percentual de indiv�duos mutados
		Maquina maquina[] = new Maquina[numMaquinas];
		Tarefa tarefa[] = new Tarefa[numTarefas];
		float matrizTarefaMaquina[][] = new float[numTarefas][numMaquinas];
		float matrizSetup[][][] = new float[numMaquinas][numTarefas][numTarefas];
		lerArquivos.popularTabelas(tarefa, maquina, matrizTarefaMaquina,matrizSetup,numMaquinas);
		//Fim dos par�metros do sistema
		
		
		// pop (tarefa, individuo) - valor da c�lula � a m�quina		
		// Gera 100% dos indiv�duos de maneira aleat�ria
		// Sequenciamento da Popula��o
		// Formato:(sequencia da individuo, maquina, tarefa)	
		
		int [][][] seq_pop = new int[numIndividuos][numMaquinas][numTarefas];				
		SequenciamentoTarefas sequenciamento = new SequenciamentoTarefas();
		seq_pop = sequenciamento.sequenciamento_Inicial(numIndividuos, numMaquinas, numTarefas, tarefa);
		
		
		// C�lculos das fun��es objetivo
		// Fun��o Objetivo 1: C�lculo do makespan
		float [] makespan = new float [numIndividuos];
		CalculoMakespan calculoMakespan = new CalculoMakespan();
		makespan = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,matrizSetup);
		
		//Fun��o Objetivo 2: C�lculo do Custo
		float[] custo = new float[numIndividuos];
		CalculoCusto calculoCusto = new CalculoCusto();
		custo = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_pop, matrizTarefaMaquina, maquina, numTarefas);
		
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
			int[][][] seq_Pop_filhos = new int[numIndividuos][numMaquinas][numTarefas];			
			//seq_Pop_filhos = operadores.operadorCruzamento(numTarefas, numIndividuos, numMaquinas, resTorneio, seq_pop);
			
			
			//Popula��o gerada pela muta��o
			//seq_Pop_filhos = operadores.operadorMutacao(numIndividuos, numMaquinas, numTarefas, qtdMut, varMur,seq_Pop_filhos);						
											
			//PALEATIVO
			for (int w=0; w<numIndividuos; w++) {
				for (int q=0; q<numMaquinas; q++) {
					for (int t=0; t<numTarefas; t++) {
						seq_Pop_filhos[w][q][t] = seq_pop[w][q][t];
					}
				}
			}
			//PALEATIVO
			
			//C�lculo makespan da popula��o de filhos
			float [] makespan_f = new float [numIndividuos];
			makespan_f = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_Pop_filhos, tarefa, matrizTarefaMaquina,matrizSetup);
			
			//INSERIR A BUSCA LOCAL//
			if (buscaLocal) {
				//Verificando qual indivisuo possui o menor atrasoAdiantamento
				System.out.println("Busca Local");
				int individuo = 0;
				float makespanIndividuo = 1000000;
				for (int w=0; w<numIndividuos; w++) {
					if (makespan_f[w]<makespanIndividuo) {
						makespanIndividuo = makespan_f[w];
						individuo = w;
					}
				}
				BuscaLocal busca = new BuscaLocal();
				seq_Pop_filhos = busca.buscaLocal(seq_Pop_filhos, numMaquinas, maquina, tarefa, matrizTarefaMaquina, matrizSetup, individuo, numIndividuos);
				buscaLocal = false;
			}
			makespan_f = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_Pop_filhos, tarefa, matrizTarefaMaquina,matrizSetup);
			//Fun��o Objetivo 2: C�lculo do Custo
			float[] custo_f = new float[numIndividuos];			
			custo_f = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_Pop_filhos, matrizTarefaMaquina, maquina, numTarefas);			
			
			//Concatenando Pais e Filhos
						
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
			float[] makespan_pai_filho = new float[2*numIndividuos];
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
			while (j<numIndividuos) {//Enquanto n�o selecionar 100 indiv�duos
				//C�lculo de indiv�duos de n�vel j				
				int n_ind_nivel = 0; //n�mero de individuos do n�vel X
				//Calculando o n�mero de indiv�duos em um determinado n�vel
				int maiorNivelDominancia = 0;
				for (int cont = 0; cont< 2*numIndividuos; cont++) {
					if (nivelDominancia[cont]>maiorNivelDominancia) {
						maiorNivelDominancia = nivelDominancia[cont];
					}
					if (nivelDominancia[cont] == nivel) {
						n_ind_nivel++;
					}
				}
				if (nivel>maiorNivelDominancia) {
					break;
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
								for (int w=0; w<numMaquinas; w++){
									for (int k =0; k<numTarefas; k++) {													
										seq_pop_linha[ind_vet][w][k] = 	seq_pai_filho[cont][w][k];																				
									}
								}
								ind_vet++;
							}
						}	
					}					
				}else {	//C�LCULO DA DIST�NCIA DE MULTID�O
					int [] posicao_nivel = new int[n_ind_nivel];
					float [] aux_mksp = new float[n_ind_nivel];
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
					//Incluindo as solu��es de fronteira encontradas na Dist�ncia de Multid�o
					int inseridos_borda = 0;
					for (int cont = 0; cont<2; cont++) {
						if (cont==0) {							
							for (int w=0; w<numMaquinas; w++){
								for (int k =0; k<numTarefas; k++) {													
									seq_pop_linha[ind_vet][w][k] = 	seq_pai_filho[cont][w][k];																				
								}
							}
							inseridos_borda++;
						}else { // if cont==1							
							for (int w=0; w<numMaquinas; w++){
								for (int k =0; k<numTarefas; k++) {													
									seq_pop_linha[ind_vet][w][k] = 	seq_pai_filho[cont][w][k];																				
								}
							}
							inseridos_borda++;
						}
						ind_vet++;	
						if (ind_vet == 100) {//Apenas um indiv�duo no n�vel escolhido							
							break;
						}
					}
					int n = numIndividuos - j; //N�mero de indiv�duos para completar a popula��o tirando as duas solu��es de borda incluidas
					for (int cont = inseridos_borda; cont<n; cont++) {
						//J� INCLUIDO OS INDIVIDUOS DAS EXTREMIDADES =-1
						if (distMultidao[cont]!=-1) {
							boolean solucaoJaIncluida = false;
							if (cont>0){
								solucaoJaIncluida = operadores.verificaSolucoesIguais(makespan_pai_filho, custo_pai_filho, cont);
							}
							if (!solucaoJaIncluida){								
								for (int w=0; w<numMaquinas; w++){
									for (int k =0; k<numTarefas; k++) {													
										seq_pop_linha[ind_vet][w][k] = 	seq_pai_filho[cont][w][k];																				
									}
								}
								ind_vet++;
								if (ind_vet >=100) {									
									break;
								}
							}
						}
					}					
				}
				//Verificar
				//seq_pop_linha = sequenciamento.sequenciamento_Inicial(numIndividuos, numMaquinas, numTarefas, pop_linha, tarefa);
				//VERIFICAR
				//j+=n_ind_nivel;
				j = ind_vet;				
				nivel++;								
			}	
	
			//% Atribui a popula��o e a sequ�ncia dos indiv�duos da popula��o			
			seq_pop = seq_pop_linha;
			
			makespan = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,matrizSetup);
			custo = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_pop, matrizTarefaMaquina, maquina, numTarefas);
			
			nivelDominancia = relacoesDominancia.calculaNivelDominancia(numIndividuos, makespan, custo);			
			Impressaoes imprimir = new Impressaoes();
			melhorMakespanGeracao = imprimir.imprimir(g, makespan, custo, seq_pop, numIndividuos, nivelDominancia, numMaquinas, numTarefas);
			System.out.println("Teste");
			//Incremanta contador de gera��es
			if (melhorMakespanGeracao<melhorMakespan) {
				melhorMakespan = melhorMakespanGeracao;
				qtdeGerSemMelhora = 0;
			}else {
				qtdeGerSemMelhora++;
			}
			if (qtdeGerSemMelhora == gatilhoBuscaLocal) {
				buscaLocal = true;
				qtdeGerSemMelhora = 0;
			}
			g++;
			if (g==500) {
				lerArquivos.gerarCsvSolucao(numIndividuos, g, makespan, custo, tempoInicial,nivelDominancia);
			}
			if (g==1000) {
				lerArquivos.gerarCsvSolucao(numIndividuos, g, makespan, custo, tempoInicial,nivelDominancia);
			}
			if (g==2000) {
				lerArquivos.gerarCsvSolucao(numIndividuos, g, makespan, custo, tempoInicial,nivelDominancia);
			}
			if (g==5000) {
				lerArquivos.gerarCsvSolucao(numIndividuos, g, makespan, custo, tempoInicial,nivelDominancia);
			}
			if (g==10000) {
				lerArquivos.gerarCsvSolucao(numIndividuos, g, makespan, custo, tempoInicial,nivelDominancia);
			}			
		}
		//Imprimindo resultados		
		Impressaoes imprimir = new Impressaoes();
		imprimir.imprimir(g, makespan, custo, seq_pop, numIndividuos, nivelDominancia,numMaquinas, numTarefas);		
	}
}
