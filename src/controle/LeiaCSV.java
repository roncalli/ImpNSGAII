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
	
	public void gerarCsvSolucao(int numIndividuos, int ger, float[] makespan, float[] custo, long tempoInicial, int[] nivelDominancia) throws IOException {
		String exec = "2";
		String rodada = "_"+exec+".csv";
		String data = "Data021018";
		String nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/Resultado"+rodada; 
		try {
			File file = new File(nomeArquivo);
			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		FileWriter arquivoSaída = new FileWriter(nomeArquivo);	
		Impressaoes impressaoes = new Impressaoes();
		nivelDominancia = impressaoes.imprimirNaoDominados(makespan, custo, numIndividuos);		
		for (int i=0; i<numIndividuos; i++) {
			if (nivelDominancia[i] != -2) {
				arquivoSaída.append(makespan[nivelDominancia[i]]+","+custo[nivelDominancia[i]]+"\n");
			}
		}		
		arquivoSaída.flush();
		arquivoSaída.close();
		
		nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/ResultadoTempo"+rodada;
		arquivoSaída = new FileWriter(nomeArquivo);	
		arquivoSaída.append("Tempo Total: "+((System.currentTimeMillis()-tempoInicial)/1000)+" segundos");
		arquivoSaída.flush();
		arquivoSaída.close();
	}
	
	public void gerarCsvSolucaoResultados(int numIndividuos, int ger, float[] makespan, float[] custo, long tempoInicial, int[] nivelDominancia) throws IOException {
		String exec = "2";
		String rodada = "_"+exec+".csv";
		String data = "Data021018";
		String nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/Resultado"+ger+rodada; 
		try {
			File file = new File(nomeArquivo);
			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		FileWriter arquivoSaída = new FileWriter(nomeArquivo);	
		Impressaoes impressaoes = new Impressaoes();
		nivelDominancia = impressaoes.imprimirNaoDominados(makespan, custo, numIndividuos);		
		for (int i=0; i<numIndividuos; i++) {
			if (nivelDominancia[i] != -2) {
				arquivoSaída.append(makespan[nivelDominancia[i]]+","+custo[nivelDominancia[i]]+"\n");
			}
		}		
		arquivoSaída.flush();
		arquivoSaída.close();
		
		nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/ResultadoTempo"+ger+rodada;
		arquivoSaída = new FileWriter(nomeArquivo);	
		arquivoSaída.append("Tempo Total: "+((System.currentTimeMillis()-tempoInicial)/1000)+" segundos");
		arquivoSaída.flush();
		arquivoSaída.close();
	}
	
	public void gerarMelhorPiorFOBJ(int ger, float melhorMakespan, float melhorCusto, float piorCusto, float piorMakespan) throws IOException{
		
		String exec = "2";
		String rodada = "_"+exec+".csv";
		String data = "Data021018";
		
		String nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/MelhorPior(Makespan-Custo)"+ger+rodada;
		FileWriter arquivoSaída = new FileWriter(nomeArquivo);	
		arquivoSaída = new FileWriter(nomeArquivo);	
		
		arquivoSaída.append("Melhor Makespan: "+melhorMakespan+"\n");
		arquivoSaída.append("Pior Makespan: "+piorMakespan+"\n");
		arquivoSaída.append("Melhor Custo: "+melhorCusto+"\n");
		arquivoSaída.append("Pior Custo: "+piorCusto+"\n");
		arquivoSaída.flush();
		arquivoSaída.close();
	}
	
	public void gerarCsvSequenciaSolucao(int numIndividuos, int numMaquina, int numTarefas, int ger, float[] makespan, float[] custo, long tempoInicial, int[] nivelDominancia, int[][][]seq_pop) throws IOException {
		String exec = "2";
		String rodada = "_"+exec+".csv";
		String data = "Data021018";
		String nomeArquivo = "D:/FELIPE/RESULTADOS/"+data+"/ARQUIVOFINAL/Exec"+exec+"/ArqFinal"+rodada; 		
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(nomeArquivo));
			File file = new File(nomeArquivo);
			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		FileWriter arquivoSaída = new FileWriter(nomeArquivo);	
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
							arquivoSaída.append(seq_pop[nivelDominancia[i]][j][k]+"");
						}else {
							arquivoSaída.append(seq_pop[nivelDominancia[i]][j][k]+",");
						}
					}else{
						break;
					}
				}	
				arquivoSaída.append("\n");
			}
		}
		arquivoSaída.flush();
		arquivoSaída.close();		
	}
	
	public int[][][] lerArquivoSolucoes(int numIndividuos, int numMaquinas, int numTarefas, int ger){
		int auxSeq[][][] = new int[numIndividuos][numMaquinas][numTarefas];
		String exec = "2";
		String rodada = "_"+exec+".csv";
		String data = "Data021018";
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