package comunicacion;

import javax.swing.JOptionPane;
import com.google.gson.Gson;
import frames.MenuCreacionPj;
import mensajeria.Paquete;
import mensajeria.PaquetePersonaje;

public class ProcesadorRegistro extends Procesador {

	public ProcesadorRegistro(ContextoProcesador contextoProcesador, Gson gson) {
		super(contextoProcesador, gson);
	}

	@Override
	public String procesar(String cadenaLeida) {
		PaquetePersonaje paquetePersonaje = gson.fromJson(cadenaLeida, PaquetePersonaje.class);

		if (paquetePersonaje.getMensaje().equals(Paquete.msjExito)) {
			MenuCreacionPj menuCreacionPJ = new MenuCreacionPj(contextoProcesador);
			menuCreacionPJ.setModal(true);
			menuCreacionPJ.setVisible(true);
		} else {
			if (paquetePersonaje.getMensaje().equals(Paquete.msjFracaso))
				JOptionPane.showMessageDialog(null, "No se pudo registrar.");
			contextoProcesador.getPaqueteUsuario().setInicioSesion(false);
		}
		return null;
	}

}
