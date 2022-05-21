package estados;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import dominio.Item;
import interfaz.MenuComercio;
import interfaz.MenuInfoPersonaje;
import interfaz.MenuIntercambio;
import juego.Juego;
import mensajeria.Comando;
import mensajeria.PaqueteComercio;
import mensajeria.PaqueteFinalizarComercio;
import mensajeria.PaqueteIntercambio;
import mensajeria.PaquetePersonaje;
import mundo.Mundo;
import recursos.Recursos;

public class EstadoComercio extends Estado {

	private Mundo mundo;
	private int[] posMouse;
	private PaquetePersonaje paquetePersonaje;
	private PaquetePersonaje paqueteEnemigo;
	private PaqueteIntercambio paqueteIntercambio;
	private PaqueteFinalizarComercio paqueteFinalizarComercio;

	private boolean miTurno;
	private boolean hayPropuesta;

	private Gson gson = new Gson();

	private MenuComercio menuComercio;
	private MenuIntercambio menuIntercambio;

	public EstadoComercio(Juego juego, PaqueteComercio paqueteComercio) {
		super(juego);
		mundo = new Mundo(juego, "recursos/mundoComercio.txt", "");
		miTurno = paqueteComercio.isMiTurno();

		paquetePersonaje = juego.getEscuchaMensajes().getPersonajesConectados().get(paqueteComercio.getId());
		paqueteEnemigo = juego.getEscuchaMensajes().getPersonajesConectados().get(paqueteComercio.getIdEnemigo());

		menuComercio = new MenuComercio(miTurno, paquetePersonaje, paqueteEnemigo);

		paqueteIntercambio = new PaqueteIntercambio();
		paqueteIntercambio.setId(paqueteComercio.getId());
		paqueteIntercambio.setIdEnemigo(paqueteComercio.getIdEnemigo());
		paqueteIntercambio.setNombre(paqueteEnemigo.getNombre());

		paqueteFinalizarComercio = new PaqueteFinalizarComercio();
		paqueteFinalizarComercio.setId(paqueteComercio.getId());
		paqueteFinalizarComercio.setIdEnemigo(paqueteComercio.getIdEnemigo());

		// por defecto no hay intercambio propuesto
		hayPropuesta = false;

		// limpio la accion del mouse
		juego.getHandlerMouse().setNuevoClick(false);

	}

	@Override
	public void actualizar() {

		juego.getCamara().setxOffset(-350);
		juego.getCamara().setyOffset(150);

		if (hayPropuesta) {
			handlerIntercambio();
		} else {
			handlerComercio();
		}
	}

	public void handlerComercio() {

		if (miTurno) {

			int boton;

			if (juego.getHandlerMouse().getNuevoClickDerecho()) {
				posMouse = juego.getHandlerMouse().getPosMouse();

				boton = menuComercio.getBotonClickeado(posMouse[0], posMouse[1]);

				if (boton >= 0 && boton <= 15) {
					menuComercio.printInfoItem(boton);
				}

				juego.getHandlerMouse().setNuevoClickDerecho(false);
			}

			if (juego.getHandlerMouse().getNuevoClick()) {
				posMouse = juego.getHandlerMouse().getPosMouse();

				boton = menuComercio.getBotonClickeado(posMouse[0], posMouse[1]);

				if (boton >= 0 && boton <= 15) {
					menuComercio.printInfoItem(boton);
					menuComercio.clickEnBoton(boton);
				}

				// boton aceptar
				if (boton == menuComercio.ACEPTAR) {
					armarPaqueteIntercambio();
					enviarPaqueteIntercambio();
					setMiTurno(false);
				}

				juego.getHandlerMouse().setNuevoClick(false);
			}
		}

	}

	public void handlerIntercambio() {
		if (juego.getHandlerMouse().getNuevoClick()) {
			posMouse = juego.getHandlerMouse().getPosMouse();

			switch (menuIntercambio.clickEnBoton(posMouse[0], posMouse[1])) {
			case MenuIntercambio.aceptar:
				finalizarComercio(true);
				break;
			case MenuIntercambio.rechazar:
				finalizarComercio(false);
				break;
			case MenuIntercambio.modificar: // Reinicio el intercambio
				hayPropuesta = false;
				break;
			case MenuIntercambio.cerrar: // Cerrar el menu equivale a rechazar
											// la propuesta
				finalizarComercio(false);
				break;
			}

			juego.getHandlerMouse().setNuevoClick(false);
		}
	}

	@Override
	public void graficar(Graphics g) {

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, juego.getAncho(), juego.getAlto());
		mundo.graficar(g);

		g.drawImage(Recursos.personaje.get(paquetePersonaje.getRaza()).get(2)[0], 0, 300, 256, 256, null);
		g.drawImage(Recursos.personaje.get(paqueteEnemigo.getRaza()).get(6)[0], 0, 50, 256, 256, null);

		mundo.graficarObstaculos(g);

