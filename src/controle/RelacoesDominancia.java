package controle;

public class RelacoesDominancia {

	public int verificaDominancia (float sol1, float sol2) {		
		if ((sol1<sol2)){ // Solução 1 domina a solução 2		
			return 1;
		}
		if ((sol2<sol1)){ // Solução 2 domina a solução 1
			return 2;
		}else {
			return 0;
		}
	}
	
	
	public int [] calculaNivelDominancia (int numIndividuos, long[] makespan) {
		int [] nivelDominancia = new int [numIndividuos];
		int [] solucoes = new int[numIndividuos];//0 individuo ainda não visitado, 1 individuo já visitado
		int k = 0; //Nível de dominância
		int contador = numIndividuos;
		while(contador > 0) {
			k++; //Incrementando o nível de dominância
			for (int i=0; i<numIndividuos; i++) {
				int flag = 0; //Determina se o indivíduo é dominado (0 = não dominado, 1 = dominado)
				if (solucoes[i] == 0) { //Indivíduo ainda não visitado
					for (int j=0; j<numIndividuos; j++) {
						if (solucoes[j] == 0) {
							if(i!=j) {//Verificando que não estamos comparando os mesmos individuos
								float sol1 = (float) makespan[i];
								float sol2 = (float) makespan[j];																					
								if (verificaDominancia(sol1, sol2) == 2) {
									flag = 1;
									break;
								}
							}
						}
					}
					if (flag == 0) {//Solução não dominada no nível k
						nivelDominancia[i] = k;
						solucoes[i] = 1;
						break;
					}
				}
			}
		contador--;			
		}
		return nivelDominancia;
	}
	
	public int[] retornarIndividuosMaisDominados (int numIndividuos, int [] nivelDominancia, int percentual) {		
		int maxDom = -1;
		int qtde = (int)(((numIndividuos*percentual)/100));
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
