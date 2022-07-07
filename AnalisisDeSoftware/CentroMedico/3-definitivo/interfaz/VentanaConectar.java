package ventana.centroMedico;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controlador.centroMedico.CENTROMEDICO;

public class VentanaConectar extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private static final String USUARIO = "centroMedico";
	private static final Integer PASSWORD = -643275276; //productoFinal2022
	private static final String ERROR_LOGIN = "Usuario y/o contraseña incorrecta.";
	
	private final String nombreVentana = "Conectar";
	
	private JLabel tituloJL = new JLabel(CENTROMEDICO.TITULO);
	private JLabel nombreVentanaJL = new JLabel(nombreVentana);
	private JLabel usuarioJL = new JLabel("Usuario:");
	private JLabel contrasenaJL = new JLabel("Contraseña:");
	private JLabel mensajeJL = new JLabel("");
	private JTextField usuarioJTF = new JTextField();
	private JPasswordField contrasenaJPF = new JPasswordField();
	private JButton ingresarJB = new JButton("Ingresar");
	private JButton cerrarJB = new JButton("Cerrar");
	
	public VentanaConectar(){
		JPanel pantalla = new Pantalla();
		
		setSize(CENTROMEDICO.ALTO, CENTROMEDICO.ANCHO);
		setTitle(CENTROMEDICO.TITULO + " - " + nombreVentana);
		add(pantalla);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static boolean puedeIngresarUsuario(String usuario, String contrasena) {
		// Verificación de usuario y contraseña correctos.
		return (USUARIO.equals(usuario) && PASSWORD.equals(contrasena.hashCode()));
	}
	
	private class Pantalla extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public Pantalla() {
			setLayout(null);
			
			tituloJL.setBounds(170, 64, 320, 32);
			tituloJL.setFont(new Font("Serif", Font.PLAIN, 22));
			nombreVentanaJL.setBounds(275, 96, 256, 32);
			nombreVentanaJL.setFont(new Font("Serif", Font.PLAIN, 18));
			usuarioJL.setBounds(208, 160, 128, 32);
			contrasenaJL.setBounds(192, 192, 96, 32);
			
			usuarioJTF.setBounds(288, 165, 144, 24);
			contrasenaJPF.setBounds(288, 197, 144, 24);
			contrasenaJPF.setEchoChar('*');
			
			mensajeJL.setBounds(200, 245, 256, 24);
			mensajeJL.setForeground(Color.RED);
			
			ingresarJB.setBounds(192, 304, 256, 32);
			cerrarJB.setBounds(192,  356, 256, 32);

			ingresarJB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(puedeIngresarUsuario(usuarioJTF.getText(), String.valueOf(contrasenaJPF.getPassword()))) {
						VentanaInicio.getInstancia().setVisible(true);
						dispose();	
					}else {
						mensajeJL.setText(ERROR_LOGIN);
					}
				}
			});
			
			cerrarJB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			add(usuarioJL);
			add(contrasenaJL);
			add(usuarioJTF);
			add(contrasenaJPF);
			add(mensajeJL);
			add(tituloJL);
			add(nombreVentanaJL);
			add(ingresarJB);
			add(cerrarJB);
		}
	}
}
