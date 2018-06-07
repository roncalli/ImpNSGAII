package controle;

import java.util.Arrays;

public class Operadores {
	
	public int[] operadorTorneio(int numIndividuos, int[] nivelDominancia) {
		int[] vet_pais = new int[numIndividuos];
		for (int i=0; i<numIndividuos; i++) {
			float k = (float)0.75; // Porcentagem de escolher o melhor
			float r = (float)Math.random();
			int op1 = (int) (Math.floor(Math.random()*100)); // Escolha aleatória dois individuos para o torneio
			int op2 = (int) (Math.floor(Math.random()*100)); // Escolha aleatória dois individuos para o torneio			
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
		int n_corte = (int) Math.floor(Math.random()*6)+5; //Números de regiões de corte
		int[] v_corte = new int[n_corte]; //Vetor dos pontos de corte
		for (int i=0; i<n_corte; i++) {
			v_corte[i] = (int) Math.floor(Math.random()*99);
		}
		Arrays.sort(v_corte);
		int[][] pop_f = new int[numTarefas][numIndividuos];
		
		//Cruzamento dos indivíduos selecionados
		int cont = (int)Math.floor(numIndividuos/2);
		for(int j=0; j<cont; j++) {
			int flag = 0;
			int cont_tarefa = 0; //Contador de Tarefa
			int num_corte = 0;
			//Realização do cruzamento com n pontos de corte
			int op1 = (int) (Math.floor(Math.random()*100)); // Escolha aleatória dois individuos para o cruzamento
			int op2 = (int) (Math.floor(Math.random()*100)); // Escolha aleatória dois individuos para o cruzamento			
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
		int pm = (int)(Math.floor(qtdMut*numIndividuos)/100); //Percentual de Mutação
		int n_mut = (int) (Math.floor(Math.random()*varMur)+qtdMut); //Número de Genes Mutáveis
		
		//Selecionar individuos para mutação		
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
	
	public float [][] calculoDistanciaMultidao(int [] makespan, int numFobj, int[] posicao_nivel) {
		float [][] distMultidao = new float[makespan.length][2];
		for (int w = 0; w<numFobj; w++){
			if (w == 0){
				//Ordenando Makespan			
				for (int i = 0; i<makespan.length; i++) {
					int aux = -1;
					int auxPos = -1;
					for (int j=i; j<makespan.length; j++) {
						if (makespan[j]<makespan[i]) {
							aux = makespan[i];
							auxPos = posicao_nivel[i];
							makespan[i] = makespan[j];
							posicao_nivel[i] = posicao_nivel[j];
							makespan[j] = aux;
							posicao_nivel[j] = auxPos;
						}
					}
				}
				for(int j=1; j<makespan.length-1; j++) {				
					if (makespan[j+1]!=makespan[j-1]) {
						distMultidao[j][0] = distMultidao[j][0] + (makespan[j+1] - makespan[j-1]);
					}
				}
//			}else if (w == 1){
//				//Ordenando Custo
//				for (int i = 0; i<custo.length; i++) {
//					float aux = -1;
//					int auxPos = -1;
//					for (int j=i; j<custo.length; j++) {
//						if (custo[j]<custo[i]) {
//							aux = custo[i];
//							auxPos = posicao_nivel[i];
//							custo[i] = custo[j];
//							posicao_nivel[i] = posicao_nivel[j];
//							custo[j] = aux;
//							posicao_nivel[j] = auxPos;
//						}
//					}
//				}
//				for(int j=1; j<custo.length-1; j++) {				
//					if (custo[j+1]!=custo[j-1]) {
//						distMultidao[j][0] = distMultidao[j][0] + (custo[j+1] - custo[j-1]);
//					}
//				}
			}
		}
		
		
		distMultidao[0][0] = -1;
		distMultidao[distMultidao.length-1][0] = -1;
		for (int i=0; i<posicao_nivel.length;i++){
			distMultidao[i][1] = posicao_nivel[i];
		}		
		
		return distMultidao;
	}
	
	public boolean verificaSolucoesIguais(int[] makespan_pai_filho, int posicao){
		for (int i=0; i<posicao; i++){
			if((makespan_pai_filho[i] == makespan_pai_filho[posicao])){
				return true;
			}
		}		
		return false;
	}

}
