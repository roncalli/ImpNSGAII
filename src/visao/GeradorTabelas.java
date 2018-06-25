package visao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.omg.PortableServer.POAPackage.WrongAdapter;

public class GeradorTabelas {

	public static void main(String[] args) throws FileNotFoundException {
		Gerar();
	}

	private static void Gerar() throws FileNotFoundException {
		int numTarefas = 70;
		int numMaquinas = 4;
		// TABELA TAREFA
		// MATRIZ SETUP - 1 Matriz para cada máquina
		String acertoSemLavagem = "D:/Mestrado UFMG/Carrano/Otimização Redes/TrabalhoFinal/acertoSemLavagem70.csv";
		BufferedReader semLavagem = null;
		String acertoComLavagem = "D:/Mestrado UFMG/Carrano/Otimização Redes/TrabalhoFinal/acertoComLavagemMaquina70.csv";
		BufferedReader comLavagem = null;
		String coresTarefas= "D:/Mestrado UFMG/Carrano/Otimização Redes/TrabalhoFinal/cores.csv";
		BufferedReader cores = null;
		semLavagem = new BufferedReader(new FileReader(acertoSemLavagem));
		comLavagem = new BufferedReader(new FileReader(acertoComLavagem));
		cores = new BufferedReader(new FileReader(coresTarefas));
		String csvDivisor = ",";
		for (int w = 0; w < numMaquinas; w++) {
			try {
				String nomeArquivo = "D:/FELIPE/TabelaSetupMaquinaIOF" + w + ".csv";
				FileWriter writer = new FileWriter(nomeArquivo);
				// Preencher Matriz Tarefa Maquina
				// Peso tarefa maquina variando de 1 a 10
				// Data de entrega 80 (2 semanas)
				String[] linhaSemLavagem = (semLavagem.readLine()).split(csvDivisor);
				String[] linhaComLavagem = (comLavagem.readLine()).split(csvDivisor);				
				for (int i = 0; i < numTarefas; i++) {
					int j=0;
					if (i>0){
						writer.append("\n");
					}
					cores = new BufferedReader(new FileReader(coresTarefas));
					//String[] linhaCores = (cores.readLine()).split(csvDivisor);
					while (j<numTarefas){					
						if (j != numTarefas - 1) {
							String[] linhaCores = (cores.readLine()).split(csvDivisor);
							if ((Integer.parseInt(linhaCores[0]) == 4)){
								writer.append((linhaSemLavagem[j]) + ",");
								j++;
								writer.append((linhaComLavagem[j]) + ",");
								j++;
							}else if ((Integer.parseInt(linhaCores[0]) == 1)){
								if (w%2 == 0){
									writer.append((linhaSemLavagem[j]) + ",");
									j++;
								}else{
									writer.append((linhaComLavagem[j]) + ",");
									j++;
								}
							}							
							if ((Integer.parseInt(linhaCores[1]) == 4)){
								writer.append((linhaSemLavagem[j]) + ",");
								j++;
								writer.append((linhaComLavagem[j]) + ",");
								j++;
							}else if ((Integer.parseInt(linhaCores[1]) == 1)){
								if (w%2 == 0){
									writer.append((linhaSemLavagem[j]) + ",");
									j++;
								}else{
									writer.append((linhaComLavagem[j]) + ",");
									j++;
								}
							}							
						} else {
							writer.append((linhaSemLavagem[j]) + "\n");
							j++;
						}
					}
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("TERMINOU");
	}
}