package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import juego.Pantalla;
import mensajeria.PaqueteIntercambio;
import recursos.Recursos;

public class MenuIntercambio {
	private static final BufferedImage menu = Recursos.menuEnemigo;
	public static final int cerrar = 0;
	public static final int aceptar = 1;
	public static final int rechazar = 2;
	public static final int modificar = 3;
	public static final int anchoBoton = 200;
	public static final int altoBoton = 25;

	private int x;
	private int y;
	private PaqueteIntercambio paqueteIntercambio;

	public MenuIntercambio(int x, int y, PaqueteIntercambio paqueteIntercambio) {
		this.x = x;
		this.y = y;
		this.paqueteIntercambio = paqueteIntercambio;
	}

	public void graficar(Graphics g) {

		// dibujo el menu
		g.drawImage(menu, x, y, null);

		// Armo la lista de objetos a intercambiar
		int linea = y + 80;
		Pantalla.centerString(g, new Rectangle(x, linea, menu.getWidth(), 0),
				paqueteIntercambio.getNombre() + " propone intercambiar:");
		for (String obj : paqueteIntercambio.getListaEnemigo()) {
			linea += 20;
			Pantalla.centerString(g, new Rectangle(x, linea, menu.getWidth(), 0), obj);
		}
		linea += 40;
		Pantalla.centerString(g, new Rectangle(x, linea, menu.getWidth(), 0), "Por tu:");
		for (String obj : paqueteIntercambio.getListaPersonaje()) {
			linea += 20;
			Pantalla.centerString(g, new Rectangle(x, linea, menu.getWidth(), 0), obj);
		}

		// muestro el nombre
		g.setColor(Color.WHITE);
		g.setFont(new Font("Book Antiqua", 1, 20));
		Pantalla.centerString(g, new Rectangle(x, y + 15, menu.getWidth(), 0), paqueteIntercambio.getNombre());

		// muestro los botones
		g.setFont(new Font("Book Antiqua", 1, 20));
		g.drawImage(Recursos.botonMenu, x + 50, y + 350, anchoBoton, altoBoton, null);
		g.drawImage(Recursos.botonMenu, x + 50, y + 380, anchoBoton, altoBoton, null);
		g.drawImage(Recursos.botonMenu, x + 50, y + 410, anchoBoton, altoBoton, null);
		g.setColor(Color.WHITE);
		Pantalla.centerString(g, new Rectangle(x + 50, y + 350, anchoBoton, altoBoton), "Aceptar");
		Pantalla.centerString(g, new Rectangle(x + 50, y + 380, anchoBoton, altoBoton), "Rechazar");
		Pantalla.centerString(g, new Rectangle(x + 50, y + 410, anchoBoton, altoBoton), "Modificar");
	}

	public int clickEnBoton(int mouseX, int mouseY) {
		if (mouseX >= x + 50 && mouseX <= x + 250 && mouseY >= y + 340 && mouseY <= y + 365)
			return aceptar;
		if (mouseX >= x + 50 && mouseX <= x + 250 && mouseY >= y + 380 && mouseY <= y + 405)
			return rechazar;
		if (mouseX >= x + 50 && mouseX <= x + 250 && mouseY >= y + 420 && mouseY <= y + 425)
			return modificar;
		if (mouseX >= x + menu.getWidth() - 24 && mouseX <= x + menu.getWidth() + 4 && mouseY >= y + 12
				&& mouseY <= y + 36)
			return cerrar;

		return -1;
	}

	public boolean clickEnMenu(int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x + menu.getWidth() && mouseY >= y && mouseY <= y + menu.getHeight())
			return true;
		return false;
	}

}
