package comunicacion;

import com.google.gson.Gson;
import estados.Estado;

public class ProcesadorFinalizarBatalla extends Procesador{

	public ProcesadorFinalizarBatalla(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		contextoProcesador.getPaquetePersonaje().setEstado(Estado.estadoJuego);
		Estado.setEstado(contextoProcesador.getJuego().getEstadoJuego());
		return null;
	}
}
