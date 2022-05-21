package comunicacion;

import com.google.gson.Gson;

import estados.Estado;
import estados.EstadoBatalla;
import mensajeria.PaqueteBatalla;

public class ProcesadorBatalla extends Procesador {

	public ProcesadorBatalla(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		contextoProcesador.getPaquetePersonaje().setEstado(Estado.estadoBatalla);
		Estado.setEstado(null);
		PaqueteBatalla paqueteBatalla = gson.fromJson(cadenaLeida, PaqueteBatalla.class);
		contextoProcesador.getJuego()
				.setEstadoBatalla(new EstadoBatalla(contextoProcesador.getJuego(), paqueteBatalla));
		Estado.setEstado(contextoProcesador.getJuego().getEstadoBatalla());
		return null;
	}

}
