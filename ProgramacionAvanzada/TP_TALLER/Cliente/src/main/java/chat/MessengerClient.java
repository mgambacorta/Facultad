package chat;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import chat.VentanaChat;

import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import mensajeria.PaqueteMensaje;
import mensajeria.PaquetePersonaje;
import cliente.Cliente;
import com.google.gson.Gson;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MessengerClient extends Thread {

	private String username;	
	private int idPersonaje;
	private JPanel contentPane;
	private JList<String> listUsuarios;
	private Map<Integer, VentanaChat> chatsAbiertos;
	private JLabel lblUsuarios;
	private JFrame VC;
	private Cliente cliente;
	private JTextField textFieldDifusion;
	private JTextArea textAreaDifusion;
	private final Gson gson = new Gson();
	private VentanaChat ventanaChat;
	
	public MessengerClient(Cliente cliente) {
		this.cliente = cliente;
		PaquetePersonaje paquetePersonaje = cliente.getPaquetePersonaje();
		this.username = paquetePersonaje.getNombre();
		this.idPersonaje = paquetePersonaje.getId();
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void crearVentanaCliente() {
		chatsAbiertos = new HashMap<Integer, VentanaChat>();
		VC = new JFrame();
		
		try
		{
		   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		   e.printStackTrace();
		}
		
		VC.setTitle("Chat");
		VC.setResizable(false);
		VC.setBounds(100, 100, 379, 600);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		VC.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 0, 373, 181);
		contentPane.add(scrollPane_1);
		
		textAreaDifusion = new JTextArea();
		textAreaDifusion.setEditable(false);
		scrollPane_1.setViewportView(textAreaDifusion);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(0, 217, 373, 322);
		contentPane.add(scrollPane);
		
		listUsuarios = new JList<String>();
		listUsuarios.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				seleccionaDobleClickChat(arg0);
			}
		});
		scrollPane.setViewportView(listUsuarios);

		lblUsuarios = new JLabel();
		lblUsuarios.setBounds(0, 464, 373, 14);
		contentPane.add(lblUsuarios);
		
		textFieldDifusion = new JTextField();
		textFieldDifusion.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					PaqueteMensaje mensaje = new PaqueteMensaje(idPersonaje, textFieldDifusion.getText());
					try {
						cliente.getSalida().writeObject(gson.toJson(mensaje));
						cliente.getSalida().flush();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					textFieldDifusion.setText("");
				}
			}
		});
		textFieldDifusion.setBounds(0, 182, 281, 25);
		contentPane.add(textFieldDifusion);
		textFieldDifusion.setColumns(10);
		
		JButton btnDifusion = new JButton("Difundir");
		btnDifusion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaqueteMensaje mensaje = new PaqueteMensaje(idPersonaje, textFieldDifusion.getText());
				try {
					cliente.getSalida().writeObject(gson.toJson(mensaje));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				textFieldDifusion.setText("");
			}
		});
		btnDifusion.setBounds(276, 182, 97, 25);
		contentPane.add(btnDifusion);
		
		setUsuariosEnLista();

	}
	
	public void setVisible() {
		VC.setVisible(true);
	}
	
	public void setUsuariosEnLista() {
		DefaultListModel<String> modeloLista = new DefaultListModel<String>();
		String txtUsuarios;

		for (PaquetePersonaje pj : cliente.getJuego().getEscuchaMensajes().getPersonajesConectados().values()) {
			if(!pj.getNombre().equals(this.username)) {
				modeloLista.addElement(pj.getId() + "-" + pj.getNombre());
			}
		}
		txtUsuarios = modeloLista.isEmpty() ? "No hay nadie conectado" : "Cantidad de Usuarios Conectados: " + modeloLista.getSize();
		listUsuarios.setModel(modeloLista);
		lblUsuarios.setText(txtUsuarios);
	}
	
	private void seleccionarElementoLista() {
		if(!listUsuarios.isSelectionEmpty()) {
			String[] datosUsuario = listUsuarios.getSelectedValue().split("-");
			getChatWindow(Integer.parseInt(datosUsuario[0]), datosUsuario[1]);
		}
	}

	private void seleccionaDobleClickChat(MouseEvent me) {
		if(me.getClickCount() == 2)
			seleccionarElementoLista();
	}
	
	public VentanaChat getChatWindow(int idUser, String username) {
    	if (!chatsAbiertos.containsKey(idUser)) {
    		chatsAbiertos.put(idUser, openChatWindow(idUser, username));
    	}
    	ventanaChat.setVisible(true);
    	return chatsAbiertos.get(idUser);
    }
	
	private VentanaChat openChatWindow(int idUser, String username) {
		ventanaChat = new VentanaChat(this, idUser, username);
		return ventanaChat;
	}

	protected synchronized void enviarMensaje(String texto, int idDestinatario) throws IOException {
		PaqueteMensaje msg = new PaqueteMensaje(idPersonaje, idDestinatario, texto);
		cliente.getSalida().writeObject(gson.toJson(msg));
		cliente.getSalida().flush();
	}
	
	public void recibirMensaje(PaqueteMensaje paqueteMensaje, String emisor) {
		if(paqueteMensaje.esDifusion()) {
			textAreaDifusion.append(emisor + " > " + paqueteMensaje.getContenido() + "\n");
		} else {
			getChatWindow(paqueteMensaje.getIdEmisor(), emisor).recibirMensaje(emisor, paqueteMensaje.getContenido());
		}
	}
}