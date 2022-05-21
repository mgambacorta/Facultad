package comunicacion;

import com.google.gson.Gson;
import mensajeria.PaqueteDeMovimientos;

public class ProcesadorMovimiento extends Procesador {

	public ProcesadorMovimiento(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		contextoProcesador
				.setUbicacionPersonajes(gson.fromJson(cadenaLeida, PaqueteDeMovimientos.class).getPersonajes());
		return null;
	}

}
