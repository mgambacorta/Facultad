package thread;

public class MiThread extends Thread {
	
	public MiThread(String str) {
		super(str);
	}
	
	public void run() {
		for(int i = 0; i < 20; i++) {
			System.out.println(i + " " + super.getName());
		}
		
		System.out.println("Termino el thread " + super.getName());
	}
}
