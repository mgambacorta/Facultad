package oia;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class SecuenciasMaximas extends EjercicioOIA {
	
	public SecuenciasMaximas(File entrada, File salida) {
		super(entrada, salida);
	}

	@Override
	public void resolver() {
		try {
			Scanner sc = new Scanner(entrada);
			FileWriter fw = new FileWriter(salida);
			int secMaximo = 0;
			int secActual = 0;
			int contador = 0;
			
			sc.nextInt(); //El primer numero no me interesa
			
			while( sc.hasNextInt() ) {
				if( esValido(sc.nextInt()) ) {
					secActual++;
					contador++;
				}
				else {
					if( secActual > secMaximo )
						secMaximo = secActual;
					
					secActual = 0;
				}
			}
			
			fw.write(contador + "\n" + secMaximo);
			
			sc.close();
			fw.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private boolean esValido(int num) {
		return num % 2 != 0 && num % 3 != 0 && num % 5 != 0;
	}
}
