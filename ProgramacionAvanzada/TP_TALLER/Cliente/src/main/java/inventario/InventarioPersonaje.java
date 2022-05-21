package inventario;

import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import dominio.Item;
import dominio.TipoItem;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.util.Collection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.SwingConstants;
import mensajeria.PaquetePersonaje;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InventarioPersonaje extends JFrame {

	private JPanel contentPane;
	private String pathImagenes = "recursos/armamento/";

	/**
	 * Create the frame.
	 */
	public InventarioPersonaje(PaquetePersonaje personaje, Point location) {
		setType(Type.POPUP);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_I) {
					dispose();
				}
			}
		});
		setResizable(false);
		setTitle("Inventario Personaje");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 406, 389);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocation(location);
		Collection<Item> items = personaje.getInventario();

		for (Item item : items) {
			String pathImagen = pathImagenes + item.getFoto();
			String descripcionItem = item.getDescripcionItem();
			switch (item.getIdTipoItem()) {
			case TipoItem.ARMA:
				ImageIcon arma = new ImageIcon(pathImagen);
				final String descripcionArma = descripcionItem;
				final JLabel labelArma = new JLabel(arma);
				labelArma.setHorizontalAlignment(SwingConstants.CENTER);
				labelArma.setBounds(25, 109, 82, 134);
				labelArma.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						labelArma.setToolTipText(descripcionArma);
					}
				});
				contentPane.add(labelArma);
				break;
			case TipoItem.ARMADURA:
				ImageIcon armadura = new ImageIcon(pathImagen);
				final String descripcionArmadura = descripcionItem;
				final JLabel labelArmadura = new JLabel(armadura);
				labelArmadura.setHorizontalAlignment(SwingConstants.CENTER);
				labelArmadura.setBounds(154, 128, 104, 205);
				labelArmadura.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						labelArmadura.setToolTipText(descripcionArmadura);
					}
				});
				contentPane.add(labelArmadura);
				break;

			case TipoItem.BOTAS:
				ImageIcon botas = new ImageIcon(pathImagen);
				final String descripcionBotas = descripcionItem;
				final JLabel labelBotas = new JLabel(botas);
				labelBotas.setHorizontalAlignment(SwingConstants.CENTER);
				labelBotas.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						labelBotas.setToolTipText(descripcionBotas);
					}
				});
				labelBotas.setBounds(299, 254, 88, 92);
				contentPane.add(labelBotas);
				break;

			case TipoItem.CASCO:
				ImageIcon casco = new ImageIcon(pathImagen);
				final String descripcionCasco = descripcionItem;
				final JLabel labelCasco = new JLabel(casco);
				labelCasco.setHorizontalAlignment(SwingConstants.CENTER);
				labelCasco.setBounds(154, 11, 104, 92);
				labelCasco.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						labelCasco.setToolTipText(descripcionCasco);
					}
				});
				contentPane.add(labelCasco);
				break;

			case TipoItem.ESCUDO:
				ImageIcon escudo = new ImageIcon(pathImagen);
				final String descripcionEscudo = descripcionItem;
				final JLabel labelEscudo = new JLabel(escudo);
				labelEscudo.setHorizontalAlignment(SwingConstants.CENTER);
				labelEscudo.setBounds(299, 109, 88, 134);
				labelEscudo.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						labelEscudo.setToolTipText(descripcionEscudo);
					}
				});
				contentPane.add(labelEscudo);
				break;

			case TipoItem.GUANTES:
				ImageIcon guantes = new ImageIcon(pathImagen);
				final String descripcionGuantes = descripcionItem;
				final JLabel labelGuantes = new JLabel(guantes);
				labelGuantes.setHorizontalAlignment(SwingConstants.CENTER);
				labelGuantes.setBounds(25, 254, 82, 92);
				labelGuantes.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						labelGuantes.setToolTipText(descripcionGuantes);
					}
				});
				contentPane.add(labelGuantes);
				break;

			default:
				break;
			}
		}

		ImageIcon fondoInventario = new ImageIcon(pathImagenes + "fondoInventario.jpg");
		final JLabel labelFondo = new JLabel(fondoInventario);
		labelFondo.setHorizontalAlignment(SwingConstants.CENTER);
		labelFondo.setBounds(0, 0, 400, 360);
		contentPane.add(labelFondo);
	}

}
