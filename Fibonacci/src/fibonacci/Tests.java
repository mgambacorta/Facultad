package fibonacci;


public class Tests {
	public static void main(String[] args) {
		System.out.println(Fibonacci.recursivo(0));
		System.out.println(Fibonacci.recursivo(1));
		System.out.println(Fibonacci.recursivo(2));
		System.out.println(Fibonacci.recursivo(3));
		System.out.println(Fibonacci.recursivo(4));
		System.out.println(Fibonacci.recursivo(5));
		System.out.println(Fibonacci.recursivo(6));
		System.out.println(Fibonacci.recursivo(7));
		System.out.println(Fibonacci.recursivo(-1));
		
		System.out.println("Cambie de metodo");
		
		System.out.println(Fibonacci.noRecursivo(0));
		System.out.println(Fibonacci.noRecursivo(1));
		System.out.println(Fibonacci.noRecursivo(2));
		System.out.println(Fibonacci.noRecursivo(3));
		System.out.println(Fibonacci.noRecursivo(4));
		System.out.println(Fibonacci.noRecursivo(5));
		System.out.println(Fibonacci.noRecursivo(6));
		System.out.println(Fibonacci.noRecursivo(7));
		System.out.println(Fibonacci.noRecursivo(-1));
		System.out.println(Fibonacci.noRecursivo(1000));
		
	}
}
