package algoritmos;

import java.util.LinkedList;
import java.util.Queue;

public class Bfs {
	
	private Grafo grafo;
	private int nodoInicial;
	private int[] distancias;
	private Queue<Integer> cola = new LinkedList<Integer>();
	
	public Bfs(Grafo grafo, int nodoInicial) {
		this.grafo = grafo;
		this.nodoInicial = nodoInicial;
		this.distancias = new int[grafo.getGrado() + 1];
		for(int i = 1; i <= grafo.getGrado(); i++) {
			distancias[i] = -1;
		}
	}
	
	public void resolver() {
		int nodo = nodoInicial;
		distancias[nodo] = 0;
		cola.add(nodo);
		
		while( !cola.isEmpty() ) {
			for(int i = 1; i <= grafo.getGrado(); i++) {
				if( distancias[i] == -1 && grafo.getValorArista(nodo, i) != Grafo.INFINITO ) {
					distancias[i] = distancias[nodo] + 1;
					cola.add(i);
				}
			}
			nodo = cola.poll();
		}
	}	
}
