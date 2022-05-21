package fibonacci;

public class Fibonacci {
	public static int recursivo(int num) {
		if(num <= 0)
			return 0;
		
		if( num == 1 )
			return 1;
		
		return recursivo(num - 1) + recursivo(num - 2); 
	}
	
	public static int noRecursivo(int num) {
		int primero = 0;
		int segundo = 1;
		int resultado = 0;
		
		if( num == 0 || num == 1 )
			return num;
		
		for(;num > 1;num--) {
			resultado = primero + segundo;
			primero = segundo;
			segundo = resultado;
		}
		
		return resultado;
	}
}
