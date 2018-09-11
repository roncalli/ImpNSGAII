package controle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Maquina;
import modelo.Tarefa;

public class BuscaLocal {
	public int [][][] buscaLocalFirstImprovements(int [][][] seq_pop, int numMaquinas, Maquina[] maquina, Tarefa[] tarefa, float[][] matrizTarefaMaquina, float[][][] matrizSetup, int numIndividuos, int numTarefas, float[] auxMakespan, float[] auxCusto){
		//Implementar utilizando First Improvements utilizando a idéia do Carrano, gerando lista aleatórias de posições e de encaixe das tarefas
		int pos = (int)(Math.random()*numIndividuos);
		
		// Incluindo distância de Multidão para a seleção dos indivíduos 
		int qtde = (int) (numIndividuos*5)/100;
		int [] escolhidos = new int [qtde];
		float [][] auxDist = new float[numIndividuos][2];
		Operadores operadores = new Operadores();
		int[] posicao_nivel = new int[numIndividuos];
		for (int i=0; i<numIndividuos; i++){
			posicao_nivel[i] = i;
		}
		auxDist = operadores.calculoDistanciaMultidao(auxMakespan, auxCusto, 2,posicao_nivel);
		float []distMultidao = new float[numIndividuos];

		//Ordenando após calcular a distância de multidão
		for (int w=0; w<numIndividuos; w++){
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
		
		for (int i=0; i<qtde; i++){
			if (i == 0){//Primeiro elemento da borda
				escolhidos[i] = posicao_nivel[numIndividuos-2];
			}else if (i == (qtde-1)){
				escolhidos[i] = posicao_nivel[numIndividuos-1];
			}else{
				escolhidos[i] = posicao_nivel[i];
			}
		}
		
		
		// Fim da Inclusão de distância de Multidão para a seleção dos indivíduos 
		
		for (int q=0; q<qtde; q++){
			
			int [] tarefaPos = new int[numMaquinas];
			int [] auxTarefaPos = new int[numMaquinas];
			pos = escolhidos[q];
			//Guardando o número de Tarefa de cada indivíduo e de cada máquina
			for (int i=0; i<numMaquinas; i++){
				for (int j=0; j<numTarefas; j++){				
					if (seq_pop[pos][i][j]==-2){
						tarefaPos[i] = j;
						auxTarefaPos[i] = j;
						break;
					}
				}
			}	
			//criando as estruturas auxiliares
			int [] ind = new int[numTarefas];		
			int []auxind = new int[numTarefas];
					
			//Colocando o valor -1 como marcador da tarefa
			for (int i=0; i<numTarefas; i++){
				auxind[i]=-2;			
			}
			
			//Copiando os valores para as estruturas criadas
			int maquinaInd = 0;
			int contInd = 0;		
			for (int i=0; i<numTarefas; i++){			
				if (seq_pop[pos][maquinaInd][contInd] == -2){
					maquinaInd++;
					contInd=0;
					ind[i] = seq_pop[pos][maquinaInd][contInd];
					auxind[i] = seq_pop[pos][maquinaInd][contInd];		
					contInd++;
				}else{
					ind[i] = seq_pop[pos][maquinaInd][contInd];		
					auxind[i] = seq_pop[pos][maquinaInd][contInd];
					contInd++;
				}						
			}
			
			int numAvaliacoes = 0;
			while (numAvaliacoes<1000){
				if (((seq_pop[pos][0][0] == 0)&&(seq_pop[pos][0][1] == 0))||((seq_pop[pos][0][1] == 0)&&(seq_pop[pos][0][2] == 0))) {
					System.out.println("Sequencia Inválida Busca Local");
					break;
				}
			
				//Gerar uma permutação aleatória das posições das tarefas
				if (numAvaliacoes == 1000){
					break;
				}
				List<Integer> posicaoTarefas = new ArrayList<Integer>();
				List<Integer> posicaoEncaixe = new ArrayList<Integer>();
		
				for (int i = 0; i < numTarefas; i++) { //Sequencia da mega sena
				    posicaoTarefas.add(i);
				    posicaoEncaixe.add(i);
				}
				//Embaralhamos os números:
				Collections.shuffle(posicaoTarefas);		
				//Embaralhamos os números:
				Collections.shuffle(posicaoEncaixe);
				
				CalculoMakespan makespan = new CalculoMakespan();
				CalculoCusto custo = new CalculoCusto();
				float makesanAntes = makespan.calculoMakespanSequencia(seq_pop[pos], tarefa, matrizTarefaMaquina, numMaquinas, matrizSetup);			
				float custoAntes = custo.calculoCustoSequencia(seq_pop[pos], tarefa, matrizTarefaMaquina, numMaquinas, maquina);
				numAvaliacoes++;
				//Efetuando a troca nas posições estabelecidas pelas sequencias aleatórias
		
				//Efetuando a troca nas posições estabelecidas pelas sequencias aleatórias
				boolean melhorou = false;
				for (int i=0; i<numTarefas; i++) {//Controle a tarefa em uma posição
					for (int j=0; j<numTarefas; j++) { //Controla a posição de encaixe desta tarefa				
						//Retirando a tarefa selecionada da sua posição de origem e movimentando o vetor
						if (numAvaliacoes == 1000){
							break;
						}
						if (posicaoEncaixe.get(j) != posicaoTarefas.get(i)){
							for (int k=posicaoTarefas.get(i); k<numTarefas; k++) {
								if (k<numTarefas-1) {
									auxind[k] = ind[k+1];
								}else {
									auxind[k] = -2;
								}					
							}				
							//Inserindo a tarefa selecionada da sua posição de definida e movimentando o vetor
							for (int k=numTarefas-1; k>posicaoEncaixe.get(j); k--) {
								auxind[k] = auxind[k-1];
								if (k == (posicaoEncaixe.get(j)+1)) {
									auxind[posicaoEncaixe.get(j)] = ind[posicaoTarefas.get(i)]; 
								}					
							}
							//Ajustando o número de tarefas nas máquinas
							int nTar = 0;
							boolean tarefaAdicionada = false;
							boolean tarefaRetirada = false;
							for (int k=0; k<numMaquinas; k++){
								nTar = nTar + tarefaPos[k];
								if ((posicaoTarefas.get(i)/nTar == 0)&&(tarefaRetirada == false)){
									auxTarefaPos[k]--; //Retirando a tarefa da máquina 
									tarefaRetirada = true;
								}
								if ((posicaoEncaixe.get(i)/nTar == 0)&&(tarefaAdicionada == false)){
									auxTarefaPos[k]++;
									tarefaAdicionada = true;
								}
								if (tarefaAdicionada && tarefaRetirada){
									break;
								}
							}
							
							//Remontando a lista para o cálculo do Makespan e Custo
							//Remontando as listas de acordo com a quantidade de tarefa de cada máquina	
							contInd = 0;
							maquinaInd = 0;
							int [][] seqAux = new int[numMaquinas][numTarefas];
							//Inicializando o vetor com valor -2
							for (int k=0; k<numMaquinas; k++) {
								for (int w=0; w<numTarefas; w++) {
									seqAux[k][w] = -2;
								}
							}
							
							for (int k=0; k<numTarefas; k++){			
								if (contInd == tarefaPos[maquinaInd]){
									maquinaInd++;
									contInd=0;
									seqAux[maquinaInd][contInd] = auxind[k];				
									contInd++;
								}else{
									seqAux[maquinaInd][contInd] = auxind[k];				
									contInd++;
								}	
							}	
							//Calcular Makespan e Custo Novamente
							float makesanApos = makespan.calculoMakespanSequencia(seqAux, tarefa, matrizTarefaMaquina, numMaquinas, matrizSetup);			
							float custoApos = custo.calculoCustoSequencia(seqAux, tarefa, matrizTarefaMaquina, numMaquinas, maquina);
							numAvaliacoes++;
							if ((makesanApos<makesanAntes)||(((makesanApos == makesanAntes) &&(custoApos<custoAntes)))) {
								//Paleativo
								int numTarefasValidas = 0;
								for (int k=0; k<numTarefas; k++){
									if (auxind[k] != -2){
										numTarefasValidas++;
									}
								}
								//Paleativo
								if (numTarefasValidas == numTarefas){
									//Verificar se seqAux é uma sequencia válida
									boolean solucaoValida = operadores.solucaoValida(seqAux, numTarefas);
									if (solucaoValida) {
										seq_pop[pos] = seqAux; //ALTERAR AQUI DEPOIS
										melhorou = true;
										System.out.println("Melhorou Busca Local");
										break;
									}else {
										System.out.println("Não Melhorou Busca Local - 1");
										for (int k=0; k<numTarefas; k++){
											auxind[k] = ind[k];
										}	
										for (int k=0; k<numMaquinas; k++){
											auxTarefaPos[k] = tarefaPos[k];
										}	
									}
									//Verificar se seqAux é uma sequencia válida																		
								}else{
									System.out.println("Não Melhorou Busca Local - 1");
									for (int k=0; k<numTarefas; k++){
										auxind[k] = ind[k];
									}	
									for (int k=0; k<numMaquinas; k++){
										auxTarefaPos[k] = tarefaPos[k];
									}									
								}
							}else{
								System.out.println("Não Melhorou Busca Local - 3");
								for (int k=0; k<numTarefas; k++){
									auxind[k] = ind[k];
								}
								for (int k=0; k<numMaquinas; k++){
									auxTarefaPos[k] = tarefaPos[k];
								}
							}
						}
						if(numAvaliacoes == 1000) {							
							break;
						}
						if (melhorou){
							numAvaliacoes=0;
						}
					}	
					if(numAvaliacoes == 1000) {							
						break;
					}
				}
			}
		}
		return seq_pop;
	}
}
