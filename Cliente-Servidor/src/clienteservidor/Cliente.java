package clienteservidor;

import java.io.DataInputStream;
import java.net.Socket;

public class Cliente {
	
	public static void main(String[] args) {
		new Cliente();
	}
	
	Cliente() {
		try {
			Socket cliente = new Socket("localhost", 10000);
			DataInputStream in = new DataInputStream(cliente.getInputStream());
			System.out.println(in.readUTF());
			cliente.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
