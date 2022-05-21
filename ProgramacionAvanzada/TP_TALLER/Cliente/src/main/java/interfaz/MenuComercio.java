package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import juego.Pantalla;
import mensajeria.PaquetePersonaje;
import dominio.Item;
import recursos.CargadorImagen;
import recursos.Recursos;

public class MenuComercio {

	private static final int x = 400;
	private static final int y = 25;

	// Botones
	public static final int SIN_USO = -1;
	public static final int ACEPTAR = 16;
	private static final int anchoBoton = 60;
	private static final int anchoAceptar = 35;
	private static final int[][] botones = { { x + 22, y + 70 }, { x + 82, y + 70 }, { x + 142, y + 70 },
			{ x + 202, y + 70 }, { x + 22, y + 130 }, { x + 82, y + 130 }, { x + 142, y + 130 }, { x + 202, y + 130 },
			{ x + 22, y + 320 }, { x + 82, y + 320 }, { x + 142, y + 320 }, { x + 202, y + 320 }, { x + 22, y + 380 },
			{ x + 82, y + 380 }, { x + 142, y + 380 }, { x + 202, y + 380 } };
	private static final int[] botonAceptar = { x + 125, y + 500 };
	private boolean[] botonEnUso = { false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false };
	private boolean[] botonActivo = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false };
	// Botones

	// Area de informacion
	private static final int[] posInfoEnemigo = { x + 25, y + 225 };
	private static final int[] posInfoPersonaje = { x + 25, y + 470 };
	private String infoEnemigo;
	private String infoPersonaje;
	private String[] descripciones;
	// area de informacion

	private boolean habilitado;
	private PaquetePersonaje personaje;
	private PaquetePersonaje enemigo;

	public MenuComercio(boolean habilitado, PaquetePersonaje personaje, PaquetePersonaje enemigo) {
		this.habilitado = habilitado;
		this.personaje = personaje;
		this.enemigo = enemigo;
		this.descripciones = new String[16];
		this.infoEnemigo = "";
		this.infoPersonaje = "";
	}

	public void graficar(Graphics g) {

		g.drawImage(Recursos.menuMercado, x, y, null);

		// Dibujo los items de los personajes
		for (int i = 0; i < 6; i++) {
			Item item = this.enemigo.getItem(i + 1);
			if (item != null) {
				g.drawImage(CargadorImagen.cargarImagen("/armamento/" + item.getFoto()), botones[i][0], botones[i][1],
						anchoBoton, anchoBoton, null);

				botonEnUso[i] = true;
				descripciones[i] = item.getDescripcionItem();
			}
		}

		for (int i = 8; i < 16; i++) {
			Item item = this.personaje.getItem(i - 7);
			if (item != null) {
				g.drawImage(CargadorImagen.cargarImagen("/armamento/" + item.getFoto()), botones[i][0], botones[i][1],
						anchoBoton, anchoBoton, null);

				botonEnUso[i] = true;
				descripciones[i] = item.getDescripcionItem();
			}
		}

		// Dibujo los botones activos
		for (int i = 0; i < 16; i++) {
			if (botonActivo[i]) {
				g.drawImage(Recursos.checkItem, botones[i][0], botones[i][1], anchoBoton, anchoBoton, null);
			}
		}

		// Dibujo las informaciones
		g.setFont(new Font("Book Antiqua", 0, 10));
		g.setColor(Color.WHITE);
		g.drawString(infoEnemigo, posInfoEnemigo[0], posInfoEnemigo[1]);
		g.drawString(infoPersonaje, posInfoPersonaje[0], posInfoPersonaje[1]);

		// Dibujo el turno de quien es
		g.setFont(new Font("Book Antiqua", 1, 14));
		if (habilitado)
			Pantalla.centerString(g, new Rectangle(x, y + 10, Recursos.menuMercado.getWidth(), 25), "Mi Turno");
		else
			Pantalla.centerString(g, new Rectangle(x, y + 10, Recursos.menuMercado.getWidth(), 25), "Turno Rival");
	}

	public int getBotonClickeado(int mouseX, int mouseY) {
		if (!habilitado)
			return SIN_USO;

		for (int i = 0; i < botones.length; i++) {
			if (mouseX >= botones[i][0] && mouseX <= botones[i][0] + anchoBoton && mouseY >= botones[i][1]
					&& mouseY <= botones[i][1] + anchoBoton) {

				return i;
			}
		}

		if (mouseX >= botonAceptar[0] && mouseX <= botonAceptar[0] + anchoAceptar && mouseY >= botonAceptar[1]
				&& mouseY <= botonAceptar[1] + anchoAceptar) {
			return ACEPTAR;
		}
		return SIN_USO;
	}

	public boolean getEstadoBoton(int boton) {
		return botonActivo[boton];
	}
	
	public void setEstadoBoton(int boton, boolean estado) {
		botonActivo[boton] = habilitado && botonEnUso[boton] && estado;
	}
	
	public void clickEnBoton(int boton) {
		botonActivo[boton] = habilitado && botonEnUso[boton] && !botonActivo[boton];
	}

	public void printInfoItem(int boton) {
		if (habilitado && botonEnUso[boton]) {
			if (boton >= 0 && boton <= 7) {
				infoEnemigo = descripciones[boton];
			} else {
				infoPersonaje = descripciones[boton];
			}
		}
	}

	public void setHabilitado(boolean b) {
		habilitado = b;
	}
}
