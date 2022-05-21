#include <time.h>
#include <math.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>
#include <omp.h>
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

  double mypi, pi, h, x, aux, sum;

  n=N;
  
  h = 1.0 / (double) n;
  sum = 0.0;

#pragma omp parallel for reduction(+:sum) private(x, i)
  for(i=1;i<=n;i++)
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
  double pi;
  
  tiempoInicial( "PI (OpenMP) ");
  
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
