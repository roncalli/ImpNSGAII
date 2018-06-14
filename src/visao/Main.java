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
		//Parâmetros do sistema
		int numTarefas = 100;
		//int numTarefas = 200;
		int entrega = 454;
		//int entrega = 851;
		long melhorMakespan = 1000000;
		int qtdeGerSemMelhora = 0;
		int gatilhoBuscaLocal = 10;
		boolean buscaLocal = false;
		int numMaquinas = 1;
		int numIndividuos = 100; //número de indivíduos
		
		//int numGer = 10000; //número de gerações
		int numGer = numTarefas*numIndividuos; //número de gerações	-para aumentar o numero de iterações de acordo com a quantidade de tarefas	
		
		int varMur = 15; //Tipo uma variância da mutação (n_mut = floor(rand*varMut)+qtdMut);
		
		//int qtdMut =  10; //% Percentual de indivíduos mutados
		int qtdMut =10;
		
		if(numIndividuos<=numTarefas) {
			qtdMut= (int) (varMur/(Double.parseDouble(numTarefas+"")/numIndividuos));//% Percentual de indivíduos mutados
		}
		
		
		Maquina maquina[] = new Maquina[numMaquinas];
		Tarefa tarefa[] = new Tarefa[numTarefas];
		int matrizTarefaMaquina[][] = new int[numTarefas][numMaquinas];
		lerArquivos.popularTabelas(tarefa, maquina, matrizTarefaMaquina,numMaquinas);
		//Fim dos parâmetros do sistema
		
		
		// pop (tarefa, individuo) - valor da célula é a máquina
		int [][] pop = new int [numTarefas][numIndividuos];		
		// Gera 100% dos indivíduos de maneira aleatória
		for (int i=0; i<numTarefas; i++) {
			for (int j=0; j<numIndividuos; j++) {
				pop[i][j] = (int) (Math.floor(Math.random()*numMaquinas));
			}
		}
		// Sequenciamento da População
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
		
		
		// Cálculos das funções objetivo
		// Função Objetivo 1: Cálculo do makespan
		long [] makespan = new long [numIndividuos];
		CalculoAdiantamentoAtraso calculoMakespan = new CalculoAdiantamentoAtraso();
		makespan = calculoMakespan.calculoAdiantamentoAtraso(numIndividuos, numMaquinas, seq_pop, tarefa, matrizTarefaMaquina,entrega);
		
		
		//Classificação por níveis de não dominância
		int[] nivelDominancia = new int[numIndividuos];
		RelacoesDominancia relacoesDominancia = new RelacoesDominancia();
		nivelDominancia = relacoesDominancia.calculaNivelDominancia(numIndividuos, makespan);
		
		int g = 0; // geração
		// Imprimindo Primeira Geração
						
		while(g<numGer) {
			//Seleciona os pais utilizando torneio de multidão
			int [] resTorneio = new int[numIndividuos];
			Operadores operadores = new Operadores();
			resTorneio = operadores.operadorTorneio1(numIndividuos, nivelDominancia);
			
			//População gerada pelo cruzamento
			int[][][] seq_pop_f = new int[numIndividuos][numMaquinas][numTarefas];
			seq_pop_f = operadores.operadorCruzamento(numTarefas, numIndividuos, resTorneio, seq_pop);			
						
			//População gerada pela mutação
			seq_pop_f = operadores.operadorMutacao(numIndividuos, numMaquinas, numTarefas, qtdMut, varMur,seq_pop_f);						
			
			//Cálculo Mekespan da população de filhos
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
			
				
			//Ranquamento de não dominância dos 2n indivíduos
			//nível de não dominância
			nivelDominancia = relacoesDominancia.calculaNivelDominancia((2*numIndividuos), makespan_pai_filho);
			
			//Criando a nova população
			int[][][] seq_pop_linha = new int[numIndividuos][numMaquinas][numTarefas]; //Nova população			
			int nivel = 1; //nível de dominância
			int j=0; // número de indivíduos adicionados
			int ind_vet = 0;			
			while (j<numIndividuos) {//Enquanto não selecionar 100 indivíduos
				//Cálculo de indivíduos de nível j				
				int n_ind_nivel = 0; //número de individuos do nível X
				//Calculando o número de indivíduos em um determinado nível
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
				
				//Adicionando todos os indivíduos dos níveis mais baixos
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
				}else {	//CÁLCULO DA DISTÂNCIA DE MULTIDÃO
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
					//Ordenando após calcular a distância de multidão
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
					//Incluindo as soluções de fronteira encontradas na Distância de Multidão
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
						if (ind_vet == 100) {//Apenas um indivíduo no nível escolhido							
							break;
						}
					}
					int n = numIndividuos - j; //Número de indivíduos para completar a população tirando as duas soluções de borda incluidas
					for (int cont = inseridos_borda; cont<n; cont++) {
						//JÁ INCLUIDO OS INDIVIDUOS DAS EXTREMIDADES =-1
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
			//% Atribui a população e a sequência dos indivíduos da população			
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
			//Incremanta contador de gerações
			g++;
		}	
	}
}
