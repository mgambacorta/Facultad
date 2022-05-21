package grafos;

public class GrafoNPartito extends Grafo {

	private int particion;
	
	public GrafoNPartito(int orden, int particion) {
		super(orden);
		this.particion = particion;
	}

	@Override
	public void generar() {
		int grupo = 1;
		int vecGrupos[] = new int[this.orden + 1]; //el cero lo voy a ignorar
		
		//armo los grupos
		for(int i = 1; i <= this.orden; i++) {
			vecGrupos[i] = grupo;
			
			if(grupo < this.particion) {
				grupo++;
			} else {
				grupo = 1;
			}
		}
		
		// Si los nodos partenecen a grupos distintos los uno
		for(int i = 1; i < this.orden; i++) {
			for( int j = i; j <= this.orden; j++) {
				if( vecGrupos[i] != vecGrupos[j] ) {
					this.grafo.setValorArista(i, j, 1);
				}
			}
		}
	}
}
