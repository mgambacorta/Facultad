package grafos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrafoRandomPorcentaje extends Grafo {

	private int porcentaje;
	
	public GrafoRandomPorcentaje(int orden, int porcentaje) {
		super(orden);
		this.porcentaje = porcentaje;
	}

	@Override
	public void generar() {
		List<int[]> lista = new ArrayList<int[]>();
		Random rnm = new Random();

		// armo una lista con todas las aristas posibles
		for(int i = 1; i < this.orden; i++) {
			for(int j = i; j <= this.orden; j++) {
				lista.add(new int[] {i,j});
			}
		}
		
		// saco de la lista, en forma aleatorea x% de aristas
		int tamaIni = lista.size();
		for(int i = 1; i <= tamaIni * porcentaje / 100; i++ ) {

			int randomIdx = rnm.nextInt(lista.size());
			int[] coord = lista.remove( randomIdx );
			grafo.setValorArista(coord[0], coord[1], 1);
		}	
	}
}
