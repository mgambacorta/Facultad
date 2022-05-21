#include <omp.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <string.h>
#include <mpi.h>
#include "comun.h"

//----------------------------------------------------------------------

int main (int argc, char *argv[]) 
{
  int myid, numprocs;
  int tag=1;
  char mensaje[100];
  MPI_Status status;
  
  tiempoInicial( "HELLO (Mpi)");
  
  MPI_Init( &argc, &argv );
  
  MPI_Comm_rank( MPI_COMM_WORLD, &myid );
  MPI_Comm_size( MPI_COMM_WORLD, &numprocs );

  if( myid != 0 )
  {
	  int dest = 0;
      sprintf( mensaje, "Hello World from thread %d to %d", myid, numprocs );	
 
	  MPI_Send(&mensaje, strlen(mensaje)+1, MPI_CHAR, dest, tag, MPI_COMM_WORLD);
  }
  else
  {
	for(int i=1; i<numprocs; i++)
	{
	  MPI_Recv(&mensaje, 100, MPI_CHAR, i, tag, MPI_COMM_WORLD, &status);
	  printf( "%s\n", mensaje );
	}   
    tiempoFinal(); 
  }
 
  MPI_Finalize();
  
  return(0);
}

//----------------------------------------------------------------------
