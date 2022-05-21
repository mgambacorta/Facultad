package juego;

import java.awt.Canvas;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.gson.Gson;

import chat.VentanaChat;
import chat.MessengerClient;
import cliente.Cliente;
import frames.MenuJugar;
import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaqueteMensaje;
import mensajeria.PaquetePersonaje;
import inventario.InventarioPersonaje;

public class Pantalla {

	private JFrame pantalla;
	private Canvas canvas;
	private InventarioPersonaje inventario;
	private VentanaChat chat;
	public MessengerClient MC;
	private Cliente cliente;

	private final Gson gson = new Gson();

	public Pantalla(final String NOMBRE, final int ANCHO, final int ALTO, final Cliente cliente) {
		pantalla = new JFrame(NOMBRE);
		this.cliente = cliente;
		
		pantalla.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(MenuJugar.class.getResource("/cursor.png")).getImage(), new Point(0, 0),
				"custom cursor"));

		pantalla.setSize(ANCHO, ALTO);
		pantalla.setResizable(false);
		pantalla.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pantalla.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				try {
					Paquete p = new Paquete();
					p.setComando(Comando.DESCONECTAR);
					p.setIp(cliente.getMiIp());
					cliente.getSalida().writeObject(gson.toJson(p));
					cliente.getEntrada().close();
					cliente.getSalida().close();
					cliente.getSocket().close();
					System.exit(0);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Fallo al intentar cerrar la aplicación.");
					System.exit(1);
					e.printStackTrace();
				}
			}
		});

		pantalla.setLocationRelativeTo(null);
		pantalla.setVisible(false);

		pantalla.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_I) {
					if (inventario == null) {
						inventario = new InventarioPersonaje(cliente.getJuego().getPersonaje(), pantalla.getLocation());
						inventario.setLocationRelativeTo(pantalla);
						inventario.setVisible(true);
					} else {
						inventario.dispose();
						inventario = null;
					}
				}
				
				// TODO: Mandar a la ventana de chat, que es la que tiene que llevar esta lógica
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					PaqueteMensaje mensajeFruta = new PaqueteMensaje(cliente.getJuego().getPersonaje().getId(), "Hola soy una difusión");
					try {
						cliente.getSalida().writeObject(gson.toJson(mensajeFruta));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				
				// Muestro los personajes conectados
				if (e.getKeyCode() == KeyEvent.VK_P) {
					if(MC == null) {
						MC = new MessengerClient(cliente);
						MC.crearVentanaCliente();
					}
					MC.setVisible();
				}
			}
		});

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(ANCHO, ALTO));
		canvas.setMaximumSize(new Dimension(ANCHO, ALTO));
		canvas.setMinimumSize(new Dimension(ANCHO, ALTO));
		canvas.setFocusable(false);

		pantalla.add(canvas);
		pantalla.pack();
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public JFrame getFrame() {
		return pantalla;
	}

	public void mostrar() {
		pantalla.setVisible(true);
	}

	public static void centerString(Graphics g, Rectangle r, String s) {
		FontRenderContext frc = new FontRenderContext(null, true, true);

		Rectangle2D r2D = g.getFont().getStringBounds(s, frc);
		int rWidth = (int) Math.round(r2D.getWidth());
		int rHeight = (int) Math.round(r2D.getHeight());
		int rX = (int) Math.round(r2D.getX());
		int rY = (int) Math.round(r2D.getY());

		int a = (r.width / 2) - (rWidth / 2) - rX;
		int b = (r.height / 2) - (rHeight / 2) - rY;

		g.drawString(s, r.x + a, r.y + b);
	}
	
	public MessengerClient getMessengetClient() {
		if(MC == null) {
			MC = new MessengerClient(cliente);
			MC.crearVentanaCliente();
		}
		return MC;
	}
}