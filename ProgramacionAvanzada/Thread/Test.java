package thread;

import java.net.Socket;

public class Test {
	public static void main(String[] args) {
		new MiThread("Mariano").start();
		new MiThread("Lautaro").start();
		
	}
}
