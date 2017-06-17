package grafos;

public class GrafoRegular extends Grafo{

	private int grado;
	
	public GrafoRegular(int orden, int grado) {
		super(orden);
		this.grado = grado;
	}

	@Override
	public void generar() {
		if( grado >= orden ) {
			System.out.println("No se puede generar un grafo de grado mayor a orden - 1");
			return;
		}
		
		if( (orden % 2) == 1 && (grado % 2) == 1 ) {
			System.out.println("No se puede generar un grafo de grado y orden impar");
			return;
		}
		
		if(grado % 2 == 1) {
			adyacenciaGrado1();
		}
		
		for(int gradAux = 2; gradAux <= grado; gradAux+=2) {
			for(int i = 1; i <= orden; i++) {
				grafo.setValorArista(i, sumaCircular(i, gradAux / 2), 1);
			}
		}
	}

	private int sumaCircular(int inicio, int adicion) {
		int aux = inicio + adicion;
		if( aux > this.orden ) {
			return aux - orden;
		}
		return aux;
	}
	
	private void adyacenciaGrado1() {
		for(int i = 1; i <= this.orden / 2; i++) {
			this.grafo.setValorArista(i, i + this.orden / 2, 1);
		}
	}
}
