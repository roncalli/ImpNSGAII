package testes;

import auxiliares.Hipervolume;

public class TesteHiper {

	public static void main (String args[]) {
		float [] nadir = new float[2];	
		nadir[0] = 74;
		nadir[1] = 31702;
		System.out.println("Teste");
		Hipervolume hiper = new Hipervolume();
		float valor = hiper.calculoHipervolume(nadir);
		System.out.println("Valor do Hipervolume: "+valor);	
	}


}