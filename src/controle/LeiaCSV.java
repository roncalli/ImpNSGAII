package controle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import modelo.Maquina;
import modelo.Tarefa;

public class LeiaCSV {

	public static void main(String[] args) {

		LeiaCSV obj = new LeiaCSV();
		// obj.popularTabelas();

	}

	public void popularTabelas(Tarefa[] tarefas, Maquina[] maquinas, float[][] matrizTarefaMaquina, float matrizSetup[][][], int numMaquinas) {

		String arquivoTarefa = "D:/Mestrado UFMG/Carrano/Otimização Redes/TrabalhoFinal/Tarefa.csv";		
		String arquivoMaquina = "D:/Mestrado UFMG/Carrano/Otimização Redes/TrabalhoFinal/temposMaquina70IOF.csv";
		String arquivoSetup = "D:/FELIPE/TabelaSetupMaquina";
		BufferedReader br = null;
		String linha = "";
		String csvDivisor = ",";
		try {

			br = new BufferedReader(new FileReader(arquivoTarefa));
			int i = 0;
			while ((linha = br.readLine()) != null) {
				String[] objeto = linha.split(csvDivisor);
				tarefas[i] = new Tarefa(Integer.valueOf(objeto[objeto.length - 3]), Integer.valueOf(objeto[objeto.length - 2]), Float.valueOf(objeto[objeto.length - 1]), true);
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
		// LENDO O ARQUIVO DE MAQUINAS
		try {

			br = new BufferedReader(new FileReader(arquivoMaquina));
			for (int i = 0; i < maquinas.length; i++) {
				maquinas[i] = new Maquina(i);
				//ALTERAR DEPOIS 
				maquinas[i].setCusto(1);
			}
			int i = 0;
			while ((linha = br.readLine()) != null) {
				String[] objeto = linha.split(csvDivisor);
				for (int j = 0; j < objeto.length; j++) {
					matrizTarefaMaquina[i][j] = Float.valueOf(objeto[j]);
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
		// Lendo os Arquivos de SETUP
		for (int w = 0; w < numMaquinas; w++) {
			arquivoSetup = "D:/FELIPE/TabelaSetupMaquinaAUX" + w + ".csv";
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
	
	public int retornaNumTarefas(){
		
		
		
		return 0;
	}
}