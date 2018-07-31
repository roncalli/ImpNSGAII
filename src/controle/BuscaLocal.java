package controle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Maquina;
import modelo.Tarefa;

public class BuscaLocal {
	public int [][][] buscaLocal(int [][][] seq_pop, int numMaquinas, Maquina[] maquina, Tarefa[] tarefa, float[][] matrizTarefaMaquina, float[][][] matrizSetup, int individuo, int numIndividuos){
		//Verificar qual máquina possui o maior makespan
		int contNaoMelhora = 0;
		int maquinaMaiorMakespan = -1;
		int maquinaMenorMakespan = -1;
		float maiorMakespan = 0;
		float menorMakespan = 1000000;		
		int [][] seq_pop_aux = new int[numMaquinas][tarefa.length];
		
		
		
		//Copiando a Sequencia
		for (int i=0; i<numMaquinas; i++){
			for (int j=0; j<tarefa.length; j++){
				seq_pop_aux[i][j] = seq_pop[individuo][i][j];
			}
		}
		
		CalculoMakespan calculoMakespan = new CalculoMakespan();
		CalculoCusto calculoCusto = new CalculoCusto();
		//Encontrar a máquina com maior makespan
		for (int i=0; i<numMaquinas; i++) {			
			float makespan = calculoMakespan.calculoMakespanSequencia(seq_pop[individuo], tarefa, matrizTarefaMaquina, i, matrizSetup);
			if (makespan>maiorMakespan) {
				maiorMakespan = makespan;
				maquinaMaiorMakespan = i;
			}
			if (makespan<menorMakespan) {
				menorMakespan = makespan;
				maquinaMenorMakespan = i;
			}
		}
		boolean melhorou = false;
		int contSemMelhora = 0;
		
		while (!melhorou) {
			for (int i=0; i<numMaquinas; i++) {			
				float makespan = calculoMakespan.calculoMakespanSequencia(seq_pop[individuo], tarefa, matrizTarefaMaquina, i, matrizSetup);
				if (makespan>maiorMakespan) {
					maiorMakespan = makespan;
					maquinaMaiorMakespan = i;
				}
				if (makespan<menorMakespan) {
					menorMakespan = makespan;
					maquinaMenorMakespan = i;
				}
			}
			int tarMaqMaior = (int)Math.floor(Math.random()*tarefa.length);
			while (seq_pop_aux[maquinaMaiorMakespan][tarMaqMaior]==-2){
				tarMaqMaior = (int)Math.floor(Math.random()*tarefa.length);
			}					
			//Calculando o Makespan e Custo Antes
			float makespanAntes = calculoMakespan.calculoMakespanSequencia(seq_pop_aux, tarefa, matrizTarefaMaquina, numMaquinas, matrizSetup);
			float custoAntes = calculoCusto.calculoCustoSequencia(seq_pop_aux, tarefa, matrizTarefaMaquina, numMaquinas, maquina);
			//Efetuando a troca
			int posMenor = 0;
			for (int w=0; w<tarefa.length; w++){
				if (seq_pop_aux[maquinaMenorMakespan][w]!=-2){
					posMenor++;
				}
			}
			int posMaior = 0;
			for (int w=0; w<tarefa.length; w++){
				if (seq_pop_aux[maquinaMaiorMakespan][w]!=-2){
					posMaior++;
				}
			}			
			seq_pop_aux[maquinaMenorMakespan][posMenor] = seq_pop_aux[maquinaMaiorMakespan][tarMaqMaior];
			seq_pop_aux[maquinaMaiorMakespan][tarMaqMaior] = seq_pop_aux[maquinaMaiorMakespan][posMaior-1];
			seq_pop_aux[maquinaMaiorMakespan][posMaior-1] = -2;
			//Troca Efetuada
			//Calculando o Makespan e Custo Antes
			float makespanApos = calculoMakespan.calculoMakespanSequencia(seq_pop_aux, tarefa, matrizTarefaMaquina, numMaquinas, matrizSetup);
			float custoApos = calculoCusto.calculoCustoSequencia(seq_pop_aux, tarefa, matrizTarefaMaquina, numMaquinas, maquina);
			if (makespanApos<makespanAntes){//Melhorou
				seq_pop[individuo] = seq_pop_aux;				
				melhorou = true;
				break;
			}else if(custoApos<custoAntes){
				seq_pop[individuo] = seq_pop_aux;				
				melhorou = true;
				break;
			}
			if (melhorou){
				break;
			}else{
				contSemMelhora++;
			}
			if (contSemMelhora == 10){
				break;
			}
		}
		return seq_pop;
	}
}
