package testsCliente;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;

//import servidor.Conector;
//import servidor.EscuchaCliente;

public class ServidorStub extends Thread {

	private final int PUERTO = 9000;
	private ServerSocket serverSocket;

	public void run() {
		try {

			// conexionDB = new Conector();
			// conexionDB.connect();

			// log.append("Iniciando el servidor..." + System.lineSeparator());
			serverSocket = new ServerSocket(PUERTO);
			// log.append("Servidor esperando conexiones..." +
			// System.lineSeparator());
			String ipRemota;

			// atencionConexiones.start();
			// atencionMovimientos.start();

			while (true) {
				Socket cliente = serverSocket.accept();
				final Gson gson = new Gson();
				ipRemota = cliente.getInetAddress().getHostAddress();
				// log.append(ipRemota + " se ha conectado" +
				// System.lineSeparator());

				ObjectOutputStream salida = new ObjectOutputStream(cliente.getOutputStream());
				ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

				Paquete paquete;
				Paquete paqueteSv = new Paquete(null, 0);
				PaqueteUsuario paqueteUsuario = new PaqueteUsuario();
				PaquetePersonaje paquetePersonaje = new PaquetePersonaje();

				String cadenaLeida = (String) entrada.readObject();

				while (!((paquete = gson.fromJson(cadenaLeida, Paquete.class)).getComando() == Comando.DESCONECTAR)) {

					switch (paquete.getComando()) {

					case Comando.REGISTRO:
						// Paquete que le voy a enviar al usuario
						paqueteSv.setComando(Comando.REGISTRO);

						// No envio a la BD la cadena leida ya que simulo
						// registro o fallo del mismo
						// paqueteUsuario = (PaqueteUsuario)
						// (gson.fromJson(cadenaLeida,
						// PaqueteUsuario.class)).clone();

						if (paquete.getMensaje().equals(Paquete.msjExito)) {
							paqueteSv.setMensaje(Paquete.msjExito);
							salida.writeObject(gson.toJson(paqueteSv));
							// Si el usuario no se pudo registrar le envio un
							// msj de fracaso
						} else {
							paqueteSv.setMensaje(Paquete.msjFracaso);
							salida.writeObject(gson.toJson(paqueteSv));
						}
						break;

					case Comando.CREACIONPJ:

						// Casteo el paquete personaje
						paquetePersonaje = (PaquetePersonaje) (gson.fromJson(cadenaLeida, PaquetePersonaje.class));

						// No guardo guardo el personaje en ese usuario por ser
						// una simulacion que no trabaja contra el server real.
						// Servidor.getConector().registrarPersonaje(paquetePersonaje,
						// paqueteUsuario);

						// Le envio el id del personaje
						salida.writeObject(gson.toJson(paquetePersonaje, paquetePersonaje.getClass()));

						break;

					case Comando.INICIOSESION:
						paqueteSv.setComando(Comando.INICIOSESION);

						// Recibo el paquete usuario
						paqueteUsuario = (PaqueteUsuario) (gson.fromJson(cadenaLeida, PaqueteUsuario.class));

						// Pregunto si quiero el exito o fracaso en el logueo.
						/*
						 * Al no trabajar contra el server real no puedo
						 * devolver un personaje creado anteriormente porque
						 * nunca lo guarde.
						 */
						// Solo simulo el logueo.
						if (paqueteUsuario.isInicioSesion()) {

							paquetePersonaje = new PaquetePersonaje();
							// paquetePersonaje =
							// Servidor.getConector().getPersonaje(paqueteUsuario);
							paquetePersonaje.setComando(Comando.INICIOSESION);
							paquetePersonaje.setMensaje(Paquete.msjExito);
							paquetePersonaje.setId(15);

							salida.writeObject(gson.toJson(paquetePersonaje));

						} else {
							paqueteSv.setMensaje(Paquete.msjFracaso);
							salida.writeObject(gson.toJson(paqueteSv));
						}
						break;

					case Comando.ACTUALIZARPERSONAJE:
						paquetePersonaje = (PaquetePersonaje) gson.fromJson(cadenaLeida, PaquetePersonaje.class);

						// Servidor.getConector().actualizarPersonaje(paquetePersonaje);

						// Servidor.getPersonajesConectados().remove(paquetePersonaje.getId());
						// Servidor.getPersonajesConectados().put(paquetePersonaje.getId(),
						// paquetePersonaje);

						// No actualizo estado para el resto de los clientes ya
						// que es simulado
						/*
						 * for (EscuchaCliente conectado :
						 * Servidor.getClientesConectados()) {
						 * conectado.getSalida().writeObject(gson.toJson(
						 * paquetePersonaje)); }
						 */
						salida.writeObject(gson.toJson(paquetePersonaje));
						break;

					default:
						break;
					}
					cadenaLeida = (String) entrada.readObject();
				}
				entrada.close();
				salida.close();
				cliente.close();
			}
		} catch (Exception e) {
			// log.append("Fallo la conexi�n." + System.lineSeparator());
			e.printStackTrace();
		}
	}
}