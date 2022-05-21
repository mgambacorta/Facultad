package algoritmos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Kruskal {
	private Grafo grafo;
	private Grafo arbol;

	private int[] caminos;
	private List<AristaKruskal> porVisitar = new ArrayList<AristaKruskal>();;
	
	private int costo = 0;

	public Kruskal(Grafo grafo) {
		this.grafo = grafo;
		this.arbol = new Grafo(grafo.getGrado());
		
		this.caminos = new int[grafo.getGrado() + 1]; //Cero no lo uso
		for(int i = 1; i <= grafo.getGrado(); i++) {
			this.caminos[i] = i;
		}
	}
	
	public void resolver() {
		
		for(int i = 1; i <= grafo.getGrado(); i++) {
			for(int j = 1; j <= grafo.getGrado(); j++) {
				if(grafo.getValorArista(i, j) != Grafo.INFINITO) {
					porVisitar.add( new AristaKruskal(i, j, grafo.getValorArista(i, j)) );
				}
			}
		}
		
		Collections.sort(porVisitar);
		
		for(AristaKruskal arista : porVisitar) {
			if(find(arista.getNodo1()) != find(arista.getNodo2())) {
				union(arista.getNodo1(), arista.getNodo2());
				arbol.setValorArista(arista.getNodo1(), arista.getNodo2(), arista.getValor());
				costo += arista.getValor();
			}
		}		
	}
	
	private int find (int x) {
		return (x == caminos[x])? x : find (caminos[x]);
	}
	
	private void union (int x, int y) {
		caminos[find(x)] = find(y);
	}
	
	
}
