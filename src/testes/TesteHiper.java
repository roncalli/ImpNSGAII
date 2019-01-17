package testes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import visao.Impressaoes;
import auxiliares.Hipervolume;

public class TesteHiper {

	public static void main (String args[]) throws IOException {
		float [] nadir = new float[2];	
		nadir[0] = 50;
		nadir[1] = 31000;
		String data = "BASE5/Data261218_";
		Hipervolume hiper = new Hipervolume();
		float[][] valor = hiper.calculoHipervolume(nadir,data);
		String nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"Hipervolume.txt";
		FileWriter arquivoSaida = new FileWriter(nomeArquivo);
		for (int i=0; i<6; i++){
			for (int j=0; j<30; j++){
				System.out.println("Valor do Hipervolume: Rodada "+i+" Execução: "+j+": "+valor[i][j]);	
				arquivoSaida.append("Valor do Hipervolume: Rodada "+i+" Execução: "+j+": "+valor[i][j]+"\n");
			}
		}
		arquivoSaida.close();
	}
}
