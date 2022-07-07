package ventana.centroMedico;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controlador.centroMedico.CENTROMEDICO;

public class VentanaIngresoMedico extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private static VentanaIngresoMedico instancia;

	private final String NOMBRE_VENTANA = "Ingresar datos del medico";
	private final String INGRESAR_NUEVO = "Se han guardado los datos del Medico correctamente, ¿Desea ingresar otro?";
	
	private JLabel tituloJL = new JLabel(CENTROMEDICO.TITULO);
	private JLabel nombreVentanaJL = new JLabel(NOMBRE_VENTANA);
	private JLabel codMedicoJL = new JLabel("Codigo del medico:");
	private JLabel nomMedicoJL = new JLabel("Nombre del medico:");
	private JLabel espMedicoJL = new JLabel("Especialización del medico:");
	private JLabel codMedicoAyuda = new JLabel("?");
	private JLabel nomMedicoAyuda = new JLabel("?");
	private JLabel mensajeJL = new JLabel("");
	private JTextField codMedicoJTF = new JTextField();
	private JTextField nomMedicoJTF = new JTextField();
	private JComboBox<String> espMedicoJTF = new JComboBox<String>(CENTROMEDICO.ESPECIALIDADES);
	private JButton ingresarJB = new JButton("Ingresar");
	private JButton volverJB = new JButton("Volver");
	
	private VentanaIngresoMedico(){
		JPanel pantalla = new Pantalla();
		setSize(CENTROMEDICO.ALTO, CENTROMEDICO.ANCHO);
		setTitle(CENTROMEDICO.TITULO + " - " + NOMBRE_VENTANA);
		add(pantalla);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// Declara el tiempo de respuesta del mouse sobre la etiqueta de ayuda.
		javax.swing.ToolTipManager.sharedInstance().setInitialDelay(10);
	}
	
	public static VentanaIngresoMedico getInstancia() {
		if(instancia == null)
			instancia = new VentanaIngresoMedico();
		
		return instancia;
	}
	
	private static void mostrarMensaje(JLabel label, String mensaje) {
		label.setText("<html>" + mensaje + "</html>");
	}
	
	
	private class Pantalla extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public Pantalla() {
			setLayout(null);
			
			tituloJL.setBounds(170, 64, 320, 32);
			tituloJL.setFont(new Font("Serif", Font.PLAIN, 22));
			nombreVentanaJL.setBounds(190, 96, 320, 32);
			nombreVentanaJL.setFont(new Font("Serif", Font.PLAIN, 18));
			
			codMedicoJL.setBounds(128, 160, 192, 32);
			nomMedicoJL.setBounds(128, 192, 192, 32);
			espMedicoJL.setBounds(92, 224, 224, 32);
			
			codMedicoJTF.setBounds(288, 165, 192, 24);
			nomMedicoJTF.setBounds(288, 197, 192, 24);
			espMedicoJTF.setBounds(288, 229, 192, 24);
			
			codMedicoAyuda.setBounds(490, 168, 16, 16);
			codMedicoAyuda.setToolTipText(CENTROMEDICO.COD_MEDICO_AYUDA);
			codMedicoAyuda.setBackground(Color.LIGHT_GRAY);
			codMedicoAyuda.setHorizontalAlignment(JLabel.CENTER);
			codMedicoAyuda.setOpaque(true);
			
			nomMedicoAyuda.setBounds(490, 200, 16, 16);
			nomMedicoAyuda.setToolTipText(CENTROMEDICO.NOM_MEDICO_AYUDA);
			nomMedicoAyuda.setBackground(Color.LIGHT_GRAY);
			nomMedicoAyuda.setHorizontalAlignment(JLabel.CENTER);
			nomMedicoAyuda.setOpaque(true);
			
			mensajeJL.setBounds(100, 260, 460, 32);
			mensajeJL.setForeground(Color.RED);
			
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
					try {
						String codigoMedico = codMedicoJTF.getText();
						String nombreMedico = nomMedicoJTF.getText();
						String especialidadMedico = CENTROMEDICO.ESPECIALIDADES[espMedicoJTF.getSelectedIndex()];
						
						CENTROMEDICO.ingresarMedico(codigoMedico, nombreMedico, especialidadMedico);
							
						int opcion = JOptionPane.showConfirmDialog(null, INGRESAR_NUEVO, NOMBRE_VENTANA, JOptionPane.YES_NO_OPTION);
						
						if(opcion == JOptionPane.NO_OPTION) {
							cerrarVentana();
						}
							
						resetearVentana();
					} catch(Exception e) {
						mostrarMensaje(mensajeJL, e.getMessage());
					}
				}
			});
			
			volverJB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					resetearVentana();
					cerrarVentana();
				}
			});
			
			add(codMedicoJL);
			add(nomMedicoJL);
			add(espMedicoJL);
			add(codMedicoJTF);
			add(nomMedicoJTF);
			add(espMedicoJTF);
			add(codMedicoAyuda);
			add(nomMedicoAyuda);
			add(mensajeJL);
			add(tituloJL);
			add(nombreVentanaJL);
			add(ingresarJB);
			add(volverJB);
		}
	}
	
	private void resetearVentana() {
		codMedicoJTF.setText("");
		nomMedicoJTF.setText("");
		mensajeJL.setText("");
	}
	
	private void cerrarVentana() {
		resetearVentana();
		VentanaIngresoMedico.getInstancia().setVisible(false);
		VentanaIngreso.getInstancia().setVisible(true);
	}
}
