#ifndef __TABLA_SIMBOLOS__
#define __TABLA_SIMBOLOS__

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "declaraciones.h"

typedef struct
{
	char nombre[36];
	char tipoDato[31];
	char valor[36];
	int longitud;
} _simbolo;

_simbolo* _tablaSimbolos;
_simbolo* _listaDeclaraciones;

int _idxTipos;
int _idxVariables;
int _idxTablaSimbolos;
int _sizeTS;
int _sizeLD;

void iniciarTS();
void enlistarTipo(const char* dato);
void enlistarId(const char* dato);
void volcarDeclaracionTS();
void guardarEnTablaCte(const char* dato, const char* tipo);
void getNombreCteString( char* destino, const char* origen );
void guardarVarASM(const char* dato);
int  buscarEnTabla( const char* dato );
void verificarDeclaracion( const char* id, int tipo );
void verificarEspacioTS();
void verificarEspacioLD( int indiceEnUso );
void volcarArchivoTS();

#endif
