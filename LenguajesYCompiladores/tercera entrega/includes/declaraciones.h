#ifndef __DECLARACIONES__
#define __DECLARACIONES__

#define ARCH_TABLA_SIMBOLOS "salidas/ts.txt"
#define ARCH_TABLA_TERCETOS "salidas/intermedia.txt"
#define ARCH_ASSEMBLER      "salidas/Final.asm"

FILE *ts;
FILE *inter;
FILE* fpAssm;

#define BLOQUE_ALOCADO	50

#define TRUE		1
#define FALSE		0
#define NO_EXISTE	-1

#define ID_NUMERICO 11
#define ID_STRING 	12

#define COMPILACION_OK			0
#define ERROR_PARAMETROS		1000
#define ERROR_ARCH_INVALIDO		1001
#define ERROR_SINTACTICO		1002
#define ERROR_MALLOC			1003
#define ERROR_RANGO				1004
#define ERROR_CARACT_INVALIDO	1005
#define ERROR_DOBLE_DECLARACION	1006
#define ERROR_VAR_NO_DECLARADA	1007
#define ERROR_ID_NO_NUMERICO	1008
#define ERROR_ID_NO_STRING		1009

#define WARN_TIPO_SIN_ID		2001
#define WARN_ID_SIN_TIPO		2002

int yylex();
void enviarLinea(int nLinea);
void terminarPrograma(int codigo);
void lanzarError(int codError, const char* parametro, int terminar);
void invertirOperador();
void idxToChar( int idx );

#endif
