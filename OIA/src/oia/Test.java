package oia;

import java.io.File;

public class Test {
	public static void main(String[] args) {
		File entrada = new File("c:\\Users\\mariano.e.gambacorta\\Documents\\Estudio\\Programacion\\IN\\PALIN.in");
		File salida = new File("c:\\Users\\mariano.e.gambacorta\\Documents\\Estudio\\Programacion\\OUT\\PALIN.out");
		
		EjercicioOIA ejercicio = new Palindromos(entrada, salida);
		
		ejercicio.resolver();
	}
}
