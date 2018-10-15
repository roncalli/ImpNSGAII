package controle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.print.attribute.standard.NumberOfInterveningJobs;

import modelo.Maquina;
import modelo.Tarefa;
import visao.Impressaoes;

public class LeiaCSV {

	public static void main(String[] args) {

		LeiaCSV obj = new LeiaCSV();
		// obj.popularTabelas();

	}

	public void popularTabelas(Tarefa[] tarefas, Maquina[] maquinas, float[][] matrizTarefaMaquina, float matrizSetup[][][], int numMaquinas) {

		String arquivoTarefa = "D:/FELIPE/FINAL/Tarefa.csv";	
		String arquivoValorMaquina = "D:/FELIPE/FINAL/ValorMaquina.csv"; 
		String arquivoMaquina = "D:/FELIPE/FINAL/temposMaquina70IOF.csv";
		//String arquivoMaquina = "D:/FELIPE/TESTE/TabelaTarefaMaquina.csv";
		//String arquivoMaquina = "D:/FELIPE/TESTE/temposMaquina70IOF.csv";
		String arquivoSetup = "D:/FELIPE/FINAL/TabelaSetupMaquina";
		BufferedReader br = null;
		String linha = "";
		String csvDivisor = ",";
		try {

			br = new BufferedReader(new FileReader(arquivoTarefa));
			int i = 0;
			while ((linha = br.readLine()) != null) {
				String[] objeto = linha.split(csvDivisor);
				tarefas[i] = new Tarefa(Integer.valueOf(objeto[objeto.length - 3]), Integer.valueOf(objeto[objeto.length - 2]), Float.valueOf(objeto[objeto.length - 1]), false);
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
			BufferedReader brAux = null;
			brAux = new BufferedReader(new FileReader(arquivoValorMaquina));
			br = new BufferedReader(new FileReader(arquivoMaquina));			
			linha = brAux.readLine();
			String[] valor = linha.split(csvDivisor);
			for (int i = 0; i < maquinas.length; i++) {
				maquinas[i] = new Maquina(i);
				//ALTERAR DEPOIS 
				maquinas[i].setCusto(Float.parseFloat(valor[i]));
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
			arquivoSetup = "D:/FELIPE/FINAL/TabelaSetupMaquinaIOF" + w + ".csv";
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
	
	public void gerarCsvSolucao(int numIndividuos, int ger, float[] makespan, float[] custo, long tempoInicial, int[] nivelDominancia, int exec) throws IOException {
		String rodada = "_"+exec+".csv";
		String data = "Data021018_5";
		String nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/Resultado"+rodada; 
		try {
			File file = new File(nomeArquivo);
			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		FileWriter arquivoSaida = new FileWriter(nomeArquivo);	
		Impressaoes impressaoes = new Impressaoes();
		nivelDominancia = impressaoes.imprimirNaoDominados(makespan, custo, numIndividuos);		
		for (int i=0; i<numIndividuos; i++) {
			if (nivelDominancia[i] != -2) {
				arquivoSaida.append(makespan[nivelDominancia[i]]+","+custo[nivelDominancia[i]]+"\n");
			}
		}		
		arquivoSaida.flush();
		arquivoSaida.close();
		
		nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/ResultadoTempo"+rodada;
		arquivoSaida = new FileWriter(nomeArquivo);	
		arquivoSaida.append("Tempo Total: "+((System.currentTimeMillis()-tempoInicial)/1000)+" segundos");
		arquivoSaida.flush();
		arquivoSaida.close();
	}
	
	public void gerarCsvSolucaoResultados(int numIndividuos, int ger, float[] makespan, float[] custo, long tempoInicial, int[] nivelDominancia, int exec) throws IOException {
		String rodada = "_"+exec+".csv";
		String data = "Data021018_5";
		String nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/Resultado"+ger+rodada; 
		try {
			File file = new File(nomeArquivo);
			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		FileWriter arquivoSaida = new FileWriter(nomeArquivo);	
		Impressaoes impressaoes = new Impressaoes();
		nivelDominancia = impressaoes.imprimirNaoDominados(makespan, custo, numIndividuos);		
		for (int i=0; i<numIndividuos; i++) {
			if (nivelDominancia[i] != -2) {
				arquivoSaida.append(makespan[nivelDominancia[i]]+","+custo[nivelDominancia[i]]+"\n");
			}
		}		
		arquivoSaida.flush();
		arquivoSaida.close();
		
		nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/ResultadoTempo"+ger+rodada;
		arquivoSaida = new FileWriter(nomeArquivo);	
		arquivoSaida.append("Tempo Total: "+((System.currentTimeMillis()-tempoInicial)/1000)+" segundos");
		arquivoSaida.flush();
		arquivoSaida.close();
	}
	
	public void gerarMelhorPiorFOBJ(int ger, float melhorMakespan, float melhorCusto, float piorCusto, float piorMakespan, int exec) throws IOException{
		String rodada = "_"+exec+".csv";
		String data = "Data021018_5";
		
		String nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/MelhorPior(Makespan-Custo)"+ger+rodada;
		FileWriter arquivoSaida = new FileWriter(nomeArquivo);	
		arquivoSaida = new FileWriter(nomeArquivo);	
		
		arquivoSaida.append("Melhor Makespan: "+melhorMakespan+"\n");
		arquivoSaida.append("Pior Makespan: "+piorMakespan+"\n");
		arquivoSaida.append("Melhor Custo: "+melhorCusto+"\n");
		arquivoSaida.append("Pior Custo: "+piorCusto+"\n");
		arquivoSaida.flush();
		arquivoSaida.close();
	}
	
	public void gerarCsvSequenciaSolucao(int numIndividuos, int numMaquina, int numTarefas, int ger, float[] makespan, float[] custo, long tempoInicial, int[] nivelDominancia, int[][][]seq_pop, int exec) throws IOException {
		String rodada = "_"+exec+".csv";
		String data = "Data021018_5";
		String nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/ArqFinal"+rodada; 		
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(nomeArquivo));
			File file = new File(nomeArquivo);
			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		FileWriter arquivoSaida = new FileWriter(nomeArquivo);	
		Impressaoes impressaoes = new Impressaoes();
		nivelDominancia = impressaoes.imprimirNaoDominados(makespan, custo, numIndividuos);		
		for (int i=0; i<numIndividuos; i++) {
			if(nivelDominancia[i] == -2){
				break;
			}
			for (int j=0; j<numMaquina; j++){
				for (int k=0; k<numTarefas; k++){
					if (seq_pop[nivelDominancia[i]][j][k] != -2) {
						if ((k == (numTarefas-1)) || (seq_pop[nivelDominancia[i]][j][k+1]==-2)) {
							arquivoSaida.append(seq_pop[nivelDominancia[i]][j][k]+"");
						}else {
							arquivoSaida.append(seq_pop[nivelDominancia[i]][j][k]+",");
						}
					}else{
						break;
					}
				}	
				arquivoSaida.append("\n");
			}
		}
		arquivoSaida.flush();
		arquivoSaida.close();		
	}
	
	public int[][][] lerArquivoSolucoes(int numIndividuos, int numMaquinas, int numTarefas, int ger, int exec){
		int auxSeq[][][] = new int[numIndividuos][numMaquinas][numTarefas];
		String rodada = "_"+exec+".csv";
		String data = "Data021018_5";
		String ArquivoFinal = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/ArqFinal"+rodada; 
		for (int i=0; i<numIndividuos; i++){
			for (int j=0; j<numMaquinas; j++){
				for (int k=0; k<numTarefas; k++){
					auxSeq[i][j][k] = -2;
				}
			}
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
					auxSeq[i/numMaquinas][i%numMaquinas][j] = Integer.parseInt(objeto[j]);
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