package ventana.centroMedico;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controlador.centroMedico.CENTROMEDICO;

public class VentanaIngresoPaciente extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private static VentanaIngresoPaciente instancia;
	
	private final String NOMBRE_VENTANA = "Ingresar datos del paciente";
	private final String INGRESAR_NUEVO = "Se han guardado los datos del paciente correctamente, ¿Desea ingresar otro?";
	
	private JLabel tituloJL = new JLabel(CENTROMEDICO.TITULO);
	private JLabel nombreVentanaJL = new JLabel(NOMBRE_VENTANA);
	private JLabel codPacienteJL = new JLabel("Codigo del paciente:");
	private JLabel nomPacienteJL = new JLabel("Nombre del paciente:");
	private JLabel mensajeJL = new JLabel("");
	private JLabel codPacienteAyuda = new JLabel("?");
	private JLabel nomPacienteAyuda = new JLabel("?");
	private JTextField codPacienteJTF = new JTextField();
	private JTextField nomPacienteJPF = new JTextField();
	private JButton ingresarJB = new JButton("Ingresar");
	private JButton volverJB = new JButton("Volver");
	
	private VentanaIngresoPaciente(){
		JPanel pantalla = new Pantalla();
		
		setSize(CENTROMEDICO.ALTO, CENTROMEDICO.ANCHO);
		setTitle(CENTROMEDICO.TITULO + " - " + NOMBRE_VENTANA);
		add(pantalla);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public static VentanaIngresoPaciente getInstancia() {
		if(instancia == null)
			instancia = new VentanaIngresoPaciente();
		
		return instancia;
	}
	
	private class Pantalla extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public Pantalla() {
			setLayout(null);
			
			tituloJL.setBounds(170, 64, 320, 32);
			tituloJL.setFont(new Font("Serif", Font.PLAIN, 22));
			nombreVentanaJL.setBounds(180, 96, 320, 32);
			nombreVentanaJL.setFont(new Font("Serif", Font.PLAIN, 18));
			
			codPacienteJL.setBounds(128, 160, 192, 32);
			nomPacienteJL.setBounds(128, 192, 192, 32);
			
			codPacienteJTF.setBounds(288, 165, 192, 24);
			nomPacienteJPF.setBounds(288, 197, 192, 24);
			
			mensajeJL.setBounds(140, 245, 360, 32);
			mensajeJL.setForeground(Color.RED);
			
			codPacienteAyuda.setBounds(490, 168, 16, 16);
			codPacienteAyuda.setToolTipText(CENTROMEDICO.COD_PACIENTE_AYUDA);
			codPacienteAyuda.setBackground(Color.LIGHT_GRAY);
			codPacienteAyuda.setHorizontalAlignment(JLabel.CENTER);
			codPacienteAyuda.setOpaque(true);
			
			nomPacienteAyuda.setBounds(490, 200, 16, 16);
			nomPacienteAyuda.setToolTipText(CENTROMEDICO.NOM_PACIENTE_AYUDA);
			nomPacienteAyuda.setBackground(Color.LIGHT_GRAY);
			nomPacienteAyuda.setHorizontalAlignment(JLabel.CENTER);
			nomPacienteAyuda.setOpaque(true);
			
			ingresarJB.setBounds(192, 304, 256, 32);
			volverJB.setBounds(192,  356, 256, 32);

			addWindowListener(new WindowListener() {

				@Override
				public void windowActivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void windowClosing(WindowEvent e) {
					cerrarVentana();
							
				}

				@Override
				public void windowDeactivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowDeiconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowIconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowOpened(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
							
			});
			
			ingresarJB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					String codPac = codPacienteJTF.getText();
					String nomPac = nomPacienteJPF.getText();
					
					try {
						CENTROMEDICO.ingresarPaciente(codPac,nomPac);
						
						int opcion = JOptionPane.showConfirmDialog(null, INGRESAR_NUEVO, NOMBRE_VENTANA, JOptionPane.YES_NO_OPTION);
						
						if(opcion == JOptionPane.NO_OPTION) {
							cerrarVentana();
						}
						
						resetearVentana();
					} catch (Exception e) {
						mensajeJL.setText("<html>" + e.getMessage() + "</html>");
					}
				}
			});
			
			volverJB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					cerrarVentana();
				}
			});
			
			add(codPacienteJL);
			add(nomPacienteJL);
			add(codPacienteJTF);
			add(nomPacienteJPF);
			add(mensajeJL);
			add(codPacienteAyuda);
			add(nomPacienteAyuda);
			add(tituloJL);
			add(nombreVentanaJL);
			add(ingresarJB);
			add(volverJB);
		}
	}
	
	private void resetearVentana() {
		codPacienteJTF.setText("");
		nomPacienteJPF.setText("");
		mensajeJL.setText("");
	}
	
	private void cerrarVentana() {
		resetearVentana();
		VentanaIngresoPaciente.getInstancia().setVisible(false);
		VentanaIngreso.getInstancia().setVisible(true);
	}
}
