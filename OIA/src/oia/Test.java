package oia;

import java.io.File;

public class Test {
	public static void main(String[] args) {
//		String archivo = "PALIN";
		String archivo = "SECMAX";
		
		File entrada = new File("c:\\Users\\mariano.e.gambacorta\\Documents\\Estudio\\Programacion\\IN\\" + archivo + ".in");
		File salida = new File("c:\\Users\\mariano.e.gambacorta\\Documents\\Estudio\\Programacion\\OUT\\" + archivo + ".out");
		
//		EjercicioOIA ejercicio = new Palindromos(entrada, salida);
		EjercicioOIA ejercicio = new SecuenciasMaximas(entrada, salida);
		
		ejercicio.resolver();
	}
}
