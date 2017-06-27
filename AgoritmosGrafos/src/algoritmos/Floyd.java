package algoritmos;

public class Floyd {

	private int[][] matriz;
	private Grafo grafo;
	
	public Floyd(Grafo grafo) {
		this.grafo = grafo;
		matriz = new int[grafo.getGrado()][grafo.getGrado()];
		
		for(int i = 0; i < grafo.getGrado(); i++) {
			for(int j = 0; j < grafo.getGrado(); j++) {
				if( i == j) {
					matriz[i][j] = 0;
				} else {
					matriz[i][j] = grafo.getValorArista(i + 1, j + 1);
				}
			}
		}
	}
	
	public void resolver() {
		
		for(int k = 1; k <= grafo.getGrado(); k++) {
			for(int i = 1; i <= grafo.getGrado(); i++) {
				for(int j = 1; j <= grafo.getGrado(); j++) {
					
					int distancia = grafo.getValorArista(i, k) + grafo.getValorArista(k, j);
					
					if(matriz[i - 1][ j - 1] > distancia) {
						matriz[i - 1][ j - 1] = distancia;
					}
				}
			}
		}
	}
	
	public int getDistancia(int inicio, int fin) {
		return matriz[inicio - 1][fin -1];
	}
}
