package grafos;

public class MatrizSimetrica {

	private int matriz[];
	private int tamanio;
	private int orden;
	
	public MatrizSimetrica(int orden) {
		this.orden = orden;
		this.tamanio = orden * (orden - 1) / 2;
		this.matriz = new int[tamanio];
	}

	public int getValorArista(int fila, int columna) {
		int idx = indice(fila, columna);

		if (idx == -1) {
			return 0;
		}

		return matriz[idx];
	}

	public void setValorArista(int fila, int columna, int valor) {
		int idx = indice(fila, columna);

		if (idx != -1) {
			matriz[idx] = valor;
		}
	}

	private int indice(int fila, int columna) {
		if (fila == columna) {
			return -1;
		}

		int f;
		int c;

		if (fila < columna) {
			f = fila - 1;
			c = columna - 1;
		} else {
			f = columna - 1;
			c = fila - 1;
		}

		return (int) (f * orden + c - (Math.pow(f, 2) + 3 * f + 2) / 2);
	}

	public void printMatriz() {

		for (int i = 1; i <= this.orden; i++) {

			String mostrar = "| ";

			for (int j = 1; j <= this.orden; j++) {
				mostrar = mostrar + this.getValorArista(i, j) + " ";
			}

			System.out.println(mostrar + "|");
		}
	}
}
