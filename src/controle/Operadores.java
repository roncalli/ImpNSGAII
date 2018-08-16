package controle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	
	//OPERADOR CRUZAMENTO
	
	public int[][][] operadorCruzamento(int numTarefas, int numIndividuos, int numMaquinas, int[] resTorneio, int[][][] seq_pop){		
		
		int[][][] seq_pop_f = new int[numIndividuos][numMaquinas][numTarefas];
		int [] tarefasMaquinaPai1 = new int [numMaquinas];			
		int [] tarefasMaquinaPai2 = new int [numMaquinas];
		
		//Copiando os indivíduos de seq_pop para seq_pop_f
		for (int i=0; i<numIndividuos; i++){
			for (int j=0; j<numMaquinas; j++){
				for (int k=0; k<numTarefas; k++){
					seq_pop_f[i][j][k] = seq_pop[i][j][k];
				}
			}
		}
		//Utilizando o vencedor do torneio binário				
		int pai1 = (int)(Math.random()*numIndividuos);
		int pai2 = (int)(Math.random()*numIndividuos);
		boolean posNaoValido = true;
		while (posNaoValido) {
			if (seq_pop[pai1][0][0] == 0 && (seq_pop[pai1][0][1] == 0)) {
				pai1 = (int)(Math.random()*numIndividuos);
			}else if (seq_pop[pai2][0][0] == 0 && (seq_pop[pai2][0][1] == 0)) {
				pai2 = (int)(Math.random()*numIndividuos);
			}else{
				posNaoValido = false;
			}
		}
		if (pai1 == pai2){
			if (pai2>0){
				pai2--;
			}else{
				pai2++;
			}
		}
		
		//Guardando o número de Tarefa de cada indivíduo e de cada máquina
		for (int i=0; i<numMaquinas; i++){
			for (int j=0; j<numTarefas; j++){				
				if (seq_pop[pai1][i][j]==-2){
					tarefasMaquinaPai1[i] = j;
					break;
				}
			}
		}	
		for (int i=0; i<numMaquinas; i++){
			for (int j=0; j<numTarefas; j++){				
				if (seq_pop[pai2][i][j]==-2){
					tarefasMaquinaPai2[i] = j;
					break;
				}
			}
		}	
		
		//criando as estruturas auxiliares
		int [] ind1 = new int[numTarefas];
		int [] ind2 = new int[numTarefas];
		int []auxind1 = new int[numTarefas];
		int []auxind2 = new int[numTarefas];
		
		//Colocando o valor -1 como marcador da tarefa
		for (int i=0; i<numTarefas; i++){
			auxind1[i]=-1;
			auxind2[i]=-1;
		}
		
		//Copiando os valores para as estruturas criadas
		int maquinaP1 = 0;
		int cont1 = 0;
		int maquinaP2 = 0;
		int cont2=0;
		for (int i=0; i<numTarefas; i++){			
			if (seq_pop[pai1][maquinaP1][cont1] == -2){
				maquinaP1++;
				cont1=0;
				ind1[i] = seq_pop[pai1][maquinaP1][cont1];				
				cont1++;
			}else{
				ind1[i] = seq_pop[pai1][maquinaP1][cont1];				
				cont1++;
			}
			if (seq_pop[pai2][maquinaP2][cont2] == -2){
				maquinaP2++;
				cont2=0;
				ind2[i] = seq_pop[pai2][maquinaP2][cont2];				
				cont2++;
			}else{
				ind2[i] = seq_pop[pai2][maquinaP2][cont2];				
				cont2++;
			}					
		}
		
		//Definindo 2 pontos de Corte 
		int corte1 = (int) (Math.random()*numTarefas);
		int corte2 = (int) (Math.random()*numTarefas);
		
		if (corte1>corte2){//Ordenando
			int aux = corte1;
			corte1 = corte2;
			corte2 = aux;
		}
		
		//Efetuando o cruzamento
		for (int i=corte1; i<=corte2; i++){//Efetuando a troca do bloco entre os pontos de corte
			auxind1[i] = ind2[i];
			auxind2[i] = ind1[i];			
		}
		
		
		//PELEATIVO
		boolean solucaoCorreta = true;
		int cont0 = 0;
		for (int i=0; i<numTarefas; i++) {
			if (ind1[i] == 0) {
				cont0++;
			}
			if (ind2[i] == 0) {
				cont0++;
			}
		}
		
		if (cont0>2) {
			solucaoCorreta = false;
		}
		
		if (solucaoCorreta == true) { // PALEATIVO
		
			//Efetuando a adequacao baseado no PMX
			for (int i=0; i<numTarefas; i++){
				if (i<corte1 || i>corte2){ //Fora da área de corte				
					int tarefaParaAdicionarInd1 = retornaTarefaPMX(auxind1, ind1, ind1[i], numTarefas);
					if (tarefaParaAdicionarInd1 == -2) {
						return seq_pop_f;
					}
					auxind1[i] = tarefaParaAdicionarInd1;
					int tarefaParaAdicionarInd2 = retornaTarefaPMX(auxind2, ind2, ind2[i], numTarefas);
					if (tarefaParaAdicionarInd2 == -2) {
						return seq_pop_f;
					}
					auxind2[i] = tarefaParaAdicionarInd2;
				}			
			}
			
			//Remontando as listas de acordo com a quantidade de tarefa de cada máquina	
			cont1 = 0;
			maquinaP1 = 0;
			cont2 = 0;
			maquinaP2 = 0;
						
			for (int i=0; i<numTarefas; i++){			
				if (cont1 == tarefasMaquinaPai1[maquinaP1]){
					maquinaP1++;
					cont1=0;
					seq_pop_f[pai1][maquinaP1][cont1] = auxind1[i];				
					cont1++;
				}else{
					seq_pop_f[pai1][maquinaP1][cont1] = auxind1[i];				
					cont1++;
				}
				if (cont2 == tarefasMaquinaPai2[maquinaP2]){
					maquinaP2++;
					cont2=0;
					seq_pop_f[pai2][maquinaP2][cont2] = auxind2[i];				
					cont2++;
				}else{
					seq_pop_f[pai2][maquinaP2][cont2] = auxind2[i];				
					cont2++;
				}					
			}			
		}
		return seq_pop_f;
	}
	
	public int retornaTarefaPMX(int[] auxindP, int[] indS, int tarefa, int numTarefas){		
		boolean encontrou = true;
		int cont = 0;
		while (encontrou == true){
			int indiceTarefa = tarefaJaIncluida(auxindP, tarefa, numTarefas);
			if (indiceTarefa == -1){ //Tarefa não incluída
				encontrou = false;
				break;
			}else{ // Procurar qual tarefa incluir 
				tarefa = indS[indiceTarefa];				
			}
			//VERIFICAR ESSE PONTO
			if (cont == 500) {
				return -2;
				
			}
			cont++;
		}		
		return tarefa;
	}
	
	public int tarefaJaIncluida(int[] ind, int tarefa, int numTarefas){
		for (int i=0; i<numTarefas; i++){
			if (ind[i] == tarefa){
				return i;
			}
		}
		return -1;
	}
	
