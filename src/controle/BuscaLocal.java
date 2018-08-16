package controle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Maquina;
import modelo.Tarefa;

public class BuscaLocal {
	public int [][][] buscaLocalFirstImprovements(int [][][] seq_pop, int numMaquinas, Maquina[] maquina, Tarefa[] tarefa, float[][] matrizTarefaMaquina, float[][][] matrizSetup, int numIndividuos, int numTarefas){
		//Implementar utilizando First Improvements utilizando a id�ia do Carrano, gerando lista aleat�rias de posi��es e de encaixe das tarefas
		int pos = (int)(Math.random()*numIndividuos);
		boolean posNaoValido = true;
		while (posNaoValido) {
			if (seq_pop[pos][0][0] == 0 && (seq_pop[pos][0][1] == 0)) {
				pos = (int)(Math.random()*numIndividuos);
			}else {
				posNaoValido = false;
			}
		}
		int [] tarefaPos = new int[numMaquinas];
		//Guardando o n�mero de Tarefa de cada indiv�duo e de cada m�quina
		for (int i=0; i<numMaquinas; i++){
			for (int j=0; j<numTarefas; j++){				
				if (seq_pop[pos][i][j]==-2){
					tarefaPos[i] = j;
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
		
		//Gerar uma permuta��o aleat�ria das posi��es das tarefas

		List<Integer> posicaoTarefas = new ArrayList<Integer>();
		for (int i = 0; i < numTarefas; i++) { //Sequencia da mega sena
		    posicaoTarefas.add(i);
		}
		//Embaralhamos os n�meros:
		Collections.shuffle(posicaoTarefas);		
		
		//Gerar uma permuta��o aleat�ria das posi��es das posi��es de encaixe das tarefas
		List<Integer> posicaoEncaixe = new ArrayList<Integer>();
		for (int i = 0; i < numTarefas; i++) { //Sequencia da mega sena
		    posicaoEncaixe.add(i);
		}
		//Embaralhamos os n�meros:
		Collections.shuffle(posicaoEncaixe);
		
		CalculoMakespan makespan = new CalculoMakespan();
		CalculoCusto custo = new CalculoCusto();
		float makesanAntes = makespan.calculoMakespanSequencia(seq_pop[pos], tarefa, matrizTarefaMaquina, numMaquinas, matrizSetup);			
		float custoAntes = custo.calculoCustoSequencia(seq_pop[pos], tarefa, matrizTarefaMaquina, numMaquinas, maquina);
		
		//Efetuando a troca nas posi��es estabelecidas pelas sequencias aleat�rias

		//Efetuando a troca nas posi��es estabelecidas pelas sequencias aleat�rias
		boolean melhorou = false;
		for (int i=0; i<numTarefas; i++) {//Controle a tarefa em uma posi��o
			for (int j=0; j<numTarefas; j++) { //Controla a posi��o de encaixe desta tarefa				
				//Retirando a tarefa selecionada da sua posi��o de origem e movimentando o vetor
				for (int k=posicaoTarefas.get(i); k<numTarefas; k++) {
					if (k<numTarefas-1) {
						auxind[k] = ind[k+1];
					}else {
						auxind[k] = -2;
					}					
				}				
				//Inserindo a tarefa selecionada da sua posi��o de definida e movimentando o vetor
				for (int k=numTarefas-1; k>posicaoEncaixe.get(j); k--) {
					auxind[k] = auxind[k-1];
					if (k == (posicaoEncaixe.get(j)+1)) {
						auxind[posicaoEncaixe.get(j)] = ind[posicaoTarefas.get(i)]; 
					}					
				}					
				//Remontando a lista para o c�lculo do Makespan e Custo
				//Remontando as listas de acordo com a quantidade de tarefa de cada m�quina	
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
				if ((makesanApos<makesanAntes)||(((makesanApos == makesanAntes) &&(custoApos<custoAntes)))) {
					System.out.println("Melhorou Busca Local");
					seq_pop[pos] = seqAux;
					melhorou = true;
					break;
				}else{
					System.out.println("N�o Melhorou Busca Local");
				}				
			}
			if(melhorou) {
				break;
			}
		}
		
		
		return seq_pop;
	}
}
