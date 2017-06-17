package grafos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grafo {
	private MatrizSimetrica grafo;
	
	public static void main(String[] args) {
		Grafo grf = new Grafo();
		MatrizSimetrica mat = grf.generadorAleatorioPorcentaje(10, 50);
		mat.printMatriz();
	}

	public Grafo() { 
	}
	
	public MatrizSimetrica generadorAleatorioProbabilidad(int orden, double probabilidad) {
		grafo = new MatrizSimetrica(orden);

		for(int i = 1; i < orden; i++) {
			for(int j = i; j <= orden; j++) {

				double rand = Math.random();
				if (rand <= probabilidad ) {
					grafo.setValorArista(i, j, 1);
				}
			}
		}
		return grafo;
	}
	
	public MatrizSimetrica generadorAleatorioPorcentaje(int orden, double porcentaje) {

		List<int[]> lista = new ArrayList<int[]>();
		
		for(int i = 1; i < orden; i++) {
			for(int j = i; j <= orden; j++) {
				lista.add(new int[] {i,j});
			}
		}
		
		Random rnm = new Random();
		
		grafo = new MatrizSimetrica(orden);
		int tamaIni = lista.size();
		
		for(int i = 1; i <= tamaIni * porcentaje / 100; i++ ) {

			int randomIdx = rnm.nextInt(lista.size());
			int[] coord = lista.remove( randomIdx );
			grafo.setValorArista(coord[0], coord[1], 1);
		}
		
		return grafo;
	}
}
