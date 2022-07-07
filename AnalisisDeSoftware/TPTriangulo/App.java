import java.util.Scanner;
import java.util.InputMismatchException;

public class App {
  private static String NO_ES_TRIANGULO_ERROR = "Los valores ingresados no corresponden a un triangulo.";
	private static String VALORES_INCORRECTOS_ERROR = "Los valores de los lados deben ser enteros mayor a 0.";
	
	public static void main(String[] args) {

        /* 1 */ 
		Scanner entrada = new Scanner(System.in);
		boolean ingresoValor = false;
		//END 1
		while(!ingresoValor) {  //2
            //3
			try {
				System.out.println("Ingrese las longitudes de los tres lados de un triángulo");
				int ladoA = entrada.nextInt();
				int ladoB = entrada.nextInt();
				int ladoC = entrada.nextInt();
			//END 3	
				if(/*4*/ladoA <= 0 || /*5*/ ladoB <= 0 || /*6*/ladoC <= 0) {
					throw new Exception(VALORES_INCORRECTOS_ERROR);//7
				}
				
				if (!esTriangulo(ladoA, ladoB, ladoC)) {//8
				  throw new Exception(NO_ES_TRIANGULO_ERROR);//9
				}
				
				if(/*10*/ladoA == ladoB && /*11*/ladoB == ladoC)
					System.out.println("El triángualo es Equilátero");//12
				else if(/*13*/ladoA != ladoB && /*14*/ladoB != ladoC && /*15*/ladoA != ladoC)
					System.out.println("El triángulo es Escaleno");//16
				else
					System.out.println("El triángulo es Isósceles");//17
				ingresoValor = true;//18
			} catch (InputMismatchException ex) {//19
                System.out.println(VALORES_INCORRECTOS_ERROR);
                entrada.nextLine();//END 19
			} catch(Exception e) {//20
				System.out.println(e.getMessage());
				entrada.nextLine();
			}//END 20
		}
		entrada.close();//21
	}//END 21
	
	public static boolean esTriangulo(int ladoA, int ladoB, int ladoC) {
	  return ladoA + ladoB > ladoC && ladoA + ladoC > ladoB && ladoB + ladoC > ladoA;
	}
}
