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

	}

	public void popularSequenciaInicial(int[][][] seq_pop, int numTarefas){
		String arquivoSequencia = "D:/FELIPE/test1.csv";
		//String arquivoSequencia = "D:/FELIPE/test2.csv";
		int numIdividuos = seq_pop.length;
		BufferedReader br = null;
		String linha = "";
		String csvDivisor = ",";
		try {

			br = new BufferedReader(new FileReader(arquivoSequencia));
			int j = 0;
			while ((linha = br.readLine()) != null) {
				String[] objeto = linha.split(csvDivisor);
				for (int w=0; w<numTarefas; w++){
					seq_pop[j][0][w] = Integer.parseInt(objeto[w])-1; 
				}			
				j++;
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
	
	public void popularTabelas(Tarefa[] tarefas, Maquina[] maquinas, int[][] matrizTarefaMaquina, int numMaquinas) {
		String arquivoTarefaMaquina = "D:/FELIPE/sch100k1.csv";		
		//String arquivoTarefaMaquina = "D:/FELIPE/sch200k1.csv";
		BufferedReader br = null;
		String linha = "";
		String csvDivisor = ",";
		try {

			br = new BufferedReader(new FileReader(arquivoTarefaMaquina));
			int i = 0;
			while ((linha = br.readLine()) != null) {
				String[] objeto = linha.split(csvDivisor);
				tarefas[i] = new Tarefa(i,Integer.valueOf(objeto[objeto.length - 2]), Integer.valueOf(objeto[objeto.length - 1]));
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

			br = new BufferedReader(new FileReader(arquivoTarefaMaquina));
			for (int i = 0; i < maquinas.length; i++) {
				maquinas[i] = new Maquina(i);
				//ALTERAR DEPOIS 
				maquinas[i].setCusto(1);
			}
			int i = 0;
			while ((linha = br.readLine()) != null) {
				String[] objeto = linha.split(csvDivisor);
				for (int j = 0; j < maquinas.length; j++) {
					matrizTarefaMaquina[i][j] = Integer.valueOf(objeto[j]);
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
//		for (int w = 0; w < numMaquinas; w++) {
//			arquivoSetup = "D:/FELIPE/TabelaSetupMaquina" + w + ".csv";
//			try {
//				br = new BufferedReader(new FileReader(arquivoSetup));
//				int i = 0;
//				while ((linha = br.readLine()) != null) {
//					String[] objeto = linha.split(csvDivisor);
//					for (int j = 0; j < objeto.length; j++) {
//						if (i != j) {
//							matrizSetup[w][i][j] = Float.valueOf(objeto[j]);
//						} else {
//							matrizSetup[w][i][j] = 0;
//						}
//					}
//					i++;
//				}
//
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (br != null) {
//					try {
//						br.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
	}
}