#include <time.h>
#include <math.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>
#include "comun.h"

//----------------------------------------------------------------------

#define N	500000		// Número de deltas.
#define I      1		// Número de iteraciones.

//----------------------------------------------------------------------

double f(double a)
{
  return (4.0 / (1.0 + a * a));
}

//----------------------------------------------------------------------

double dCalculoPI()
{
  int n, i;

  double mypi, pi, h, sum;
	
  h = 1.0 / (double) N;
  sum = 0.0;
  for(i=1;i<=N;i++)
  {
    double x = h * ((double) i - 0.5);
    sum += f(x);
  }
  pi = h * sum;

  return pi;
}

//----------------------------------------------------------------------

int main(int argc, char *argv[])
{
  double PI25DT = 3.141592653589793238462643;
  double pi, h, sum;
  
  tiempoInicial( "PI (secuencial) ");
  
  int iCntInteracciones = 0;
  
  while (iCntInteracciones < I )
  {
     pi = dCalculoPI();
     
    iCntInteracciones++;
  }
  
  printf("PI es aproximadamente %.16f, el error cometido es %.16f\n", pi, fabs( pi - PI25DT ) );
 
  tiempoFinal();
  
  return 0;
}

//----------------------------------------------------------------------
