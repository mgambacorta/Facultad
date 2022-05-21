
#ifndef __COMUN_H__
#define __COMUN_H__

#include "mmio.h"

float **fMatrizA;

int iFilas, iColumnas, iNNZ;

void tiempoInicial( const char * );

void tiempoFinal( void );

int iCargarMatriz( void );

int iLiberarMatriz( float **fMatriz );


#endif /* __COMUN_H__ */
