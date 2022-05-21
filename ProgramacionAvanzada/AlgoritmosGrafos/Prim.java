package algoritmos;

import java.util.ArrayList;
import java.util.List;

public class Prim {
	
	private Grafo grafo;
	private Grafo arbol;

	private List<Integer> solucion = new ArrayList<Integer>();
	private List<Integer> porVisitar = new ArrayList<Integer>();;
	
	private int costo = 0;
	private int nodoInicio = 1;

	public Prim(Grafo grafo) {
		this.grafo = grafo;
		this.arbol = new Grafo(grafo.getGrado());
		
		for(int i = 1; i <= grafo.getGrado(); i++) {
			this.porVisitar.add(i);
		}
	}
	
	public void resolver() {
		solucion.add(nodoInicio);
		porVisitar.remove(Integer.valueOf(nodoInicio));
		int w = buscarMenor();
		
		//Si me quedan nodos por visitar, pero la busqueda me devolvio 0, 
		//es que tengo un grafo no conexo
		while(! porVisitar.isEmpty() || w == 0) {
			solucion.add(w);
			porVisitar.remove(Integer.valueOf(w));
			w = buscarMenor();
		}
	}
	
	private int buscarMenor() {
		int menor = grafo.INFINITO;
		int fila = 0;
		int columna = 0;
		
		for(Integer nodo1 : solucion) {
			for(Integer nodo2 : porVisitar) {
				int aux = grafo.getValorArista(nodo1, nodo2);
				if(menor > aux ) {
					menor = aux;
					fila = nodo1;
					columna = nodo2;
				}
			}
		}

		if(menor != grafo.INFINITO) {
			costo += menor;
			arbol.setValorArista(fila, columna, menor);
		}
		
		return columna;
	}
}