//OPERADOR SLIDE
	public int[][] operadorMutSlide(int numTarefas, int numMaquinas,int[][] seq_pop){
		//Vetor com a quantidade de tarefas alocadas em cada máquina
		int [] tarefasMaquina = new int [numMaquinas];
		for (int i=0; i<numMaquinas; i++){
			for (int j=0; j<numTarefas; j++){				
				if (seq_pop[i][j]==-2){
					tarefasMaquina[i] = j;
					break;
				}
			}
		}		
		int maquinaSelecionada = (int) (Math.random()*numMaquinas); // Seleciona a máquina
		int posicaoTarefaMaquina = (int) (Math.random()*tarefasMaquina[maquinaSelecionada]); // Seleciona a posição da Tarefa na lista de execução da máquina
		int tarefaSelecionada =  (int) (Math.random()*numTarefas); // Seleciona a tarefa na lista de execução da máquina
		
		//Encontrando a Tarefa selecionada
		int maquinaTarefaSelecionada = 0;
		int posicaoTarefaSelecionada = 0;
		for (int i=0; i<numMaquinas; i++){
			for (int j=0; j<numTarefas; j++){				
				if (seq_pop[i][j]==tarefaSelecionada){
					maquinaTarefaSelecionada = i;
					posicaoTarefaSelecionada = j;
					break;
				}
			}
		}		
		System.out.println("");
		//Movimentando as tarefas da máquina onde a tarefa foi retirada
		for (int i=posicaoTarefaSelecionada; i<numTarefas; i++){			
			seq_pop[maquinaTarefaSelecionada][i] = seq_pop[maquinaTarefaSelecionada][i+1];			
			if (seq_pop[maquinaTarefaSelecionada][i+1] == -2){
				tarefasMaquina[maquinaTarefaSelecionada]--;
				break;
			}
			//REVISAR PARA RETIRAR
			if (i==numTarefas-2){
				break;
			}
			//REVISAR PARA RETIRAR
		}
		
		//Inserindo a tarefa na máquina selecionada
		for (int i = tarefasMaquina[maquinaSelecionada]-1; i>=posicaoTarefaMaquina-1; i--){
			if (i == posicaoTarefaMaquina-1){
				seq_pop[maquinaSelecionada][posicaoTarefaMaquina] = tarefaSelecionada;
				break;
			}
			seq_pop[maquinaSelecionada][i+1] = seq_pop[maquinaSelecionada][i];						
		}
		
		return seq_pop;
	}
	
