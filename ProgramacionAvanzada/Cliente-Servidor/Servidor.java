package clienteservidor;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	public static void main(String[] args) {
		new Servidor();
	}
	
	Servidor() {
		try {
			ServerSocket server = new ServerSocket(10000);
			
			for(int i = 1; i <= 3; i++) {
				Socket cliente = server.accept();
				DataOutputStream out = new DataOutputStream(cliente.getOutputStream());
				out.writeUTF("Hola cliente " + i);
				cliente.close();
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
