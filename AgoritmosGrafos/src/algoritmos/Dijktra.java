package algoritmos;

import java.util.ArrayList;
import java.util.List;

public class Dijktra {
	
	private Grafo grafo;
	private int[] distancias;
	private int[] recorrido;
	private boolean[] visto;
	
	private int nodoInicial;

	public Dijktra(Grafo grafo) {
		this.grafo = grafo;
		this.distancias = new int[grafo.getGrado()];
		this.recorrido = new int[grafo.getGrado()];
		this.visto = new boolean[grafo.getGrado()];
	}
	
	public void calcular(int nodoInicial) {
		this.nodoInicial = nodoInicial;
		
		// Meto a todos en el vector distancia
		for(int i = 0; i < grafo.getGrado(); i++) {
			distancias[i] = grafo.getValorArista(nodoInicial, i);
			visto[i] = false;
			
			// Marco a cuales puedo llegar desde el nodo inicial
			if( distancias[i] != Grafo.INFINITO) {
				recorrido[i] = nodoInicial;
			}
		}
		
		// Pongo mi nodo inicial como visto
		visto[nodoInicial] = true;
		
		// Busco el menor
		int w = getMenor();
		
		// Itero mientras que tenga un valor para W
		while( w != grafo.INFINITO) {
			
			// Marco W como visto
			visto[w] = true;
			
			for(int i = 0; i < grafo.getGrado(); i++) {
				// Me fijo entre los que todavia no recorri si pasar por W es mas barato
				if( ! visto[i] && distancias[i] > grafo.getValorArista(w, i) ) {
					distancias[i] = grafo.getValorArista(w, i);
					recorrido[i] = w;
				}
			}
			
			w = getMenor();
		}
	}
	
	public int getDistancia(int nodoFinal) {
		return distancias[nodoFinal];
	}
	
//	public String printRecorrido(int nodoFinal) {
//		String aux;
//		
//		if(distancias[nodoFinal] != Grafo.INFINITO) {
//			return "No existe recorrido desde " + nodoInicial + " hasta " + nodoFinal;
//		}
//			
//		
//		return aux;
//	}
	
	private int getMenor() {
		int nodo = Grafo.INFINITO;
		
		for(int i = 0; i < grafo.getGrado(); i++) {
			if( ! visto[i] && distancias[i] < nodo) {
				nodo = distancias[i];
			}
		}
		
		return nodo;
	}
}
