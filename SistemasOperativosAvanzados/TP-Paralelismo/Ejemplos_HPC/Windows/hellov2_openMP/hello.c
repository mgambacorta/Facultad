#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <omp.h>

//----------------------------------------------------------------------

void tomarTiempo()
{
  static int iBandera = 1;
  static double dTiempoInicial, dTiempoFinal;
  struct timeval tv;
  struct tm *ptrLocaltime;
  time_t tiempo;
  
  gettimeofday(&tv,NULL);

  // Si llamo a la funcion una vez.
  if( iBandera == 1 )
  {
	  iBandera = 0;
	  
	  dTiempoInicial = tv.tv_sec + tv.tv_usec/1000000.0;
	  
	  tiempo = time(NULL);
	  ptrLocaltime = localtime( &tiempo );
	  
	  printf("# HELLO (Open MP) %04d/%02d/%02d %02d:%02d:%02d\n", ptrLocaltime->tm_year+1900, ptrLocaltime->tm_mon+1, ptrLocaltime->tm_mday, ptrLocaltime->tm_hour, ptrLocaltime->tm_min, ptrLocaltime->tm_sec );
  }
  else
  {
	  // Si llamo a la funcion la segunda vez.
	  iBandera = 1;
	  
	  dTiempoFinal = tv.tv_sec + tv.tv_usec/1000000.0;
	  
	  printf("# Tiempo total: %0.3f [ms]\n", (dTiempoFinal - dTiempoInicial) * 1000 );
  }
}

//----------------------------------------------------------------------

void funA( int numprocs, int iThreadId, int segundos )
{	
	printf( "Hola Mundo desde funA thead %d to %d, %ds\n", iThreadId, numprocs, segundos );
	
	sleep( segundos );
}

//----------------------------------------------------------------------

void funB( int numprocs, int iThreadId, int segundos )
{
	printf( "Hola Mundo desde funB thead %d to %d, %ds\n", iThreadId, numprocs, segundos );
	
	sleep( segundos );
}

//----------------------------------------------------------------------

void funC( int numprocs, int iThreadId, int segundos )
{
	printf( "Hola Mundo desde funC thead %d to %d, %ds\n", iThreadId, numprocs, segundos );
	
	sleep( segundos );
}

//----------------------------------------------------------------------

void funD( int numprocs, int iThreadId, int segundos )
{
	printf( "Hola Mundo desde funD thead %d to %d, %ds\n", iThreadId, numprocs, segundos );
	
	sleep( segundos );
}

//----------------------------------------------------------------------

void funE( int numprocs, int iThreadId, int segundos )
{
	printf( "Hola Mundo desde funE thead %d to %d, %ds\n", iThreadId, numprocs, segundos );
	
	sleep( segundos );
}

//----------------------------------------------------------------------

void funF( int numprocs, int iThreadId, int segundos )
{
	printf( "Hola Mundo desde funF thead %d to %d, %ds\n", iThreadId, numprocs, segundos );
	
	sleep( segundos );
}

//----------------------------------------------------------------------

void funG( int numprocs, int iThreadId, int segundos )
{
	printf( "Hola Mundo desde funG thead %d to %d, %ds\n", iThreadId, numprocs, segundos );
	
	sleep( segundos );
}

//----------------------------------------------------------------------


int main (int argc, char *argv[]) 
{
  int numprocs, myid;
  
  tomarTiempo();
  
  #pragma omp parallel private( myid, numprocs ) 
  {
    myid = omp_get_thread_num();
    numprocs = omp_get_num_threads();
    
    #pragma omp sections 
    {
      #pragma omp section
      {
        funA( numprocs, myid, 10 );
      }
      #pragma omp section
      {
        funB( numprocs, myid, 20 );
      }
      #pragma omp section
      {
        funC( numprocs, myid, 30 );
      }
      #pragma omp section
      {
        funD( numprocs, myid, 40 );
      }
    }
  
    #pragma omp sections
    {
      #pragma omp section
	  {
        funE( numprocs, myid, 20 );
	  }
	  #pragma omp section
	  {
	    funF( numprocs, myid, 30 );
	  }
    }
    #pragma omp single
      funG( numprocs, myid, 20 );
  }


  tomarTiempo();
		
  return 0;
}

//----------------------------------------------------------------------
