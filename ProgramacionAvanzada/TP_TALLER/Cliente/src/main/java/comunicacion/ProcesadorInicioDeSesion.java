package comunicacion;

import javax.swing.JOptionPane;
import com.google.gson.Gson;
import mensajeria.Paquete;
import mensajeria.PaquetePersonaje;

public class ProcesadorInicioDeSesion extends Procesador {

	public ProcesadorInicioDeSesion(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		Paquete paquete = gson.fromJson(cadenaLeida, Paquete.class);
		if (paquete.getMensaje().equals(Paquete.msjExito)) {
			contextoProcesador.getPaqueteUsuario().setInicioSesion(true);
			contextoProcesador.setPaquetePersonaje(gson.fromJson(cadenaLeida, PaquetePersonaje.class));
		} else {
			JOptionPane.showMessageDialog(null, "Error al iniciar sesi�n. Revise el usuario y la contrase�a");
			contextoProcesador.getPaqueteUsuario().setInicioSesion(false);
		}
		return null;
	}

}
