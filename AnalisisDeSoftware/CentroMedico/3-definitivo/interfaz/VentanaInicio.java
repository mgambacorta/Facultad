package ventana.centroMedico;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controlador.centroMedico.CENTROMEDICO;

public class VentanaInicio extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private static VentanaInicio instancia;
	
	private final String nombreVentana = "Mesa de admisión";
	
	private JLabel tituloJL = new JLabel(CENTROMEDICO.TITULO);
	private JLabel nombreVentanaJL = new JLabel(nombreVentana);
	private JButton ingresarJB = new JButton("Ingreso de datos");
	private JButton informesJB = new JButton("Informes");
	private JButton salirJB = new JButton("Salir");
	
	private VentanaInicio(){
		JPanel pantalla = new Pantalla();
		
		setSize(CENTROMEDICO.ALTO, CENTROMEDICO.ANCHO);
		setTitle(CENTROMEDICO.TITULO + " - " + nombreVentana);
		add(pantalla);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public static VentanaInicio getInstancia() {
		if(instancia == null)
			instancia = new VentanaInicio();
		
		return instancia;
	}
	
	private class Pantalla extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public Pantalla() {
			setLayout(null);
			
			tituloJL.setBounds(170, 64, 320, 32);
			tituloJL.setFont(new Font("Serif", Font.PLAIN, 22));
			nombreVentanaJL.setBounds(240, 96, 256, 32);
			nombreVentanaJL.setFont(new Font("Serif", Font.PLAIN, 18));
			
			ingresarJB.setBounds(192, 192, 256, 32);
			informesJB.setBounds(192,  256, 256, 32);
			salirJB.setBounds(192, 320, 256, 32);
			
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
				public void actionPerformed(ActionEvent e) {
					VentanaIngreso.getInstancia().setVisible(true);
					VentanaInicio.getInstancia().setVisible(false);
				}
			});
			
			informesJB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					VentanaInformes.getInstancia().setVisible(true);
					VentanaInicio.getInstancia().setVisible(false);
				}
			});
			
			salirJB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					cerrarVentana();
				}
			});
			
			add(tituloJL);
			add(nombreVentanaJL);
			add(ingresarJB);
			add(informesJB);
			add(salirJB);
		}
	}
	
	private void cerrarVentana() {
		VentanaIngreso.getInstancia().dispose();
		VentanaIngresoPaciente.getInstancia().dispose();
		VentanaIngresoSituacion.getInstancia().dispose();
		VentanaIngresoMedico.getInstancia().dispose();
		VentanaInformes.getInstancia().dispose();
		VentanaInformesPacientesXMedico.getInstancia().dispose();
		VentanaInformesEnfermedadesXMedico.getInstancia().dispose();
		VentanaInicio.getInstancia().dispose();
	}
}
