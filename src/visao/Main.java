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
		//Parï¿½metros do sistema
		int numTarefas = 70;
		int numMaquinas = 4;
		int numIndividuos = 100; //nï¿½mero de indivï¿½duos
		float melhorMakespanGeracao = 100000;
		float mMakespan = 100000;
		float mCusto = 100000;
		float piorMakespanGeracao = 0;
		float piorCustoGeracao = 0;
		int qtdeGerSemMelhora=0;
		int gatilhoBuscaLocal = 50;
		boolean buscaLocal = false;
		float melhorMakespan = 100000;
		int numGer = 10000; //nï¿½mero de geraï¿½ï¿½es		
		int qtdMut =  10; //% Percentual de indivï¿½duos mutados
		Maquina maquina[] = new Maquina[numMaquinas];
		Tarefa tarefa[] = new Tarefa[numTarefas];
		float matrizTarefaMaquina[][] = new float[numTarefas][numMaquinas];
		float matrizSetup[][][] = new float[numMaquinas][numTarefas][numTarefas];
		lerArquivos.popularTabelas(tarefa, maquina, matrizTarefaMaquina,matrizSetup,numMaquinas);
		//Fim dos parï¿½metros do sistema
		
		// Formato:(sequencia da individuo, maquina, tarefa)	
		
		int [][][] seq_pop = new int[numIndividuos][numMaquinas][numTarefas];				
		SequenciamentoTarefas sequenciamento = new SequenciamentoTarefas();
		seq_pop = sequenciamento.sequenciamento_Inicial(numIndividuos, numMaquinas, numTarefas, tarefa);
		
		Operadores operadores = new Operadores();
		
		// Cï¿½lculos das funï¿½ï¿½es objetivo
		// Funï¿½ï¿½o Objetivo 1: Cï¿½lculo do makespan
		float [] makespan = new float [numIndividuos];
		CalculoMakespan calculoMakespan = new CalculoMakespan();
		makespan = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,matrizSetup);
		float auxMakespan = operadores.piorMakespan(makespan, numIndividuos);
		if (auxMakespan>piorMakespanGeracao) {
			piorMakespanGeracao = auxMakespan;
		}
		auxMakespan = operadores.melhorMakespan(makespan, numIndividuos);
		if (auxMakespan<mMakespan) {
			mMakespan = auxMakespan;
		}
		
		
		//Funï¿½ï¿½o Objetivo 2: Cï¿½lculo do Custo
		float[] custo = new float[numIndividuos];
		CalculoCusto calculoCusto = new CalculoCusto();
		custo = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_pop, matrizTarefaMaquina, maquina, numTarefas);
		float auxCusto = operadores.piorCusto(custo, numIndividuos);
		if (auxCusto>piorCustoGeracao) {
			piorCustoGeracao = auxCusto;
		}
		auxCusto = operadores.melhorCusto(custo, numIndividuos);
		if (auxCusto<mCusto) {
			mCusto = auxCusto;
		}
		
		
		//Classificaï¿½ï¿½o por nï¿½veis de nï¿½o dominï¿½ncia
		int[] nivelDominancia = new int[numIndividuos];
		RelacoesDominancia relacoesDominancia = new RelacoesDominancia();
		nivelDominancia = relacoesDominancia.calculaNivelDominancia(numIndividuos, makespan, custo);
		int g = 0; // geraï¿½ï¿½o
		// Imprimindo Primeira Geraï¿½ï¿½o
						
		while(g<numGer) {			
			//Seleciona os pais utilizando torneio de multidï¿½o
			int [] resTorneio = new int[numIndividuos];			
			resTorneio = operadores.operadorTorneio(numIndividuos, nivelDominancia);			
			//Populaï¿½ï¿½o gerada pelo cruzamento
			int[][][] seq_Pop_filhos = new int[numIndividuos][numMaquinas][numTarefas];			
			seq_Pop_filhos = operadores.operadorCruzamento(numTarefas, numIndividuos, numMaquinas, resTorneio, seq_pop);			
			
			//Populaç
			//seq_Pop_filhos = operadores.operadorMutacao(numIndividuos, numMaquinas, numTarefas, qtdMut, varMur,seq_Pop_filhos);
			seq_Pop_filhos = operadores.operadorMutacao(numIndividuos, numMaquinas, numTarefas, qtdMut, seq_Pop_filhos);									
			
			//Cï¿½lculo makespan da populaï¿½ï¿½o de filhos
			float [] makespan_f = new float [numIndividuos];
			float[] custo_f = new float[numIndividuos];					
			
			//INSERIR A BUSCA LOCAL//
			//BuscaLocalFalse
			if (buscaLocal) {
				BuscaLocal busca = new BuscaLocal();
				seq_Pop_filhos = busca.buscaLocalFirstImprovements(seq_Pop_filhos, numMaquinas, maquina, tarefa, matrizTarefaMaquina, matrizSetup, numIndividuos, numTarefas);
				buscaLocal = false;
			}						
			
			//LOCAL ONDE SERÁ COLOCADO O ARQUIVO
			if (g==0){ // Primeira geração, ainda não existe o arquivo
				lerArquivos.gerarCsvSolucao(numIndividuos, g,makespan_f, custo_f, tempoInicial, nivelDominancia);
				lerArquivos.gerarCsvSequenciaSolucao(numIndividuos, numMaquinas, numTarefas, g, makespan_f, custo_f, tempoInicial, nivelDominancia, seq_Pop_filhos);
			}else{ //Atualização do Arquivo
				int auxSeqPop[][][] = lerArquivos.lerArquivoSolucoes(numIndividuos, numMaquinas, numTarefas,g);	
				int contArquivo = 0;				
				for (int i=0; i<numIndividuos; i++) {
					if (auxSeqPop[i][0][0] == -2) {
						break;
					}					
					contArquivo++;
				}
				
				int indArq = 0;
				for (int i=numIndividuos-contArquivo; i<numIndividuos; i++) {				
					for (int j=0; j<numMaquinas; j++) {
						for (int k=0; k<numTarefas; k++) {
							seq_Pop_filhos[i][j][k] = auxSeqPop[indArq][j][k];
						}
					}
					indArq++;
				}
				makespan_f = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_Pop_filhos, tarefa, matrizTarefaMaquina,matrizSetup);
				auxMakespan = operadores.piorMakespan(makespan_f, numIndividuos);
				if (auxMakespan>piorMakespanGeracao) {
					piorMakespanGeracao = auxMakespan;
				}
				auxMakespan = operadores.melhorMakespan(makespan, numIndividuos);
				if (auxMakespan<mMakespan) {
					mMakespan = auxMakespan;
				}
				//Funï¿½ï¿½o Objetivo 2: Cï¿½lculo do Custo
				custo_f = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_Pop_filhos, matrizTarefaMaquina, maquina, numTarefas);
				auxCusto = operadores.piorCusto(custo, numIndividuos);
				if (auxCusto>piorCustoGeracao) {
					piorCustoGeracao = auxCusto;
				}
				auxCusto = operadores.melhorCusto(custo, numIndividuos);
				if (auxCusto<mCusto) {
					mCusto = auxCusto;
				}
				
				lerArquivos.gerarCsvSolucao(numIndividuos, g, makespan_f, custo, tempoInicial, nivelDominancia);
				lerArquivos.gerarCsvSequenciaSolucao(numIndividuos, numMaquinas, numTarefas, g, makespan_f, custo, tempoInicial, nivelDominancia, seq_Pop_filhos);
			}
			//FIM DO ARQUIVO
			
			makespan_f = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_Pop_filhos, tarefa, matrizTarefaMaquina,matrizSetup);
			auxMakespan = operadores.piorMakespan(makespan_f, numIndividuos);
			if (auxMakespan>piorMakespanGeracao) {
				piorMakespanGeracao = auxMakespan;
			}
			auxMakespan = operadores.melhorMakespan(makespan, numIndividuos);
			if (auxMakespan<mMakespan) {
				mMakespan = auxMakespan;
			}
			//Funï¿½ï¿½o Objetivo 2: Cï¿½lculo do Custo
			custo_f = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_Pop_filhos, matrizTarefaMaquina, maquina, numTarefas);
			auxCusto = operadores.piorCusto(custo, numIndividuos);
			if (auxCusto>piorCustoGeracao) {
				piorCustoGeracao = auxCusto;
			}
			auxCusto = operadores.melhorCusto(custo, numIndividuos);
			if (auxCusto<mCusto) {
				mCusto = auxCusto;
			}
			
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
				
			//Ranquamento de nï¿½o dominï¿½ncia dos 2n indivï¿½duos
			//nï¿½vel de nï¿½o dominï¿½ncia
			nivelDominancia = relacoesDominancia.calculaNivelDominancia((2*numIndividuos), makespan_pai_filho, custo_pai_filho);
			
			//Criando a nova populaï¿½ï¿½o
			int[][][] seq_pop_linha = new int[numIndividuos][numMaquinas][numTarefas]; //nova sequencia
			int nivel = 1; //nï¿½vel de dominï¿½ncia
			int j=0; // nï¿½mero de indivï¿½duos adicionados
			int ind_vet = 0;
			//criando vetor com os mais dominados (para gerar diversidade populacional)			
			while (j<numIndividuos) {//Enquanto nï¿½o selecionar 100 indivï¿½duos
				//Cï¿½lculo de indivï¿½duos de nï¿½vel j				
				int n_ind_nivel = 0; //nï¿½mero de individuos do nï¿½vel X
				//Calculando o nï¿½mero de indivï¿½duos em um determinado nï¿½vel
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
				
				//Adicionando todos os indivï¿½duos dos nï¿½veis mais baixos
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
				}else {	//Cï¿½LCULO DA DISTï¿½NCIA DE MULTIDï¿½O
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
					//Ordenando apï¿½s calcular a distï¿½ncia de multidï¿½o
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
					//Incluindo as soluï¿½ï¿½es de fronteira encontradas na Distï¿½ncia de Multidï¿½o
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
						if (ind_vet == 100) {//Apenas um indivï¿½duo no nï¿½vel escolhido							
							break;
						}
					}
					int n = numIndividuos - j; //Nï¿½mero de indivï¿½duos para completar a populaï¿½ï¿½o tirando as duas soluï¿½ï¿½es de borda incluidas
					for (int cont = inseridos_borda; cont<n; cont++) {
						//Jï¿½ INCLUIDO OS INDIVIDUOS DAS EXTREMIDADES =-1
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
				j = ind_vet;				
				nivel++;								
			}	
			//% Atribui a populaï¿½ï¿½o e a sequï¿½ncia dos indivï¿½duos da populaï¿½ï¿½o		
			//Copiando os indivíduos de seq_pop para seq_pop_f
			for (int q=0; q<numIndividuos; q++){
				for (int w=0; w<numMaquinas; w++){
					for (int r=0; r<numTarefas; r++){
						seq_pop[q][w][r] = seq_pop_linha[q][w][r];
					}
				}
			}
			makespan = calculoMakespan.calculoMakespan(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,matrizSetup);
			auxMakespan = operadores.piorMakespan(makespan_f, numIndividuos);
			if (auxMakespan>piorMakespanGeracao) {
				piorMakespanGeracao = auxMakespan;
			}
			auxMakespan = operadores.melhorMakespan(makespan, numIndividuos);
			if (auxMakespan<mMakespan) {
				mMakespan = auxMakespan;
			}
			
			custo = calculoCusto.calculoCusto(numIndividuos, numMaquinas, seq_pop, matrizTarefaMaquina, maquina, numTarefas);
			auxCusto = operadores.piorCusto(custo, numIndividuos);
			if (auxCusto>piorCustoGeracao) {
				piorCustoGeracao = auxCusto;
			}
			auxCusto = operadores.melhorCusto(custo, numIndividuos);
			if (auxCusto<mCusto) {
				mCusto = auxCusto;
			}
			
			nivelDominancia = relacoesDominancia.calculaNivelDominancia(numIndividuos, makespan, custo);			
			Impressaoes imprimir = new Impressaoes();
			melhorMakespanGeracao = imprimir.imprimir(g, makespan, custo, seq_pop, numIndividuos, nivelDominancia, numMaquinas, numTarefas);
			//Incremanta contador de geraï¿½ï¿½es
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
			auxMakespan = operadores.melhorMakespan(makespan, numIndividuos);
			auxCusto = operadores.melhorCusto(custo, numIndividuos);
			float auxPiorMakespan = operadores.piorMakespan(makespan, numIndividuos);
			float auxPiorCusto = operadores.piorCusto(custo, numIndividuos);
			if (g==500) {
				lerArquivos.gerarMelhorPiorFOBJ(g, auxMakespan, auxCusto, auxPiorCusto, auxPiorMakespan);
				lerArquivos.gerarCsvSequenciaSolucao(numIndividuos, numMaquinas, numTarefas, g, makespan, custo, tempoInicial, nivelDominancia, seq_pop_linha);
				lerArquivos.gerarCsvSolucaoResultados(numIndividuos, g, makespan, custo, tempoInicial, nivelDominancia);
				piorMakespanGeracao = 0;
				piorCustoGeracao = 0;
				mMakespan = 100000;
				mCusto = 100000;
			}
			if (g==1000) {
				lerArquivos.gerarMelhorPiorFOBJ(g, mMakespan, mCusto, piorCustoGeracao, piorMakespanGeracao);
				lerArquivos.gerarCsvSequenciaSolucao(numIndividuos, numMaquinas, numTarefas, g, makespan, custo, tempoInicial, nivelDominancia, seq_pop_linha);
				lerArquivos.gerarCsvSolucaoResultados(numIndividuos, g, makespan, custo, tempoInicial, nivelDominancia);
				piorMakespanGeracao = 0;
				piorCustoGeracao = 0;
				mMakespan = 100000;
				mCusto = 100000;
			}
			if (g==2000) {
				lerArquivos.gerarMelhorPiorFOBJ(g, mMakespan, mCusto, piorCustoGeracao, piorMakespanGeracao);
				lerArquivos.gerarCsvSequenciaSolucao(numIndividuos, numMaquinas, numTarefas, g, makespan, custo, tempoInicial, nivelDominancia, seq_pop_linha);
				lerArquivos.gerarCsvSolucaoResultados(numIndividuos, g, makespan, custo, tempoInicial, nivelDominancia);
				piorMakespanGeracao = 0;
				piorCustoGeracao = 0;
				mMakespan = 100000;
				mCusto = 100000;
			}
			if (g==5000) {
				lerArquivos.gerarMelhorPiorFOBJ(g, mMakespan, mCusto, piorCustoGeracao, piorMakespanGeracao);
				lerArquivos.gerarCsvSequenciaSolucao(numIndividuos, numMaquinas, numTarefas, g, makespan, custo, tempoInicial, nivelDominancia, seq_pop_linha);
				lerArquivos.gerarCsvSolucaoResultados(numIndividuos, g, makespan, custo, tempoInicial, nivelDominancia);
				piorMakespanGeracao = 0;
				piorCustoGeracao = 0;
				mMakespan = 100000;
				mCusto = 100000;
			}
			if (g==10000) {
				lerArquivos.gerarMelhorPiorFOBJ(g, mMakespan, mCusto, piorCustoGeracao, piorMakespanGeracao);
				lerArquivos.gerarCsvSequenciaSolucao(numIndividuos, numMaquinas, numTarefas, g, makespan, custo, tempoInicial, nivelDominancia, seq_pop_linha);
				lerArquivos.gerarCsvSolucaoResultados(numIndividuos, g, makespan, custo, tempoInicial, nivelDominancia);
				piorMakespanGeracao = 0;
				piorCustoGeracao = 0;
				mMakespan = 100000;
				mCusto = 100000;
			}			
		}
		//Imprimindo resultados		
		Impressaoes imprimir = new Impressaoes();
		imprimir.imprimir(g, makespan, custo, seq_pop, numIndividuos, nivelDominancia,numMaquinas, numTarefas);		
	}
}
