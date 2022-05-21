#ifndef __ASMGEN__
#define __ASMGEN__

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "declaraciones.h"
#include "tercetos.h"
#include "tablaSimbolos.h"

typedef struct 
{
	char str[255];
} _linea;

_linea* _sentencias;
int _idxSent;
int _sizeSent;
char _buffLnea[255];

void generarAssembler();
void tercetosToAssembler();
void grabarSalto( int idx );
void grabarOperBin( int idx );
void grabarPrint( int idx );
void grabarRead( int idx );
void grabarComparacion( int idx );
void grabarAsignacion( int idx );
void grabarVariable( int idx );
void guardarLinea();
void printHeader();
void printCodigo();
void verificarEspacioSent();
void cambiaOpSalto( char* destino, const char* origen );
#endif