		// Si hay una propuesta la informo, sino voy al menu comercio
		if (hayPropuesta) {
			menuIntercambio = new MenuIntercambio(300, 50, paqueteIntercambio);
			menuIntercambio.graficar(g);
		} else {
			menuComercio.graficar(g);
		}

		g.setColor(Color.GREEN);
	}

	public void recibirPaqueteIntercambio(PaqueteIntercambio paquete) {
		paqueteIntercambio.setId(paquete.getIdEnemigo());
		paqueteIntercambio.setIdEnemigo(paquete.getId());
		paqueteIntercambio.reiniciarListas();
		paqueteIntercambio.setListaPersonaje(paquete.getListaEnemigo());
		paqueteIntercambio.setListaEnemigo(paquete.getListaPersonaje());
		for (int i = 0; i < 8; i++) {
			paqueteIntercambio.setSeleccionadoPersonaje(i, paquete.getSeleccionadoEnemigo(i));
			paqueteIntercambio.setSeleccionadoEnemigo(i, paquete.getSeleccionadoPersonaje(i));
		}
	}

	public void enviarPaqueteIntercambio() {
		try {
			juego.getCliente().getSalida().writeObject(gson.toJson(paqueteIntercambio));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo la conexion con el servidor.");
			e.printStackTrace();
		}
	}

	public void enviarPaqueteFinalizarComercio() {
		try {
			juego.getCliente().getSalida().writeObject(gson.toJson(paqueteFinalizarComercio));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo la conexion con el servidor.");
			e.printStackTrace();
		}
	}

	public void armarPaqueteIntercambio() {
		paqueteIntercambio.reiniciarListas();

		for (int i = 0; i < 8; i++) {
			paqueteIntercambio.setSeleccionadoEnemigo(i, menuComercio.getEstadoBoton(i));
			if (menuComercio.getEstadoBoton(i)) {
				paqueteIntercambio.addDescripcionEnemigo(paqueteEnemigo.getItem(i + 1).getNombre());
			}
		}

		for (int i = 8; i < 16; i++) {
			paqueteIntercambio.setSeleccionadoPersonaje(i - 8, menuComercio.getEstadoBoton(i));
			if (menuComercio.getEstadoBoton(i)) {
				paqueteIntercambio.addDescripcionPersonaje(paquetePersonaje.getItem(i - 7).getNombre());
			}
		}
	}

	public void setMiTurno(boolean miTurno) {
		this.miTurno = miTurno;
		menuComercio.setHabilitado(miTurno);
	}

	public void actualizarBotonesActivos() {
		// el Personaje del paquete que recibi es mi enemigo
		for (int i = 0; i < 8; i++) {
			menuComercio.setEstadoBoton(i, paqueteIntercambio.getSeleccionadoEnemigo(i));
		}
		for (int i = 8; i < 16; i++) {
			menuComercio.setEstadoBoton(i, paqueteIntercambio.getSeleccionadoPersonaje(i - 8));
		}

	}

	public void proponerIntercambio() {
		hayPropuesta = true;
	}

	public void finalizarComercio(boolean aceptado) {
		paqueteFinalizarComercio.aceptaIntercambio(aceptado);
		enviarPaqueteFinalizarComercio();

		if (aceptado) {
			juego.getEstadoJuego().setHaySolicitud(true, juego.getPersonaje(),
					MenuInfoPersonaje.menuIntercambioAceptado);
		} else {
			juego.getEstadoJuego().setHaySolicitud(true, juego.getPersonaje(),
					MenuInfoPersonaje.menuIntercambioRechazado);
		}

		hayPropuesta = false;
		Estado.setEstado(juego.getEstadoJuego());
	}

	public void realizarIntercambio() {
		// Itero por los items
		for (int i = 0; i < 8; i++) {
			if (paqueteIntercambio.getSeleccionadoPersonaje(i) && paqueteIntercambio.getSeleccionadoEnemigo(i)) {
				// Si los dos están seleccionados los intercambio
				Item aux = paquetePersonaje.extraerItem(i + 1);
				paquetePersonaje.agregarItem(paqueteEnemigo.extraerItem(i + 1));
				paqueteEnemigo.agregarItem(aux);
			} else if (paqueteIntercambio.getSeleccionadoPersonaje(i)) {
				paqueteEnemigo.agregarItem(paquetePersonaje.extraerItem(i + 1));
			} else if (paqueteIntercambio.getSeleccionadoEnemigo(i)) {
				paquetePersonaje.agregarItem(paqueteEnemigo.extraerItem(i + 1));
			}
		}

		try {
			paquetePersonaje.setComando(Comando.ACTUALIZARPERSONAJE);
			paqueteEnemigo.setComando(Comando.ACTUALIZARPERSONAJE);

			juego.getCliente().getSalida().writeObject(gson.toJson(paquetePersonaje));
			juego.getCliente().getSalida().writeObject(gson.toJson(paqueteEnemigo));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo la conexiÃ³n con el servidor.");
			e.printStackTrace();
		}

	}
}
