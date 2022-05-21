#ifndef __TERCETOS__
#define __TERCETOS__

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "declaraciones.h"

int _idxTercetos;
int _topeCola;
int _finCola;
int _topePila;

typedef struct
{
	char operador[35];
	char operando1[35];
	char operando2[35];
} _terceto;

_terceto* _tablaTercetos;
_terceto* _colaTercetos;
int* _pilaTercetos;

int _sizeTT;
int _sizeCola;
int _sizePila;

void iniciarTT();
void encolar( const char* oper, const char* op1, const char* op2 );
_terceto  desencolar();
void resetCola();
void apilar( int posicion );
int  desapilar();
int  guardarTerceto(const char* operador, const char* op1, const char* op2);
void modificarTerceto( int posicion, const char* operador, const char* op1, const char* op2 );
void verificarEspacioTT();
void verificarEspacioCola();
void verificarEspacioPila();
void volcarArchivoTT();

#endif
