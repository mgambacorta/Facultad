package comunicacion;

import com.google.gson.Gson;

import mensajeria.PaqueteAtacar;

public class ProcesadorAtacar extends Procesador {

	public ProcesadorAtacar(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		PaqueteAtacar paqueteAtacar = gson.fromJson(cadenaLeida, PaqueteAtacar.class);
		contextoProcesador.getJuego().getEstadoBatalla().getEnemigo().refreshAtacante(paqueteAtacar);
		contextoProcesador.getJuego().getEstadoBatalla().getPersonaje().refreshAtacado(paqueteAtacar);
		contextoProcesador.getJuego().getEstadoBatalla().setMiTurno(true);
		return null;
	}

}
