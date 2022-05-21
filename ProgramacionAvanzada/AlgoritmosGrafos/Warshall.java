package algoritmos;

public class Warshall {
	private boolean[][] matriz;
	private Grafo grafo;
	
	public Warshall(Grafo grafo) {
		this.grafo = grafo;
		matriz = new boolean[grafo.getGrado()][grafo.getGrado()];
		
		for(int i = 0; i < grafo.getGrado(); i++) {
			for(int j = 0; j < grafo.getGrado(); j++) {
				matriz[i][j] = grafo.getValorArista(i + 1, j + 1) != Grafo.INFINITO;
			}
		}
	}
	
	public void resolver() {
		
		for(int k = 1; k <= grafo.getGrado(); k++) {
			for(int i = 0; i < grafo.getGrado(); i++) {
				for(int j = 0; j < grafo.getGrado(); j++) {
					matriz[i][j] =  matriz[i][j] || (grafo.getValorArista(i + 1, k) != Grafo.INFINITO && 
							grafo.getValorArista(k, j + 1) != Grafo.INFINITO);
				}
			}
		}
	}
	
	public boolean hayCamino(int inicio, int fin) {
		return matriz[inicio - 1][fin -1];
	}
}
