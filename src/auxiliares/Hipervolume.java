package auxiliares;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Hipervolume {
	
	
		public float[][] lerCsvHipervolume(){		
		String arquivoTarefa = "D:/FELIPE/RESULTADOS/Data021018_5/ARQUIVOFINAL/Exec1/Resultado10000_1.csv";
		BufferedReader br = null;
		String linha = "";
		String csvDivisor = ",";
		float[][] valorHipervolume = null;
		try {
			BufferedReader brAux = null;
			brAux = new BufferedReader(new FileReader(arquivoTarefa));
			br = new BufferedReader(new FileReader(arquivoTarefa));
			int i = 0;
			float [][] hipervolume = new float[(int)brAux.lines().count()][2];			
			while ((linha = br.readLine()) != null) {
				String[] objeto = linha.split(csvDivisor);
				hipervolume[i][0] = Float.valueOf(objeto[objeto.length - 2]);
				hipervolume[i][1] = Float.valueOf(objeto[objeto.length - 1]);						
				i++;
			}
			valorHipervolume = hipervolume;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return valorHipervolume;
	}
	
	public float calculoHipervolume(float[] nadir){
		float [][] hipervolume = lerCsvHipervolume();
		float menorCusto = 100000;
		float menorMakespan = 100000;
		for (int i=0; i<hipervolume.length; i++){
			if (hipervolume[i][0]<menorMakespan) {
				menorMakespan = hipervolume[i][0];
			}
			if (hipervolume[i][1]<menorCusto) {
				menorCusto = hipervolume[i][1];
			}								
		}
		
		//Ordenando as solu��es para a realiza��o dos c�lculos
		float auxM = 0;
		float auxC = 0;
		for(int linha = 0; linha < hipervolume.length; linha++) {
			for(int coluna = 0; coluna < hipervolume[linha].length; coluna++) {
				int col = coluna;
				for(int i = linha; i < hipervolume.length; i++) {		
					if (  hipervolume[linha][0] > hipervolume[i][0] ){
						auxM = hipervolume[linha][0];
						auxC = hipervolume[linha][1];
						hipervolume[linha][0] = hipervolume[i][0];
						hipervolume[linha][1] = hipervolume[i][1];
						hipervolume[i][0] = auxM;
						hipervolume[i][1] = auxC;
					}
				}
			}
		}
		
		
		
		
		for (int i=0; i<hipervolume.length; i++){
			hipervolume[i][0] = (hipervolume[i][0]-menorMakespan)/(nadir[0]-menorMakespan); 
			hipervolume[i][1] = (hipervolume[i][1]-menorCusto)/(nadir[1] - menorCusto);					
		}
	
		//Fim da Normaliza��o
		
		float ca = 1;
		float ma = 0;
		float calcHipervolume = 0;
		for (int i=0; i<hipervolume.length; i++){
			ca = 1-hipervolume[i][1];
			if (i<hipervolume.length-1) {
				ma = hipervolume[i+1][0] - hipervolume[i][0];
			}else {
				ma = 1 - hipervolume[i][0];
			}
			calcHipervolume = calcHipervolume + ca*ma;
		}
		return calcHipervolume;
	}
}
