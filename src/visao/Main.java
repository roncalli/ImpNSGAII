package visao;

import java.util.Random;

import controle.BuscaLocal;
import controle.CalculoCusto;
import controle.CalculoAdiantamentoAtraso;
import controle.LeiaCSV;
import controle.Operadores;
import controle.RelacoesDominancia;
import controle.SequenciamentoTarefas;
import modelo.Maquina;
import modelo.Tarefa;

public class Main {
	public static void main (String[] args) {		
		LeiaCSV lerArquivos = new LeiaCSV();
		//Par�metros do sistema
		int numTarefas = 100;
		//int numTarefas = 200;
		int entrega = 454;
		//int entrega = 851;
		long melhorMakespan = 1000000;
		int qtdeGerSemMelhora = 0;
		int gatilhoBuscaLocal = 10;
		boolean buscaLocal = false;
		int numMaquinas = 1;
		int numIndividuos = 100; //n�mero de indiv�duos
		
		//int numGer = 10000; //n�mero de gera��es
		int numGer = numTarefas*numIndividuos; //n�mero de gera��es	-para aumentar o numero de itera��es de acordo com a quantidade de tarefas	
		
		int varMur = 15; //Tipo uma vari�ncia da muta��o (n_mut = floor(rand*varMut)+qtdMut);
		
		//int qtdMut =  10; //% Percentual de indiv�duos mutados
		int qtdMut = varMur/(numTarefas/numIndividuos);//% Percentual de indiv�duos mutados
		
		
		Maquina maquina[] = new Maquina[numMaquinas];
		Tarefa tarefa[] = new Tarefa[numTarefas];
		int matrizTarefaMaquina[][] = new int[numTarefas][numMaquinas];
		lerArquivos.popularTabelas(tarefa, maquina, matrizTarefaMaquina,numMaquinas);
		//Fim dos par�metros do sistema
		
		
		// pop (tarefa, individuo) - valor da c�lula � a m�quina
		int [][] pop = new int [numTarefas][numIndividuos];		
		// Gera 100% dos indiv�duos de maneira aleat�ria
		for (int i=0; i<numTarefas; i++) {
			for (int j=0; j<numIndividuos; j++) {
				pop[i][j] = (int) (Math.floor(Math.random()*numMaquinas));
			}
		}
		// Sequenciamento da Popula��o
		// Formato:(sequencia da individuo, maquina, tarefa)
		int [][][] seq_pop = new int[numIndividuos][numMaquinas][numTarefas];
		SequenciamentoTarefas sequenciamento = new SequenciamentoTarefas();
		seq_pop = sequenciamento.sequenciamento_Inicial(numIndividuos, numMaquinas, numTarefas, pop, tarefa);
		
		//TESTE
		int [][][] seq_pop_teste = new int[numIndividuos][numMaquinas][numTarefas];
		seq_pop_teste = sequenciamento.gerarSequenciaInicial(matrizTarefaMaquina, tarefa, numIndividuos, entrega);
		for (int w=0; w<10; w++) {
			seq_pop[w][0] = seq_pop_teste[w][0];
		}
		//TESTE
		
		
		// C�lculos das fun��es objetivo
		// Fun��o Objetivo 1: C�lculo do makespan
		long [] makespan = new long [numIndividuos];
		CalculoAdiantamentoAtraso calculoMakespan = new CalculoAdiantamentoAtraso();
		makespan = calculoMakespan.calculoAdiantamentoAtraso(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,entrega);
		
		
		//Classifica��o por n�veis de n�o domin�ncia
		int[] nivelDominancia = new int[numIndividuos];
		RelacoesDominancia relacoesDominancia = new RelacoesDominancia();
		nivelDominancia = relacoesDominancia.calculaNivelDominancia(numIndividuos, makespan);
		
		int g = 0; // gera��o
		// Imprimindo Primeira Gera��o
						
		while(g<numGer) {
			//Seleciona os pais utilizando torneio de multid�o
			int [] resTorneio = new int[numIndividuos];
			Operadores operadores = new Operadores();
			resTorneio = operadores.operadorTorneio(numIndividuos, nivelDominancia);
			
			//Popula��o gerada pelo cruzamento
			int[][][] seq_pop_f = new int[numIndividuos][numMaquinas][numTarefas];
			seq_pop_f = operadores.operadorCruzamento(numTarefas, numIndividuos, resTorneio, seq_pop);			
						
			//Popula��o gerada pela muta��o
			seq_pop_f = operadores.operadorMutacao(numIndividuos, numMaquinas, numTarefas, qtdMut, varMur,seq_pop_f);						
			
			//C�lculo Mekespan da popula��o de filhos
			long [] makespan_f = new long [numIndividuos];
			makespan_f = calculoMakespan.calculoAdiantamentoAtraso(numIndividuos, numMaquinas, seq_pop_f, tarefa, matrizTarefaMaquina,entrega);
			
//			//INSERIR A BUSCA LOCAL//
			if (buscaLocal) {
				//Verificando qual indivisuo possui o menor makespan
				int individuo = 0;
				long makespanIndividuo = 1000000;
				for (int w=0; w<numIndividuos; w++) {
					if (makespan_f[w]<makespanIndividuo) {
						makespanIndividuo = makespan_f[w];
						individuo = w;
					}
				}
				BuscaLocal busca = new BuscaLocal();
				seq_pop_f = busca.buscaLocal(seq_pop_f, tarefa, matrizTarefaMaquina, individuo, entrega, numIndividuos);
				buscaLocal = false;
			}
//			//INSERIR A BUSCA LOCAL//
			makespan_f = calculoMakespan.calculoAdiantamentoAtraso(numIndividuos, numMaquinas, seq_pop_f, tarefa, matrizTarefaMaquina,entrega);
			//Concatenando Pais e Filhos
			int[][][] seq_pop_pai_filho = new int[2*numIndividuos][numMaquinas][numTarefas];
			for (int i=0; i<numTarefas; i++) {			
				for (int j=0; j<2*numIndividuos;j++) {
					if (j<numIndividuos){
						seq_pop_pai_filho[j][0][i] = seq_pop[j][0][i]; 						
					}else {										
						seq_pop_pai_filho[j][0][i] = seq_pop_f[j-100][0][i];						
					}					
				}
			}
					
			long[] makespan_pai_filho = new long[2*numIndividuos];
			for (int i=0; i<2*numIndividuos; i++) {
				if (i<numIndividuos) {
					makespan_pai_filho[i] = makespan[i];
				}else {
					makespan_pai_filho[i] = makespan_f[i-100];
				}
			}
			
				
			//Ranquamento de n�o domin�ncia dos 2n indiv�duos
			//n�vel de n�o domin�ncia
			nivelDominancia = relacoesDominancia.calculaNivelDominancia((2*numIndividuos), makespan_pai_filho);
			
			//Criando a nova popula��o
			int[][][] seq_pop_linha = new int[numIndividuos][numMaquinas][numTarefas]; //Nova popula��o			
			int nivel = 1; //n�vel de domin�ncia
			int j=0; // n�mero de indiv�duos adicionados
			int ind_vet = 0;			
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
								solucaoJaIncluida = operadores.verificaSolucoesIguais(makespan_pai_filho, cont, seq_pop_pai_filho, numTarefas);
								solucaoJaIncluida = false;
							}
							if (!solucaoJaIncluida){
								for (int k =0; k<numTarefas; k++) {
									if (nivelDominancia[cont]==nivel) {				
										seq_pop_linha[ind_vet][0][k] = seq_pop_pai_filho[cont][0][k];								
									}
								}
								ind_vet++;
							}
						}	
					}					
				}else {	//C�LCULO DA DIST�NCIA DE MULTID�O
					int [] posicao_nivel = new int[n_ind_nivel];
					long [] aux_mksp = new long[n_ind_nivel];
					int pos = 0;
					for (int cont = 0; cont< 2*numIndividuos; cont++) {
						if (nivelDominancia[cont] == nivel) {
							posicao_nivel[pos] = cont;
							aux_mksp[pos] = makespan_pai_filho[cont];							
							pos++;
						}
					}
					float [][] auxDist = new float[n_ind_nivel][2];
					float [] distMultidao = new float[n_ind_nivel];
					auxDist = operadores.calculoDistanciaMultidao(aux_mksp, posicao_nivel);
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
							for (int k =0; k<numTarefas; k++) {								
								seq_pop_linha[ind_vet][0][k] = seq_pop_pai_filho[posicao_nivel[distMultidao.length-1]][0][k];								
							}
							inseridos_borda++;
						}else { // if cont==1
							for (int k =0; k<numTarefas; k++) {								
								seq_pop_linha[ind_vet][0][k] = seq_pop_pai_filho[posicao_nivel[distMultidao.length-2]][0][k];
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
								solucaoJaIncluida = operadores.verificaSolucoesIguais(makespan_pai_filho, cont, seq_pop_pai_filho, numTarefas);
							}
							if (!solucaoJaIncluida){
								for (int k =0; k<numTarefas; k++) {								
									seq_pop_linha[ind_vet][0][k] = seq_pop_pai_filho[posicao_nivel[cont]][0][k];																
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
			//% Atribui a popula��o e a sequ�ncia dos indiv�duos da popula��o			
			seq_pop = seq_pop_linha;			
			makespan = calculoMakespan.calculoAdiantamentoAtraso(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,entrega);			
			nivelDominancia = relacoesDominancia.calculaNivelDominancia(numIndividuos, makespan);			
			Impressaoes imprimir = new Impressaoes();			
			long melhorMakespanGeracao = imprimir.imprimir(g, makespan, seq_pop, numIndividuos, nivelDominancia, numMaquinas, numTarefas);
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
			//Incremanta contador de gera��es
			g++;
		}	
	}
}
