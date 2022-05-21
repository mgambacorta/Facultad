package comunicacion;

import com.google.gson.Gson;

import estados.Estado;
import estados.EstadoComercio;
import mensajeria.PaqueteComercio;

public class ProcesadorComercio extends Procesador {

	public ProcesadorComercio(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		PaqueteComercio paqueteComercio = gson.fromJson(cadenaLeida, PaqueteComercio.class); 
		contextoProcesador.getPaquetePersonaje().setEstado(Estado.estadoComercio);
		Estado.setEstado(null);
		contextoProcesador.getJuego().setEstadoComercio(new EstadoComercio(contextoProcesador.getJuego(), paqueteComercio));
		Estado.setEstado(contextoProcesador.getJuego().getEstadoComercio());
		return null;
	}

}
