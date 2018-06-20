package controle;

import java.util.Arrays;
import java.util.HashMap;

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
	
	public int[] operadorTorneio1(int numIndividuos, int[] nivelDominancia) {
		int[] vet_pais = new int[numIndividuos];
		int x=0;
		HashMap<String,String> possui= new HashMap<String,String>();
		
		while(x<numIndividuos) {
			
			float k = (float)0.75; // Porcentagem de escolher o melhor
			float r = (float)Math.random();
			int op1 = (int) (Math.floor(Math.random()*100)); // Escolha aleatória dois individuos para o torneio
			int op2 = (int) (Math.floor(Math.random()*100)); // Escolha aleatória dois individuos para o torneio			
			while (op1 == op2) { // Garantir que op1 seja o mesmo individuo de op2
				op2 = (int) (Math.floor(Math.random()*100));
			}
			//evita geracao de itens que ja foram gerados
			while(possui.containsKey(op1+"")) {
				op1 = (int) (Math.floor(Math.random()*100));
			}
			
			//evita geracao de itens que ja foram gerados			
			while(possui.containsKey(op2+"")) {
				op2 = (int) (Math.floor(Math.random()*100));
			}
			
			if (r<k) { //Escolhe o melhor
				if (nivelDominancia[op1]<nivelDominancia[op2]) {
					vet_pais[x] = op1;
					possui.put(op1+"", op1+"");
					x++;
				}else {
					vet_pais[x] = op2;
					possui.put(op2+"", op2+"");
					x++;
				}
			}else { //Escolhe o pior
				if (nivelDominancia[op1]>nivelDominancia[op2]) {
					vet_pais[x] = op1;
					possui.put(op1+"", op1+"");
					x++;
				}else {
					vet_pais[x] = op2;
					possui.put(op2+"", op2+"");
					x++;
				}
			}
			
			
		}
		return vet_pais;
	}
	
	
	public int[][][] operadorCruzamento (int numTarefas, int numIndividuos, int[] resTorneio, int[][][] seq_pop){
		boolean sequenciaValida = validarSequencia(seq_pop, numIndividuos, numTarefas);
		int n_corte = (int) Math.floor(Math.random()*6)+5; //Números de regiões de corte
		int[] v_corte = new int[n_corte]; //Vetor dos pontos de corte
		for (int i=0; i<n_corte; i++) {
			v_corte[i] = (int) Math.floor(Math.random()*99);
		}
		Arrays.sort(v_corte);
		int[][][] seq_pop_f = new int[numIndividuos][1][numTarefas];
		seq_pop_f = seq_pop;
		//Cruzamento dos indivíduos selecionados
		int cont = (int)Math.floor(numIndividuos/2);
		for(int j=0; j<cont; j++) {
			int flag = 0;
			int cont_tarefa = 0; //Contador de Tarefa
			int num_corte = 0;
			//Realização do cruzamento com n pontos de corte
			int op1 = (int) (Math.floor(Math.random()*100)); // Escolha aleatória dois individuos para o cruzamento									
			int pai = resTorneio[op1];
			while(cont_tarefa <numTarefas-1) {
				if(flag == 0) {
					int aux = seq_pop[pai][0][cont_tarefa];
					seq_pop_f[pai][0][cont_tarefa] = seq_pop[pai][0][cont_tarefa+1];
					seq_pop_f[pai][0][cont_tarefa+1] = aux;
				}else {
					int aux = seq_pop[pai][0][cont_tarefa];
					seq_pop_f[pai][0][cont_tarefa] = seq_pop[pai][0][cont_tarefa+1];
					seq_pop_f[pai][0][cont_tarefa+1] = aux;
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
		sequenciaValida = validarSequencia(seq_pop_f, numIndividuos, numTarefas);
		return seq_pop_f;
	}
	
	public int [][][] operadorMutacao(int numIndividuos, int numMaquinas, int numTarefas, int qtdMut, int varMur, int[][][]seq_pop_f){
		//Luiz
		double randomVarMu=Math.floor(Math.random()*varMur);			
		double relacaoPesoQtdMuttarefasMaquina= (qtdMut*(Double.parseDouble(numMaquinas+"")/numTarefas));				
		int n_mut = ((int) (randomVarMu*relacaoPesoQtdMuttarefasMaquina));//Número de Genes Mutáveis
		//Luiz
		int pm = (int)(Math.floor(qtdMut*numIndividuos)/100); //Percentual de Mutação
		//int n_mut = (int) (Math.floor(Math.random()*varMur)+qtdMut); //Número de Genes Mutáveis
		boolean sequenciaValida = validarSequencia(seq_pop_f, numIndividuos, numTarefas);
		//Selecionar individuos para mutação		
		for (int i=0; i<pm; i++) {
			int filho = (int) Math.floor(Math.random()*numIndividuos);
			for (int j=0; j<n_mut; j++) {
				int ind_Pop_Mut_t1 = (int)Math.floor(Math.random()*numTarefas);
				int ind_Pop_Mut_t2 = (int)Math.floor(Math.random()*numTarefas);
				int aux = seq_pop_f[filho][0][ind_Pop_Mut_t1];
				seq_pop_f[filho][0][ind_Pop_Mut_t1] = seq_pop_f[filho][0][ind_Pop_Mut_t2];
				seq_pop_f[filho][0][ind_Pop_Mut_t2] = aux;
			}
		}
		sequenciaValida = validarSequencia(seq_pop_f, numIndividuos, numTarefas);
		return seq_pop_f;
	}
	
	public float [][] calculoDistanciaMultidao(long [] makespan, int[] posicao_nivel) {
		float [][] distMultidao = new float[makespan.length][2];			
		//Ordenando Makespan			
		for (int i = 0; i<makespan.length; i++) {
			long aux = -1;
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
		
		distMultidao[0][0] = -1;
		distMultidao[distMultidao.length-1][0] = -1;
		for (int i=0; i<posicao_nivel.length;i++){
			distMultidao[i][1] = posicao_nivel[i];
		}		
		
		return distMultidao;
	}
	
	public boolean verificaSolucoesIguais(long[] makespan_pai_filho, int posicao, int[][][] seq_pop, int numTarefas){
		for (int i=0; i<posicao; i++){
			if(i!=posicao&&(makespan_pai_filho[i] == makespan_pai_filho[posicao])){
//				for (int j=0; j<numTarefas; j++) {
//					if (seq_pop[i][0][j] != seq_pop[posicao][0][j]) {
//						return false;
//					}
//				}
				return true;
			}
		}		
		return false;
	}
	
	public boolean validarSequencia(int [][][] seq_pop, int numIndividuos, int numTarefas) {
		for (int i=0; i<numIndividuos; i++) {
			int cont = 0;
			for (int j=0; j<numTarefas; j++) {
				if (seq_pop[i][0][j] == i) {
					cont++;
				}
				if (cont>1) {
					System.out.println("Sequencia Inválida!");
					return false;
				}
			}
		}
		//System.out.println("Sequencia Válida!");
		return true;
	}

}
