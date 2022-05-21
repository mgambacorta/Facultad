package grafos;

public abstract class Grafo {
	
	protected MatrizSimetrica grafo;
	protected int orden;
	
	public Grafo(int orden) {
		grafo = new MatrizSimetrica(orden);
		this.orden = orden;
	}

	public abstract void generar();
	
	public MatrizSimetrica getMatriz() {
		return grafo;
	}
	
	public void colorearAleatorio() {
		
	}
	
	public void colorearWelshPowell() {
		
	}
	
	public void colorearMatula() {
		
	}
}

