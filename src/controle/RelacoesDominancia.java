package controle;

public class RelacoesDominancia {

	public int verificaDominancia (float []sol1, float[]sol2) {		
		if (((sol1[0]<sol2[0]) && (sol1[1]<=sol2[1]))||(sol1[0]<=sol2[0]) && (sol1[1]<sol2[1])){ // Solu��o 1 domina a solu��o 2		
			return 1;
		}
		if (((sol2[0]<sol1[0]) && (sol2[1]<=sol1[1]))||(sol2[0]<=sol1[0]) && (sol2[1]<sol1[1])){ // Solu��o 2 domina a solu��o 1		
			return 2;
		}else {
			return 0;
		}
	}
	
	
	public int [] calculaNivelDominancia (int numIndividuos, float[] makespan, float[] custo) {
		int [] nivelDominancia = new int [numIndividuos];
		int [] solucoes = new int[numIndividuos];//0 individuo ainda n�o visitado, 1 individuo j� visitado
		int k = 0; //N�vel de domin�ncia
		int contador = numIndividuos;
		while(contador > 0) {
			k++; //Incrementando o n�vel de domin�ncia
			int nfk = 0; //N�mero de individuos no n�vel k
			for (int i=0; i<numIndividuos; i++) {
				int flag = 0; //Determina se o indiv�duo � dominado (0 = n�o dominado, 1 = dominado)
				if (solucoes[i] == 0) { //Indiv�duo ainda n�o visitado
					for (int j=0; j<numIndividuos; j++) {
						if (solucoes[j] == 0) {
							if(i!=j) {//Verificando que n�o estamos comparando os mesmos individuos
								float [] sol1 = new float[2];
								float [] sol2 = new float[2];
								sol1[0] = (float) makespan[i];
								sol1[1] =  custo[i];
								sol2[0] = (float) makespan[j];
								sol2[1] =  custo[j];								
								if (verificaDominancia(sol1, sol2) == 2) {
									flag = 1;
									break;
								}
							}
						}
					}
					if (flag == 0) {//Solu��o n�o dominada no n�vel k
						nfk++;
						nivelDominancia[i] = k;
						solucoes[i] = 1;
					}
				}
			}
			contador = contador-nfk;			
		}
		return nivelDominancia;
	}
	
	public int[] retornarIndividuosMaisDominados (int numIndividuos, int [] nivelDominancia, int percentual) {		
		int maxDom = -1;
		int qtde = (int)(((numIndividuos*percentual)/numIndividuos));
		int [] retorno = new int[qtde];
		for (int i=0; i<numIndividuos; i++) {
			if (nivelDominancia[i] > maxDom) {
				maxDom = nivelDominancia[i];
			}			
		}
		int cont = 0;
		for (int i=0; i<2*numIndividuos; i++) {
			if (nivelDominancia[i] == maxDom) {
				retorno[cont] = i;
				cont++;
			}
			if (cont == qtde) {
				break;				
			}
			if ((i == (2*numIndividuos-1))&&(cont<qtde)) {
				i = 0;
				maxDom--;
			}
		}
		return retorno;
	}
}
