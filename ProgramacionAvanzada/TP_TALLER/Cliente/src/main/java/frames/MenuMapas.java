package frames;

import mensajeria.Comando;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cliente.Cliente;

import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JLayeredPane;

public class MenuMapas extends JFrame {

	private JPanel contentPane;

	public MenuMapas(final Cliente cliente) {

		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(MenuJugar.class.getResource("/cursor.png")).getImage(), new Point(0, 0),
				"custom cursor"));

		setTitle("Elegir Mapa");
		setBounds(100, 100, 450, 300);

		// En caso de cerrar
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				synchronized (cliente) {
					cliente.setAccion(Comando.SALIR);
					cliente.notify();
				}
				dispose();
			}
		});

		// Panel
		setTitle("WOME - Elegir Mapa");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, 444, 271);
		contentPane.add(layeredPane);

		// Mapa Aris
		JLabel lblAris = new JLabel("Aris");
		lblAris.setBounds(204, 129, 32, 23);
		layeredPane.add(lblAris, new Integer(2));
		lblAris.setForeground(Color.WHITE);
		lblAris.setFont(new Font("Tahoma", Font.PLAIN, 15));

		// Mapa Aubenor
		JLabel lblAubenor = new JLabel("Aubenor");
		lblAubenor.setBounds(191, 72, 66, 23);
		layeredPane.add(lblAubenor, new Integer(2));
		lblAubenor.setForeground(Color.WHITE);
		lblAubenor.setFont(new Font("Tahoma", Font.PLAIN, 15));

		// Mapa Eodrim
		JLabel lblEodrim = new JLabel("Eodrim");
		lblEodrim.setBounds(198, 192, 53, 23);
		layeredPane.add(lblEodrim, new Integer(2));
		lblEodrim.setForeground(Color.WHITE);
		lblEodrim.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JButton btnAubenor = new JButton("");
		btnAubenor.setBounds(148, 72, 143, 23);
		layeredPane.add(btnAubenor, new Integer(1));
		btnAubenor.setFocusable(false);
		btnAubenor.setIcon(new ImageIcon(MenuMapas.class.getResource("/frames/BotonMenu.png")));

		JButton btnEodrim = new JButton("");
		btnEodrim.setBounds(148, 192, 143, 23);
		layeredPane.add(btnEodrim, new Integer(1));
		btnEodrim.setFocusable(false);
		btnEodrim.setEnabled(false);
		btnEodrim.setIcon(new ImageIcon(MenuMapas.class.getResource("/frames/BotonMenu.png")));
		btnEodrim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized (cliente) {
					cliente.getPaquetePersonaje().setMapa(3);
					cliente.notify();
				}
				dispose();
			}
		});

		btnEodrim.setEnabled(false);

		JButton btnAris = new JButton("");
		btnAris.setBounds(148, 130, 143, 23);
		layeredPane.add(btnAris, new Integer(1));
		btnAris.setFocusable(false);
		btnAris.setIcon(new ImageIcon(MenuMapas.class.getResource("/frames/BotonMenu.png")));
		btnAris.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized (cliente) {
					cliente.getPaquetePersonaje().setMapa(2);
					cliente.notify();
				}
				dispose();
			}
		});

		btnAris.setEnabled(false);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 444, 271);
		layeredPane.add(lblBackground, new Integer(0));
		lblBackground.setIcon(new ImageIcon(MenuMapas.class.getResource("/frames/menuBackground.jpg")));
		btnAubenor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized (cliente) {
					cliente.getPaquetePersonaje().setMapa(1);
					cliente.notify();
				}
				dispose();
			}
		});
	}
}
