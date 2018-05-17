package visao;

import java.io.FileWriter;
import java.io.IOException;

public class GeradorTabelas {

	public static void main(String[] args) {
		Gerar();
	}

	private static void Gerar() {
		int numTarefas = 20;
		int numMaquinas = 5;
		// TABELA TAREFA
		try {
			FileWriter writer = new FileWriter("D:/FELIPE/TabelaTarefa.csv");
			// Preencher Matriz Tarefa Maquina
			// IdTarefa, PesoTarefa, DataEntrega
			// Peso tarefa variando de 1 a 10
			// Data de entrega 80 (2 semanas)
			int vMax = 10;
			int vMin = 1;
			int dataEntrega = 40;
			for (int i = 0; i < numTarefas; i++) {
				writer.append(i + "," + (int) ((Math.floor(Math.random() * (vMax - vMin)) + vMin)) + "," + dataEntrega);
				writer.append('\n');
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TABELA TAREFA MAQUINA
		try {
			FileWriter writer = new FileWriter("D:/FELIPE/TabelaTarefaMaquina.csv");
			// Preencher Matriz Tarefa Maquina
			// Peso tarefa maquina variando de 1 a 10
			// Data de entrega 80 (2 semanas)
			int vMax = 20;
			int vMin = 5;
			for (int i = 0; i < numTarefas; i++) {
				for (int j = 0; j < numMaquinas; j++) {
					if (j != numMaquinas - 1) {
						writer.append((int) ((Math.floor(Math.random() * (vMax - vMin)) + vMin)) + ",");
					} else {
						writer.append((int) ((Math.floor(Math.random() * (vMax - vMin)) + vMin)) + "\n");
					}
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// MATRIZ SETUP - 1 Matriz para cada máquina
		// TABELA TAREFA MAQUINA

		for (int w = 0; w < numMaquinas; w++) {
			try {
				String nomeArquivo = "D:/FELIPE/TabelaSetupMaquina" + w + ".csv";
				FileWriter writer = new FileWriter(nomeArquivo);
				// Preencher Matriz Tarefa Maquina
				// Peso tarefa maquina variando de 1 a 10
				// Data de entrega 80 (2 semanas)
				int vMax = 0;
				int vMin = 0;
				for (int i = 0; i < numTarefas; i++) {
					for (int j = 0; j < numTarefas; j++) {
						if (i == j) {
							if (j == numTarefas - 1) {
								writer.append("0");
							} else {
								writer.append(0 + ",");
							}
						} else if (j != numTarefas - 1) {
							writer.append((int) ((Math.floor(Math.random() * (vMax - vMin)) + vMin)) + ",");
						} else {
							writer.append((int) ((Math.floor(Math.random() * (vMax - vMin)) + vMin)) + "\n");
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