package comunicacion;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import juego.Juego;
import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;

public class ContextoProcesador {
	private PaquetePersonaje paquetePersonaje;
	private PaqueteUsuario paqueteUsuario;
	private ObjectInputStream entrada;
	private ObjectOutputStream salida;
	private Juego juego;
	
	private Map<Integer, PaqueteMovimiento> ubicacionPersonajes;
	private Map<Integer, PaquetePersonaje> personajesConectados;

	public ContextoProcesador(ObjectInputStream entrada, ObjectOutputStream salida) {
		this.entrada = entrada;
		this.salida = salida;
		this.paqueteUsuario = new PaqueteUsuario();
		this.paquetePersonaje = new PaquetePersonaje();
	}

	public PaquetePersonaje getPaquetePersonaje() {
		return this.paquetePersonaje;
	}

	public void setPaquetePersonaje(PaquetePersonaje paquetePersonaje) {
		this.paquetePersonaje = paquetePersonaje;
	}

	public PaqueteUsuario getPaqueteUsuario() {
		return paqueteUsuario;
	}

	public ObjectInputStream getEntrada() {
		return entrada;
	}

	public ObjectOutputStream getSalida() {
		return salida;
	}

	public Map<Integer, PaquetePersonaje> getPersonajesConectados() {
		return personajesConectados;
	}

	public void setPersonajesConectados(Map<Integer, PaquetePersonaje> personajesConectados) {
		this.personajesConectados = personajesConectados;
	}

	public Map<Integer, PaqueteMovimiento> getUbicacionPersonajes() {
		return ubicacionPersonajes;
	}

	public void setUbicacionPersonajes(Map<Integer, PaqueteMovimiento> ubicacionPersonajes) {
		this.ubicacionPersonajes = ubicacionPersonajes;
	}

	public Juego getJuego() {
		return juego;
	}

	public void setJuego(Juego juego) {
		this.juego = juego;
	}
}
