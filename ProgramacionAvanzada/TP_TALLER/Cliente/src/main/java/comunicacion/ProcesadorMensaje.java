package comunicacion;

import com.google.gson.Gson;

import mensajeria.PaqueteMensaje;

public class ProcesadorMensaje extends Procesador {

	public ProcesadorMensaje(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		contextoProcesador.getJuego().recibirMensaje(gson.fromJson(cadenaLeida, PaqueteMensaje.class));
		return null;
	}

}
