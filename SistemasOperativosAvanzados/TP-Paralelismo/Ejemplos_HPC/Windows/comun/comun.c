#include <time.h>
#include <math.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>

#include "comun.h"

//--------------------------------------------------------------------------------------------------

char zMatrixOpcion[3][30]={ { "../../matrices/cavity04.mtx"   },
                            { "../../matrices/bcsstk34.mtx"   }, 
                            { "../../matrices/rkat7_mat5.mtx" } };

//--------------------------------------------------------------------------------------------------

static double dTiempoInicial, dTiempoFinal;
static int iBandera = 1;

//--------------------------------------------------------------------------------------------------

void tiempoInicial( const char *cCadena )
{
  struct timeval tv;
  struct tm *ptrLocaltime;
  time_t tiempo;

  // Si llamo a la funcion la una vez.
  if( iBandera == 1 )
  {
	  iBandera = 0;

      gettimeofday(&tv,NULL);
	  
	  dTiempoInicial = tv.tv_sec + tv.tv_usec/1000000.0;
	  
	  tiempo = time(NULL);
	  ptrLocaltime = localtime( &tiempo );
	  
	  printf("# %s %04d/%02d/%02d %02d:%02d:%02d\n", cCadena, ptrLocaltime->tm_year+1900, ptrLocaltime->tm_mon+1, ptrLocaltime->tm_mday, ptrLocaltime->tm_hour, ptrLocaltime->tm_min, ptrLocaltime->tm_sec );
  }
}

//--------------------------------------------------------------------------------------------------

void tiempoFinal( void )
{
  struct timeval tv;
  
  if(iBandera == 0 )
  {
    gettimeofday(&tv,NULL);

    // Si llamo a la funcion la segunda vez.
    iBandera = 1;
    dTiempoFinal = tv.tv_sec + tv.tv_usec/1000000.0;
    printf("# Tiempo total: %0.3f [ms]\n", (dTiempoFinal - dTiempoInicial) * 1000 );
  }
}

//--------------------------------------------------------------------------------------------------

int iCargarMatriz( void )
{
    int iOpcion;
    int iAuxFila, iAuxColumna;
    double dAuxVal;
    FILE *fArchivo;
    MM_typecode matcode;
    
    printf( "\n" );
    printf( "###########################################\n" );
    printf( "## 1 - cavity04.mtx                      ##\n" );
    printf( "## 2 - bcsstk34.mtx                      ##\n" );
    printf( "## 3 - rkat7_mat5.mtx                    ##\n" );
    printf( "###########################################\n" );
    printf( "Opcion: " );
    do{
      iOpcion=0;
      //flush(stdin);
      scanf("%d", &iOpcion ); 
//          printf(" %l, matrix elegida \n", iOpcion );

    } while( (iOpcion<1) ||  (iOpcion>3) );
        
    //return( zMatrixOpcion[iOpcion-1] );

    // Abro el archivo de la matriz
    if ((fArchivo = fopen(zMatrixOpcion[iOpcion-1], "r")) == NULL) 
       exit(1);
    
    // Leo la cabecera del archivo.
    if (mm_read_banner(fArchivo, &matcode) != 0)
    {
        fprintf( stderr, "No pudo leer la cabecera del archivo matriz %s.\n", zMatrixOpcion[iOpcion-1] );
        exit(1);
    }

    //  Filtra los formatos no soportados de matrices.
    if (mm_is_complex(matcode) && mm_is_matrix(matcode) && mm_is_sparse(matcode) )
    {
        fprintf( stderr, "Formato de matriz no soportado [%s].\n ", mm_typecode_to_str(matcode) );
        exit(1);
    }
    
    // Cargo las proporciones de la matiz .... 
    if (mm_read_mtx_crd_size(fArchivo, &iColumnas, &iFilas, &iNNZ) !=0)
    {
        fprintf( stderr, "Error leyendo proporciones de la matriz.\n " );
        exit(1);
    }
    
    // Reservo la memoria.
    fMatrizA = (float **) malloc( iFilas * iColumnas * sizeof(float));

    for(int i=0;i < iFilas; i++)
    {
        fMatrizA[i] = (float *) malloc (iColumnas * sizeof(float));
    }
    
    // Inicializa la matriz
    for(int i=0;i < iFilas; i++)
    {
        for(int j=0;j < iColumnas; j++)
        {
            fMatrizA[i][j] = 0;
        }
    }
    
    // Carga los valores en la matriz
    for (int i=0; i<iNNZ; i++)
    {
        //fscanf(f, "%d %d %lg\n", &I[i], &J[i], &val[i]);
        fscanf( fArchivo, "%d %d %lg\n", &iAuxFila, &iAuxColumna, &dAuxVal );
        fMatrizA[iAuxFila-1][iAuxColumna-1] = dAuxVal; 
    }
    
    fclose( fArchivo );

    // Muestro la matriz cargada 
    mm_write_banner(stdout, matcode);
    printf( "\%%\%%MatrixMarket [%d] [%d] con NNZ %d, tamaño total %d k.\n", iFilas, iColumnas, iNNZ, (int)(iFilas * iColumnas * sizeof(float)/1000) );

 return 1;
}

//--------------------------------------------------------------------------------------------------

int iLiberarMatriz( float **fMatriz )
{
  if( fMatriz )
  {
    for(int i=0;i < iFilas; i++)
    {
       free( fMatriz[i] );
    }
    
    free ( fMatriz );
  }

  return 1;
}

//--------------------------------------------------------------------------------------------------
