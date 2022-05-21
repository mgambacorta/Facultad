package cliente;

import java.util.Map;
import javax.swing.JOptionPane;
import com.google.gson.Gson;
import comunicacion.Procesador;
import comunicacion.ProcesadorFactory;
import juego.Juego;
import mensajeria.Paquete;
import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;

public class EscuchaMensajes extends Thread {

	private Cliente cliente;
	private final Gson gson = new Gson();

	public EscuchaMensajes(Juego juego) {
		cliente = juego.getCliente();
	}

	public void run() {

		try {
			Paquete paquete;
			while (true) {
				String objetoLeido = (String) cliente.getContexto().getEntrada().readObject();
				paquete = gson.fromJson(objetoLeido, Paquete.class);
				Procesador procesador = ProcesadorFactory.crear(paquete.getComando(), this.cliente.getContexto(),
						this.gson);
				procesador.procesar(objetoLeido);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Fallo la conexión con el servidor.");
			e.printStackTrace();
		}
	}

	public Map<Integer, PaqueteMovimiento> getUbicacionPersonajes() {
		return cliente.getContexto().getUbicacionPersonajes();
	}

	public Map<Integer, PaquetePersonaje> getPersonajesConectados() {
		return cliente.getContexto().getPersonajesConectados();
	}
}