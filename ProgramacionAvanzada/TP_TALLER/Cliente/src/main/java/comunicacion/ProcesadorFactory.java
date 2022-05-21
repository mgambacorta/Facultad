package comunicacion;

import com.google.gson.Gson;

import mensajeria.Comando;

public class ProcesadorFactory {
	public static Procesador crear(int comando, ContextoProcesador contextoProcesador, Gson gson) throws ComandoDesconocidoException {
		switch (comando) {
		case Comando.REGISTRO:
			return new ProcesadorRegistro(contextoProcesador, gson);
		case Comando.COMERCIO:
			return new ProcesadorComercio(contextoProcesador, gson);
		case Comando.INICIOSESION:
			return new ProcesadorInicioDeSesion(contextoProcesador, gson);
		case Comando.ACTUALIZARPERSONAJE:
			return new ProcesadorActualizarPersonaje(contextoProcesador, gson);
		case Comando.ATACAR:
			return new ProcesadorAtacar(contextoProcesador, gson);
		case Comando.BATALLA:
			return new ProcesadorBatalla(contextoProcesador, gson);
		case Comando.CONEXION:
			return new ProcesadorConexion(contextoProcesador, gson);
		case Comando.FINALIZARBATALLA:
			return new ProcesadorFinalizarBatalla(contextoProcesador, gson);
		case Comando.INTERCAMBIAR:
			return new ProcesadorIntercambiar(contextoProcesador, gson);
		case Comando.MOVIMIENTO:
			return new ProcesadorMovimiento(contextoProcesador, gson);
		case Comando.MENSAJE: 
			return new ProcesadorMensaje(contextoProcesador, gson);
		case Comando.FINALIZARCOMERCIO: 
			return new ProcesadorFinalizarComercio(contextoProcesador, gson);
		default:
			throw new ComandoDesconocidoException();
		}
	}
}
