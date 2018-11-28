package auxiliares;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Paretao {
	
	public static void main (String [] args) throws IOException{
		System.out.println("Teste");
		int num = 100;
		Paretao aux = new Paretao();
		String data = "Data061118_";
		String nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"Paretao.txt";
		FileWriter arquivoSaida = new FileWriter(nomeArquivo);
		for (int w=0; w<6; w++){
			for (int j=0; j<30; j++){
				float[][] seq = aux.lerArquivoSolucoes((j+1), 100, data+(w));
				for (int i=0; i<num; i++){
					if (seq[i][0] == -2){
						break;
					}
					arquivoSaida.append(seq[i][0]+","+seq[i][1]+"\n");
					System.out.println("Makespan: "+seq[i][0]+" Custo: "+seq[i][1]);
				}
			}
		}
		arquivoSaida.close();
	}

	public float[][] lerArquivoSolucoes(int exec, int num, String data){
		float auxSeq[][] = new float[num][2];
		String rodada = "_"+exec+".csv";
		String ArquivoFinal = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/Resultado20000"+rodada; 
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
