#include <time.h>
#include <math.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>
#include <mpi.h>
#include "comun.h"

//----------------------------------------------------------------------

#define N 500000   // Número de deltas.
#define I     1   // Número de iteraciones.
//----------------------------------------------------------------------

double f(double a)
{
  return (4.0 / (1.0 + a * a));
}

//----------------------------------------------------------------------

double dCalculoPI()
{
  int n, i, myid, numprocs;
  double mypi, pi, sum, h, x;

  MPI_Comm_size( MPI_COMM_WORLD, &numprocs );
  MPI_Comm_rank( MPI_COMM_WORLD, &myid );
  
  int tag=1;
  
  if( myid == 0 )
  { 
    n = N;
  }

  // El proceso 0 enviara el intervao a todos los procesos.
  MPI_Bcast( &n, 1, MPI_INT, 0, MPI_COMM_WORLD );
  
  h = 1.0 / (double) n;
  sum= 0.0;

  for(i=myid+1;i<=n;i+=numprocs)
  {
    x = h * ((double) i - 0.5);
    sum += f(x);
  }
  mypi = h * sum;

  printf("Proceso: %d of %d, mypi parcial: %.16f \n", myid, numprocs, mypi );

  // suma el valor de los mypi parciales mediante reduccion al proceos con id 0.
  MPI_Reduce( &mypi, &pi, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD );
	
	  if( myid == 0 )
	  {
	     return pi;
	  }

  return -1;
}


//----------------------------------------------------------------------

int main(int argc, char *argv[])
{
  double PI25DT = 3.141592653589793238462643;
  double pi;

  tiempoInicial( "PI (MPI) ");

  MPI_Init( &argc, &argv );
    
  
  int iCntInteracciones = 0;

  while (iCntInteracciones < I )
  {
    pi = dCalculoPI();
       
    iCntInteracciones++;
  }

  MPI_Finalize();

  if( pi > 0 )
  {
    printf("PI es aproximadamente %.16f, el error cometido es %.16f\n", pi, fabs( pi - PI25DT ));
    
    tiempoFinal();
  }

  return 0;
}

//----------------------------------------------------------------------
