package controle;

import java.util.Arrays;

public class Operadores {
	
	public int[] operadorTorneio(int numIndividuos, int[] nivelDominancia) {
		int[] vet_pais = new int[numIndividuos];
		for (int i=0; i<numIndividuos; i++) {
			float k = (float)0.75; // Porcentagem de escolher o melhor
			float r = (float)Math.random();
			int op1 = (int) (Math.floor(Math.random()*100)); // Escolha aleat�ria dois individuos para o torneio
			int op2 = (int) (Math.floor(Math.random()*100)); // Escolha aleat�ria dois individuos para o torneio			
			while (op1 == op2) { // Garantir que op1 seja o mesmo individuo de op2
				op2 = (int) (Math.floor(Math.random()*100));
			}
			if (r<k) { //Escolhe o melhor
				if (nivelDominancia[op1]<nivelDominancia[op2]) {
					vet_pais[i] = op1;
				}else {
					vet_pais[i] = op2;
				}
			}else { //Escolhe o pior
				if (nivelDominancia[op1]>nivelDominancia[op2]) {
					vet_pais[i] = op1;
				}else {
					vet_pais[i] = op2;
				}
			}
			
		}
		return vet_pais;
	}
	
	
	public int[][] operadorCruzamento (int numTarefas, int numIndividuos, int[] resTorneio, int[][] pop){
		int n_corte = (int) Math.floor(Math.random()*6)+5; //N�meros de regi�es de corte
		int[] v_corte = new int[n_corte]; //Vetor dos pontos de corte
		for (int i=0; i<n_corte; i++) {
			v_corte[i] = (int) Math.floor(Math.random()*99);
		}
		Arrays.sort(v_corte);
		int[][] pop_f = new int[numTarefas][numIndividuos];
		
		//Cruzamento dos indiv�duos selecionados
		int cont = (int)Math.floor(numIndividuos/2);
		for(int j=0; j<cont; j++) {
			int flag = 0;
			int cont_tarefa = 0; //Contador de Tarefa
			int num_corte = 0;
			//Realiza��o do cruzamento com n pontos de corte
			int op1 = (int) (Math.floor(Math.random()*100)); // Escolha aleat�ria dois individuos para o cruzamento
			int op2 = (int) (Math.floor(Math.random()*100)); // Escolha aleat�ria dois individuos para o cruzamento			
			while (op1 == op2) { // Garantir que op1 seja o mesmo individuo de op2
				op2 = (int) (Math.floor(Math.random()*100));
			}
			int pai1 = resTorneio[op1];
			int pai2 = resTorneio[op2];
			while(cont_tarefa <numTarefas) {
				if(flag == 0) {
					pop_f[cont_tarefa][(2*j)] = pop[cont_tarefa][pai1];
					pop_f[cont_tarefa][(2*j+1)] = pop[cont_tarefa][pai2];
				}else {
					pop_f[cont_tarefa][(2*j)] = pop[cont_tarefa][pai2];
					pop_f[cont_tarefa][(2*j+1)] = pop[cont_tarefa][pai1];
				}
				cont_tarefa++;
				if (cont_tarefa <= num_corte) {
					if(cont_tarefa>v_corte[num_corte-1]) {
						num_corte++;
						if (flag == 0) {
							flag = 1;
						}else {
							flag = 0;
						}
					}	
				}else {
					if (flag == 0) {
						flag = 1;
					}else {
						flag = 0;
					}
				}
			}
		}		
		return pop_f;
	}
	
	public int [][] operadorMutacao(int numIndividuos, int numMaquinas, int numTarefas, int qtdMut, int varMur, int[][]pop_f){		
		int pm = (int)(Math.floor(qtdMut*numIndividuos)/100); //Percentual de Muta��o
		int n_mut = (int) (Math.floor(Math.random()*varMur)+qtdMut); //N�mero de Genes Mut�veis
		
		//Selecionar individuos para muta��o		
		for (int i=0; i<pm; i++) {
			int filho = (int) Math.floor(Math.random()*numIndividuos);
			for (int j=0; j<n_mut; j++) {
				int ind_Pop_Mut = (int)Math.floor(Math.random()*numTarefas);
				int alt_Pop_Mut = (int) Math.floor(Math.random()*numMaquinas);
				pop_f[ind_Pop_Mut][filho] = alt_Pop_Mut; 
			}
		}
		return pop_f;
	}
	
	public float [] calculoDistanciaMultidao(int [] makespan, float[] custo, int numFobj) {
		float [] distMultidao = new float[makespan.length];
		//Ordenando Makespan
		for (int i = 0; i<makespan.length; i++) {
			int aux = -1;
			for (int j=i; j<makespan.length; j++) {
				if (makespan[j]<makespan[i]) {
					aux = makespan[i];
					makespan[i] = makespan[j];
					makespan[j] = aux;
				}
			}
		}
		//Ordenando Custo
		for (int i = 0; i<custo.length; i++) {
			float aux = -1;
			for (int j=i; j<custo.length; j++) {
				if (custo[j]<custo[i]) {
					aux = custo[i];
					custo[i] = custo[j];
					custo[j] = aux;
				}
			}
		}		
		for(int j=1; j<makespan.length-1; j++) {				
			if (makespan[j+1]!=makespan[j-1]) {
				distMultidao[j] = distMultidao[j] + (makespan[j+1] - makespan[j-1]);
			}
		}
		for(int j=1; j<custo.length-1; j++) {				
			if (custo[j+1]!=custo[j-1]) {
				distMultidao[j] = distMultidao[j] + (custo[j+1] - custo[j-1]);
			}
		}
		distMultidao[0] = 10000;
		distMultidao[distMultidao.length-1] = 10000;
		return distMultidao;
	}

}
