package comunicacion;

import com.google.gson.Gson;

import estados.Estado;
import interfaz.MenuInfoPersonaje;
import mensajeria.PaqueteFinalizarComercio;

public class ProcesadorFinalizarComercio extends Procesador {

	public ProcesadorFinalizarComercio(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		PaqueteFinalizarComercio paqueteFinalizarComercio = gson.fromJson(cadenaLeida, PaqueteFinalizarComercio.class);
		if (paqueteFinalizarComercio.aceptaIntercambio()) {
			contextoProcesador.getJuego().getEstadoComercio().realizarIntercambio();
			contextoProcesador.getJuego().getEstadoJuego().setHaySolicitud(true,
					contextoProcesador.getPaquetePersonaje(), MenuInfoPersonaje.menuIntercambioAceptado);
		} else {
			contextoProcesador.getJuego().getEstadoJuego().setHaySolicitud(true,
					contextoProcesador.getPaquetePersonaje(), MenuInfoPersonaje.menuIntercambioRechazado);
		}
		contextoProcesador.getPaquetePersonaje().setEstado(Estado.estadoJuego);
		Estado.setEstado(contextoProcesador.getJuego().getEstadoJuego());
		return null;
	}

}
