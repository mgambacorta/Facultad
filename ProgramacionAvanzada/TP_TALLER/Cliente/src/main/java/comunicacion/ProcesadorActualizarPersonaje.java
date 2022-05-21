package comunicacion;

import com.google.gson.Gson;

import mensajeria.PaquetePersonaje;

public class ProcesadorActualizarPersonaje extends Procesador {

	public ProcesadorActualizarPersonaje(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		PaquetePersonaje paquetePersonaje = gson.fromJson(cadenaLeida, PaquetePersonaje.class);
		contextoProcesador.getPersonajesConectados().put(paquetePersonaje.getId(), paquetePersonaje);

		if (contextoProcesador.getPaquetePersonaje().getId() == paquetePersonaje.getId()) {
			contextoProcesador.setPaquetePersonaje((PaquetePersonaje) paquetePersonaje.clone());
			contextoProcesador.getJuego().getEstadoJuego().actualizarPersonaje(); // volarlo
		}
		return null;
	}

}
