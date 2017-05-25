package oia;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Palindromos extends EjercicioOIA {

	private String palabra;
	
	Palindromos(File entrada, File salida) {
		super(entrada,salida);
	}
	
	private boolean esPalindromo(String str) {
		if(str.length() < 2) return false;
		
		for(int i = 0, j = str.length() - 1; i < j; i++, j--) {
			if( str.charAt(i) != str.charAt(j) ) return false;
		}
		return true;
	}
	
	private boolean esIPalindromo(String str) {
		return esPalindromo(str.substring(1));
	}
	
	private boolean esDPalindromo(String str) {
		return esPalindromo(str.substring(0,str.length()-1));
	}
	
	private boolean esDistinguida(String str) {		
		return esPalindromo(str) || esIPalindromo(str) || esDPalindromo(str);
	}
	
	@Override
	public void resolver() {
		
		boolean pudo = false;
		
		try {
			Scanner sc = new Scanner(super.entrada);
			FileWriter fw = new FileWriter(super.salida);
			
			palabra = sc.nextLine();

			for(int i = 2; i < palabra.length() - 1; i++) {
				
				if(esDistinguida(palabra.substring(0,i)) && esDistinguida(palabra.substring(i))) {
					fw.write(palabra.substring(0,i));
					if( esPalindromo(palabra.substring(0,i)) ) fw.write(" palindromo");
					if( esIPalindromo(palabra.substring(0,i)) ) fw.write(" i-palindromo");
					if( esDPalindromo(palabra.substring(0,i)) ) fw.write(" d-palindromo");
					fw.write("\n" + palabra.substring(i));
					if( esPalindromo(palabra.substring(i)) ) fw.write(" palindromo");
					if( esIPalindromo(palabra.substring(i)) ) fw.write(" i-palindromo");
					if( esDPalindromo(palabra.substring(i)) ) fw.write(" d-palindromo");
					fw.write("\n");
					pudo = true;
				}
			}
			
			if(!pudo) fw.write("No se pudo");
			sc.close();
			fw.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
