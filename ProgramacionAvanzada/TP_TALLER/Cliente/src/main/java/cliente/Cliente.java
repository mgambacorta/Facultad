package cliente;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;
import com.google.gson.Gson;
import comunicacion.ComandoDesconocidoException;
import comunicacion.ContextoProcesador;
import comunicacion.Procesador;
import comunicacion.ProcesadorFactory;
import frames.*;
import juego.Juego;
import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;

public class Cliente extends Thread {

	private Socket socketCliente;
	private String miIp;
	private ContextoProcesador contexto;

	// Objeto gson
	private final Gson gson = new Gson();

	// Acciones que realiza el usuario
	private int accion;

	// Ip y puerto
	private String ip;
	private int puerto;

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	private MenuCarga menuCarga;

	public Cliente(String ip, int puerto) {

		try {
			socketCliente = new Socket(ip, puerto);
			miIp = socketCliente.getInetAddress().getHostAddress();
			this.contexto = new ContextoProcesador(new ObjectInputStream(socketCliente.getInputStream()),
					new ObjectOutputStream(socketCliente.getOutputStream()));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo al iniciar la aplicación. Revise la conexión con el servidor.");
			System.exit(1);
			e.printStackTrace();
		}
	}

	public Cliente() {

		Scanner sc;

		try {
			sc = new Scanner(new File("config.txt"));
			ip = sc.nextLine();
			puerto = sc.nextInt();
			sc.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "No se ha encontrado el archivo de configuración config.txt");
			e.printStackTrace();
		}

		try {
			socketCliente = new Socket(ip, puerto);
			miIp = socketCliente.getInetAddress().getHostAddress();
			this.contexto = new ContextoProcesador(new ObjectInputStream(socketCliente.getInputStream()),
					new ObjectOutputStream(socketCliente.getOutputStream()));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo al iniciar la aplicación. Revise la conexión con el servidor.");
			System.exit(1);
			e.printStackTrace();
		}
	}

	public void run() {
		synchronized (this) {
			try {
				while (!getPaqueteUsuario().isInicioSesion()) {
					new MenuJugar(this).setVisible(true);
					wait();

					switch (getAccion()) {
					case Comando.REGISTRO:
						getPaqueteUsuario().setComando(Comando.REGISTRO);
						break;
					case Comando.INICIOSESION:
						getPaqueteUsuario().setComando(Comando.INICIOSESION);
						break;
					case Comando.SALIR:
						getPaqueteUsuario().setIp(getMiIp());
						getPaqueteUsuario().setComando(Comando.DESCONECTAR);
						break;
					}
					contexto.getSalida().writeObject(gson.toJson(getPaqueteUsuario()));

					if (getAccion() == Comando.SALIR) {
						this.socketCliente.close();
						System.exit(1);
					}

					String cadenaLeida = (String) contexto.getEntrada().readObject();
					Procesador proceso = ProcesadorFactory.crear(gson.fromJson(cadenaLeida, Paquete.class).getComando(),
							contexto, gson);
					proceso.procesar(cadenaLeida);
				}

				// Creo un paquete con el comando mostrar mapas
				getPaquetePersonaje().setComando(Comando.MOSTRARMAPAS);

				// Abro el menu de eleccion del mapa
				MenuMapas menuElegirMapa = new MenuMapas(this);
				menuElegirMapa.setVisible(true);

				// Espero a que el usuario elija el mapa
				wait();

				// Establezco el mapa en el paquete personaje
				getPaquetePersonaje().setIp(miIp);

				// Le envio el paquete con el mapa seleccionado
				contexto.getSalida().writeObject(gson.toJson(getPaquetePersonaje()));

				// Instancio el juego y cargo los recursos
				this.contexto.setJuego(new Juego("World Of the Middle Earth", 800, 600, this));

				// Muestro el menu de carga
				menuCarga = new MenuCarga(this);
				menuCarga.setVisible(true);

				// Espero que se carguen todos los recursos
				wait();

				// Inicio el juego
				this.getJuego().start();

				// Finalizo el menu de carga
				menuCarga.dispose();

			} catch (IOException | InterruptedException | ClassNotFoundException | ComandoDesconocidoException e) {
				JOptionPane.showMessageDialog(null, "Fallo la conexión con el servidor durante el inicio de sesión.");
				System.exit(1);
				e.printStackTrace();
			}
		}

	}

	public Socket getSocket() {
		return socketCliente;
	}

	public String getMiIp() {
		return miIp;
	}

	public void setMiIp(String miIp) {
		this.miIp = miIp;
	}

	public ObjectInputStream getEntrada() {
		return getContexto().getEntrada();
	}

	public ObjectOutputStream getSalida() {
		return getContexto().getSalida();
	}

	public PaqueteUsuario getPaqueteUsuario() {
		return getContexto().getPaqueteUsuario();
	}

	public PaquetePersonaje getPaquetePersonaje() {
		return getContexto().getPaquetePersonaje();
	}

	public synchronized ContextoProcesador getContexto() {
		return this.contexto;
	}

	public Juego getJuego() {
		return getContexto().getJuego();
	}

	public MenuCarga getMenuCarga() {
		return menuCarga;
	}
}
