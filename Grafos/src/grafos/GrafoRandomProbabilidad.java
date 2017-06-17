package grafos;

public class GrafoRandomProbabilidad extends Grafo {

	private double probabilidad;

	public GrafoRandomProbabilidad(int orden, double probabilidad) {
		super(orden);
		this.probabilidad = probabilidad;
	}

	@Override
	public void generar() {
		for (int i = 1; i < this.orden; i++) {
			for (int j = i; j <= this.orden; j++) {

				double rand = Math.random();
				if (rand <= this.probabilidad) {
					grafo.setValorArista(i, j, 1);
				}
			}
		}
	}
}
