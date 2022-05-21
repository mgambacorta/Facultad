package algoritmos;

public class Grafo {
	
	public static final int INFINITO = 9999;
	private int[][] matriz; 
	private int grado;
	
	public Grafo(int grado) {
		this.grado = grado;
		this.matriz = new int[grado][grado];
		
		for(int i = 0; i < grado; i++) {
			for(int j = 0; j < grado; j++) {
				this.matriz[i][j] = INFINITO;
			}
		}
	}
	
	public void cargar() {
		
	}
	
	public int getGrado() {
		return this.grado;
	}
	
	public void setValorArista(int nodo1, int nodo2, int valor) {
		if(nodo1 > 0 && nodo1 <= this.grado && nodo2 > 0 && nodo2 <= this.grado) {
			this.matriz[nodo1 - 1][nodo2 - 1] = valor;
		}		
	}
	
	public int getValorArista(int nodo1, int nodo2) {
		if(nodo1 > 0 && nodo1 <= this.grado && nodo2 > 0 && nodo2 <= this.grado) {
			return this.matriz[nodo1 - 1][nodo2 - 1];
		}
		return INFINITO;
	}
}
