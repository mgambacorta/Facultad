package comunicacion;

import com.google.gson.Gson;
import mensajeria.PaqueteDePersonajes;

public class ProcesadorConexion extends Procesador {

	public ProcesadorConexion(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		contextoProcesador
				.setPersonajesConectados(gson.fromJson(cadenaLeida, PaqueteDePersonajes.class).getPersonajes());
		contextoProcesador.getJuego().actualizarUsuariosChat();
		return null;
	}

}
