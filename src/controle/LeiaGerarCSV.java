package controle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import modelo.Maquina;
import modelo.Tarefa;

public class LeiaGerarCSV {

	public void popularSequenciaInicial(int[][][] seq_pop, int numTarefas){
		//String arquivoSequencia = "D:/seq_pop_10.csv";
		//String arquivoSequencia = "D:/test1.csv";
		String arquivoSequencia = "D:/test2.csv";
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
					//seq_pop[j][0][w] = Integer.parseInt(objeto[w]);
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
		//String arquivoTarefaMaquina = "D:/teste10.csv";		
		//String arquivoTarefaMaquina = "D:/sch100k1.csv";		
		String arquivoTarefaMaquina = "D:/sch200k1.csv";
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
	}
	
	public void gerarCsvSolucao(int numTarefas, int[] seq_pop) throws IOException {
		String nomeArquivo = "D://MachadoNascimentoCarneiro.csv"; 
		FileWriter arquivoSaída = new FileWriter(nomeArquivo);	
		for (int i=0; i<numTarefas; i++) {
			if (i==numTarefas-1) {
				arquivoSaída.append(Integer.toString(seq_pop[i]));				
			}else {
				arquivoSaída.append(Integer.toString(seq_pop[i]));
				arquivoSaída.append(',');
			}
		}
		arquivoSaída.flush();
		arquivoSaída.close();
	}
	
}