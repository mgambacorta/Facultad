package comunicacion;

import com.google.gson.Gson;

import mensajeria.PaqueteIntercambio;

public class ProcesadorIntercambiar extends Procesador {

	public ProcesadorIntercambiar(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String procesar(String cadenaLeida) {
		PaqueteIntercambio paqueteIntercambio = gson.fromJson(cadenaLeida, PaqueteIntercambio.class);
		contextoProcesador.getJuego().getEstadoComercio().recibirPaqueteIntercambio(paqueteIntercambio);
		contextoProcesador.getJuego().getEstadoComercio().setMiTurno(true);
		contextoProcesador.getJuego().getEstadoComercio().actualizarBotonesActivos();
		contextoProcesador.getJuego().getEstadoComercio().proponerIntercambio();
		return null;
	}

}
