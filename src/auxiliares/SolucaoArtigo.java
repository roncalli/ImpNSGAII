package auxiliares;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import controle.LeiaCSV;

public class SolucaoArtigo {
	
	
	public static void main (String [] args) throws IOException{
		System.out.println("Teste");
		float matrizSetup[][][] = new float[4][70][70];
		SolucaoArtigo aux = new SolucaoArtigo();
		String data = "Data261218_";
		String nomeArquivo = "D:/FELIPE/RESULTADOS/BASE5/"+data+"MelhorSolucao.txt";
		FileWriter arquivoSaida = new FileWriter(nomeArquivo);	
		float[][] seq = aux.lerArquivoSolucoes(70, matrizSetup);
		for (int w=0; w<4; w++){
			for (int i=0; i<70; i++) {
				if (seq[i][0] == -2){
					break;
				}			
			arquivoSaida.append(seq[i][0]+","+matrizSetup[w][(int) seq[i][0]][(int) seq[i+1][0]]);			
			System.out.println("Makespan: "+seq[i][0]+" Custo: "+seq[i][1]);
			}
		}
		arquivoSaida.close();
		System.out.println("FIM");
	}
	
	public void popularSetup(float matrizSetup[][][]) {
		String arquivoSetup = "D:/FELIPE/BASE5/TabelaSetupMaquina";
		BufferedReader br = null;
		String linha = "";
		String csvDivisor = ",";
		for (int w = 0; w < 4; w++) {
			arquivoSetup = "D:/FELIPE/BASE5/TabelaSetupMaquinaIOF" + w + ".csv";
			try {
				br = new BufferedReader(new FileReader(arquivoSetup));
				int i = 0;
				while ((linha = br.readLine()) != null) {
					String[] objeto = linha.split(csvDivisor);
					for (int j = 0; j < objeto.length; j++) {
						if (i != j) {
							matrizSetup[w][i][j] = Float.parseFloat(objeto[j]);
						} else {
							matrizSetup[w][i][j] = 0;
						}
					}
					i++;
				}
	
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
		}
	}
	
	
	public float[][] lerArquivoSolucoes(int num, float[][][] matrizSetup){
		float auxSeq[][] = new float[num][2];
		String ArquivoFinal = "D:/FELIPE/RESULTADOS/BASE5/Base5MelhorSolucao.csv";		
		popularSetup(matrizSetup);
		for (int i=0; i<num; i++){
			auxSeq[i][0] = -2;
			auxSeq[i][1] = -2;
		}
		BufferedReader br = null;
		String linha = "";
		String csvDivisor = ",";
		try {
			br = new BufferedReader(new FileReader(ArquivoFinal));
			int i = 0;
			while ((linha = br.readLine()) != null) {
				String[] objeto = linha.split(csvDivisor);				
				for (int j=0; j<objeto.length; j++) {
					auxSeq[i][j] = Float.parseFloat(objeto[j]);
				}				
				i++;
			}
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
	return auxSeq;	
	}

}
