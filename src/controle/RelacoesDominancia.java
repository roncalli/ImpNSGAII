package controle;

public class RelacoesDominancia {

	public int verificaDominancia (float []sol1, float[]sol2) {		
		if ((sol2[0]>=sol1[0]) && (sol2[1]>=sol1[1])){ // Solução 1 domina a solução 2
		//if (sol2[0]>=sol1[0]){
			return 1;
		}
		if ((sol1[0]>=sol2[0]) && (sol1[1]>=sol2[1])){ // Solução 2 domina a solução 1
		//if (sol1[0]>=sol2[0]){
			return 2;
		}else {
			return 0;
		}
	}
	
	
	public int [] calculaNivelDominancia (int numIndividuos, float[] makespan, float[] custo) {
		int [] nivelDominancia = new int [numIndividuos];
		int [] solucoes = new int[numIndividuos];//0 individuo ainda não visitado, 1 individuo já visitado
		int k = 0; //Nível de dominância
		int contador = numIndividuos;
		while(contador > 0) {
			k++; //Incrementando o nível de dominância
			int nfk = 0; //Número de individuos no nível k
			for (int i=0; i<numIndividuos; i++) {
				int flag = 0; //Determina se o indivíduo é dominado (0 = não dominado, 1 = dominado)
				if (solucoes[i] == 0) { //Indivíduo ainda não visitado
					for (int j=0; j<numIndividuos; j++) {
						if (solucoes[j] == 0) {
							if(i!=j) {//Verificando que não estamos comparando os mesmos individuos
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
					if (flag == 0) {//Solução não dominada no nível k
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