//OPERADOR TROCA
	
	public int[][] operadorMutTroca(int numTarefas, int numMaquinas,int[][] seq_pop){
		//Vetor com a quantidade de tarefas alocadas em cada máquina
		int [] tarefasMaquina = new int [numMaquinas];
		for (int i=0; i<numMaquinas; i++){
			for (int j=0; j<numTarefas; j++){				
				if (seq_pop[i][j]==-2){
					tarefasMaquina[i] = j;
					break;
				}
			}
		}		
		int primeiraMaquinaSelecionada = (int) (Math.random()*numMaquinas); // Seleciona a máquina
		int primeiraTarefaSelecionada = (int) (Math.random()*tarefasMaquina[primeiraMaquinaSelecionada]); // Seleciona a posição da Tarefa na lista de execução da máquina
		int segundaMaquinaSelecionada = (int) (Math.random()*numMaquinas);// Seleciona a máquina
		int segundaTarefaSelecionada = (int) (Math.random()*tarefasMaquina[segundaMaquinaSelecionada]);// Seleciona a posição da Tarefa na lista de execução da máquina
		
		//Verificando se as tarefas são distintas - (Garante que as tarefas serão distintas)
		if (primeiraMaquinaSelecionada == segundaMaquinaSelecionada && primeiraTarefaSelecionada == segundaTarefaSelecionada){
			if (segundaTarefaSelecionada >0){
				segundaTarefaSelecionada--;
			}else{
				segundaTarefaSelecionada++;
			}
		}
		
		//Efetuando de fato a troca das tarefas 
		int aux = seq_pop[primeiraMaquinaSelecionada][primeiraTarefaSelecionada];
		seq_pop[primeiraMaquinaSelecionada][primeiraTarefaSelecionada] = seq_pop[segundaMaquinaSelecionada][segundaTarefaSelecionada];
		seq_pop[segundaMaquinaSelecionada][segundaTarefaSelecionada] = aux;
		//Troca Efetuada
	
		return seq_pop;
	}
	
	
	//OPERADOR DE MUTAÇÃO
	public int [][][] operadorMutacao(int numIndividuos, int numMaquinas, int numTarefas, int qtdMut, int[][][]seq_pop){		
		int[][][] seq_pop_f = new int[numIndividuos][numMaquinas][numTarefas];

		//Copiando os indivíduos de seq_pop para seq_pop_f
		for (int i=0; i<numIndividuos; i++){
			for (int j=0; j<numMaquinas; j++){
				for (int k=0; k<numTarefas; k++){
					seq_pop_f[i][j][k] = seq_pop[i][j][k];
				}
			}
		}
		
		//Gera a lista de indivíduos de forma aleatória
		List<Integer> tarefas = new ArrayList<Integer>();
		for (int i = 0; i < numTarefas; i++) { //Sequencia da mega sena
		    tarefas.add(i);
		}
		//Embaralhamos os números:
		Collections.shuffle(tarefas);
		for (int i=0; i<qtdMut; i++){						
			int individuo = (int) tarefas.get(i); //Seleciona o indivíduo que sofrerá a mutação
			int sorteio = (int) (Math.random()*100);												
			if (sorteio>=50){//Escolhe-se o operador de troca para mutação
				operadorMutTroca(numTarefas, numMaquinas, seq_pop_f[individuo]);
			}else{//Escolhe-se o operador de slide para mutação																						
				operadorMutSlide(numTarefas, numMaquinas, seq_pop_f[individuo]);			
			}
		}
		
		return seq_pop_f;
	}
	
	
	
	public float [][] calculoDistanciaMultidao(float [] makespan, float[] custo, int numFobj, int[] posicao_nivel) {
		float [][] distMultidao = new float[makespan.length][2];
		for (int w = 0; w<numFobj; w++){
			if (w == 0){
				//Ordenando Makespan			
				for (int i = 0; i<makespan.length; i++) {
					float aux = -1;
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
			}else if (w == 1){
				//Ordenando Custo
				for (int i = 0; i<custo.length; i++) {
					float aux = -1;
					int auxPos = -1;
					for (int j=i; j<custo.length; j++) {
						if (custo[j]<custo[i]) {
							aux = custo[i];
							auxPos = posicao_nivel[i];
							custo[i] = custo[j];
							posicao_nivel[i] = posicao_nivel[j];
							custo[j] = aux;
							posicao_nivel[j] = auxPos;
						}
					}
				}
				for(int j=1; j<custo.length-1; j++) {				
					if (custo[j+1]!=custo[j-1]) {
						distMultidao[j][0] = distMultidao[j][0] + (custo[j+1] - custo[j-1]);
					}
				}
			}
		}
		
		
		distMultidao[0][0] = -1;
		distMultidao[distMultidao.length-1][0] = -1;		
		for (int i=0; i<posicao_nivel.length;i++){
			distMultidao[i][1] = posicao_nivel[i];
		}		
		return distMultidao;
	}
	
	public boolean verificaSolucoesIguais(float[] makespan_pai_filho, float[] custo_pai_filho, int posicao){
		for (int i=0; i<posicao; i++){
			if((makespan_pai_filho[i] == makespan_pai_filho[posicao])&&(custo_pai_filho[i]==custo_pai_filho[posicao])){
				return true;
			}
		}		
		return false;
	}

}