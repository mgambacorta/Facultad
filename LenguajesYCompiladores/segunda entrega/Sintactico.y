%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "y.tab.h"

FILE *yyin;
FILE *ts;
FILE *inter;
int _numLinea = 0;

#define ARCH_TABLA_SIMBOLOS "ts.txt"
#define ARCH_TABLA_TERCETOS "intermedia.txt"

#define BLOQUE_ALOCADO	50

#define TRUE		1
#define FALSE		0
#define NO_EXISTE	-1

/////// Declaraciones para la tabla se simbolos
int _idxTipos = 0;
int _idxVariables = 0;
int _idxTablaSimbolos = 0;

typedef struct
{
	char nombre[36];
	char tipoDato[31];
	char valor[36];
	int longitud;
} _simbolo;

_simbolo* _tablaSimbolos = NULL;
_simbolo* _listaDeclaraciones = NULL;

int _sizeTS = 0;
int _sizeLD = 0;
//////////////////////////////////////////////

/////// Declaraciones para los tercetos
int _idxTercetos = 0;
int _topeCola = 0;
int _finCola = -1;
int _topePila = -1;

typedef struct
{
	char operando[35];
	char operador1[35];
	char operador2[35];
} _terceto;

_terceto* _tablaTercetos = NULL;
int* _colaTercetos = NULL;
int* _pilaTercetos = NULL;

int _sizeTT = 0;
int _sizeCola = 0;
int _sizePila = 0;
////////////////////////////////////////

/////// Indices de No Terminales y auxiliares globales
int idxExpresion = -1;
int idxTermino = -1;
int idxFactor = -1;
int idxConstNum = -1;
int idxExpresionAnterior = -1;

char _auxOpSalto[5];
char _auxVarInList[32];
int _existeAnd = FALSE;
/////////////////////////////////////////////////////

int yylex();
void enviarLinea(int nLinea);
void terminarPrograma(int codigo);
void lanzarError(int codError, const char* parametro, int terminar);
void enlistarTipo(const char* dato);
void enlistarId(const char* dato);
void volcarDeclaracionTS();
void guardarEnTablaCte(const char* dato, const char* tipo);
int  buscarEnTabla( const char* dato );
void verificarDeclaracion( const char* id, int tipo );
void verificarEspacioTS();
void verificarEspacioLD( int indiceEnUso );
void verificarEspacioTT();
void verificarEspacioCola();
void verificarEspacioPila();
void volcarTablas();
void encolar( int posicion );
int  desencolar();
void resetCola();
void apilar( int posicion );
int  desapilar();
int  guardarTerceto(const char* operador, const char* op1, const char* op2);
void modificarTerceto( int posicion, const char* operando, const char* op1, const char* op2 );
void idxToChar( char* destino, int idx );
void invertirOperador();

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

%}

%union {
	int intval;
	double doubleval;
	char str_val[50];
}

// palabras reservadas
%token R_REPEAT
%token R_UNTIL
%token R_INT
%token R_FLOAT
%token R_STRING
%token R_PRINT
%token R_READ
%token R_IF
%token R_ELSE
%token R_ENDIF
%token R_VAR
%token R_ENDVAR
%token R_INLIST
// datos
%token ID
%token CTE_FLOAT
%token CTE_INT
%token CTE_STRING
// bloques y separadores
%token PAR_AB
%token PAR_CER
%token COR_AB
%token COR_CER
%token COMA
%token P_COMA
%token DOS_P
// operadores
%token OP_SUMA 
%token OP_RESTA
%token OP_PROD
%token OP_DIV 
%token OP_ASIG 
%token OP_IGUAL
%token OP_MAYOR
%token OP_MENOR
%token OP_MAYIG
%token OP_MENIG
%token OP_DIST
%token OP_AND
%token OP_OR
%token OP_NOT


%%
programa : blq_declaraciones blq_sentencias { volcarTablas(); }

blq_declaraciones : R_VAR lista_declaraciones R_ENDVAR;

lista_declaraciones : lista_declaraciones declaracion 
					| declaracion;

declaracion : COR_AB lista_tipos COR_CER DOS_P COR_AB lista_ids_dec COR_CER { volcarDeclaracionTS(); };

lista_tipos : lista_tipos COMA tipo { enlistarTipo($<str_val>3); }
			| tipo { enlistarTipo($<str_val>1); };
			
tipo : R_INT
	 | R_FLOAT
	 | R_STRING;

lista_ids_dec : lista_ids_dec COMA ID { enlistarId($<str_val>3); }
			  | ID { enlistarId($<str_val>1); };
		  
blq_sentencias : blq_sentencias sentencia
			   | sentencia;

sentencia : iteracion
		  | desicion
		  | escritura
		  | lectura
		  | asignacion;

iteracion : R_REPEAT {
				apilar(_idxTercetos);
			} blq_sentencias R_UNTIL PAR_AB condicion PAR_CER {
				char auxIdx[5];
				invertirOperador();
				idxToChar( auxIdx, desapilar() );
				guardarTerceto(_auxOpSalto, auxIdx, "");
				if( _existeAnd == TRUE ) // si hubo un AND que fracaso, salgo
				{
					modificarTerceto( desapilar(), NULL, auxIdx, NULL );
					_existeAnd = FALSE;
				}
			};

desicion : condicion_if blq_sentencias R_ENDIF {
				char auxIdx[5];
				idxToChar( auxIdx, _idxTercetos );
				modificarTerceto( desapilar(), NULL, auxIdx, NULL );
			};
		 | condicion_if blq_sentencias R_ELSE {
				char auxIdx[5];
				idxToChar( auxIdx, _idxTercetos + 1 );
				modificarTerceto( desapilar(), NULL, auxIdx, NULL );
				if( _existeAnd == TRUE ) // si hubo un and tengo que desapilar dos veces
				{
					modificarTerceto( desapilar(), NULL, auxIdx, NULL );
					_existeAnd = FALSE;
				}
				apilar(guardarTerceto("BI", "", ""));
			} blq_sentencias R_ENDIF {
				char auxIdx[5];
				idxToChar( auxIdx, _idxTercetos );
				modificarTerceto( desapilar(), NULL, auxIdx, NULL );
				if( _existeAnd == TRUE ) // si hubo un and tengo que desapilar dos veces
				{
					modificarTerceto( desapilar(), NULL, auxIdx, NULL );
					_existeAnd = FALSE;
				}
			};

condicion_if : R_IF PAR_AB condicion PAR_CER {
					apilar(guardarTerceto(_auxOpSalto, "", ""));
				}

escritura : R_PRINT PAR_AB ID PAR_CER {
				verificarDeclaracion($<str_val>3, ID_STRING);
				guardarTerceto("PRINT", $<str_val>3, "");
			}
		  | R_PRINT PAR_AB CTE_STRING PAR_CER {
				guardarEnTablaCte($<str_val>3, "CTE_STRING");
				guardarTerceto("PRINT", $<str_val>3, "");
			};

lectura : R_READ PAR_AB ID PAR_CER {
				verificarDeclaracion($<str_val>3, ID_STRING);
				guardarTerceto("READ", $<str_val>3, "");
			};

asignacion : asignacion_simple
		   | asignacion_multiple;

condicion : comparacion
		  | OP_NOT comparacion { invertirOperador(); }
		  | comparacion {
				apilar( guardarTerceto(_auxOpSalto, "", "") );
			} OP_AND comparacion {
				_existeAnd = TRUE;
			}
		  | comparacion {
				invertirOperador();
				apilar( guardarTerceto(_auxOpSalto, "", "") );
				invertirOperador();
			} OP_OR comparacion {
				char auxIdx[5];
				idxToChar( auxIdx, _idxTercetos + 1 );
				modificarTerceto( desapilar(), NULL, auxIdx, NULL );
			};

comparacion : expresion {
				idxExpresionAnterior = idxExpresion;
			} op_comparacion expresion {
				char auxExp1[5];
				char auxExp2[5];
				idxToChar(auxExp1, idxExpresionAnterior);
				idxToChar(auxExp2, idxExpresion);
				guardarTerceto("CMP", auxExp1, auxExp2);
			}
			| inlist;

inlist : R_INLIST PAR_AB ID { 
			verificarDeclaracion($<str_val>3, ID_NUMERICO);
			strcpy( _auxVarInList, $<str_val>3 );
		} P_COMA COR_AB lista_expr_il COR_CER PAR_CER {
			char auxIdx[5];
			strcpy(_auxOpSalto, "BI"); // Si se ejecuta es te terceto es porque ninguna expresion es igual, salto derecho a falsa
			idxToChar( auxIdx, _idxTercetos + 1 ); // El terceto que viene es el de comparacion, me lo salteo
			while( _topeCola <= _finCola )
			{
				modificarTerceto(desencolar(), NULL, auxIdx, NULL);
			}
			resetCola();
		};

lista_expr_il : lista_expr_il P_COMA expresion {
					char auxExp[5];
					idxToChar(auxExp, idxExpresion);
					guardarTerceto("CMP", _auxVarInList, auxExp);
					encolar(guardarTerceto("BEQ", "", ""));
				}
			  | expresion{
					char auxExp[5];
					int idx;
					idxToChar(auxExp, idxExpresion);
					guardarTerceto("CMP", _auxVarInList, auxExp);
					encolar(guardarTerceto("BEQ", "", ""));
				};

op_comparacion : OP_IGUAL { strcpy(_auxOpSalto, "BNE"); }
			   | OP_MAYOR { strcpy(_auxOpSalto, "BLE"); }
			   | OP_MENOR { strcpy(_auxOpSalto, "BGE"); }
			   | OP_MAYIG { strcpy(_auxOpSalto, "BLT"); }
			   | OP_MENIG { strcpy(_auxOpSalto, "BGT"); }
			   | OP_DIST { strcpy(_auxOpSalto, "BEQ"); };

asignacion_simple : ID OP_ASIG expresion {
						char auxExp[5];
						verificarDeclaracion($<str_val>1, ID_NUMERICO);
						idxToChar(auxExp, idxExpresion);
						guardarTerceto(":=", $<str_val>1, auxExp);
					};

asignacion_multiple : COR_AB lista_ids COR_CER OP_ASIG COR_AB lista_expresiones COR_CER { resetCola(); };

lista_ids : lista_ids COMA ID {
				verificarDeclaracion($<str_val>3, ID_NUMERICO);
				encolar( guardarTerceto(":=", $<str_val>3, "") );
			}
		  | ID {
				verificarDeclaracion($<str_val>1, ID_NUMERICO);
				encolar( guardarTerceto(":=", $<str_val>1, "") );
			};

lista_expresiones : lista_expresiones COMA expresion {
						char auxExp[5];
						idxToChar(auxExp, idxExpresion);
						modificarTerceto( desencolar(), NULL, NULL, auxExp );
					}
				  | expresion {
						char auxExp[5];
						idxToChar(auxExp, idxExpresion);
						modificarTerceto( desencolar(), NULL, NULL, auxExp );
					};

expresion : expresion OP_SUMA termino {
				char auxExp[5], auxTer[5];
				idxToChar(auxExp, idxExpresion);
				idxToChar(auxTer, idxTermino);
				idxExpresion = guardarTerceto("+", auxExp, auxTer);
			}
		  | expresion OP_RESTA termino {
				char auxExp[5], auxTer[5];
				idxToChar(auxExp, idxExpresion);
				idxToChar(auxTer, idxTermino);
				idxExpresion = guardarTerceto("-", auxExp, auxTer);
			}
		  | termino { idxExpresion = idxTermino; };

termino : termino OP_PROD factor {
			char auxFact[5], auxTer[5];
			idxToChar(auxFact, idxFactor);
			idxToChar(auxTer, idxTermino);
			idxTermino = guardarTerceto("*", auxTer, auxFact);
		}
		| termino OP_DIV factor {
			char auxFact[5], auxTer[5];
			idxToChar(auxFact, idxFactor);
			idxToChar(auxTer, idxTermino);
			idxTermino = guardarTerceto("/", auxTer, auxFact);
		}
		| factor { idxTermino = idxFactor; };

factor : PAR_AB expresion PAR_CER { idxFactor = idxExpresion; }
	   | constante_numerica { idxFactor = idxConstNum; }
	   | ID {
			verificarDeclaracion($<str_val>1, ID_NUMERICO);
			idxFactor = guardarTerceto($<str_val>1, "", "");
		};

constante_numerica : CTE_FLOAT {
						guardarEnTablaCte($<str_val>1, "CTE_FLOAT");
						idxConstNum = guardarTerceto($<str_val>1, "", "");
					}
				   | CTE_INT { 
						guardarEnTablaCte($<str_val>1, "CTE_INT");
						idxConstNum = guardarTerceto($<str_val>1, "", "");
					};

%%

int main(int argc,char *argv[])
{
	if( argc != 2 )
		lanzarError(ERROR_PARAMETROS, argv[0], TRUE);

	if( ( yyin = fopen(argv[1], "rt" ) ) == NULL)
		lanzarError(ERROR_ARCH_INVALIDO, argv[1], TRUE);

	yyparse();

	printf( "\n*** COMPILACION EXITOSA ***\n\n" );
	terminarPrograma(COMPILACION_OK);
}

//
// recibe errores del lexico
// 
int yyerror(void)
{
	lanzarError(ERROR_SINTACTICO, NULL, TRUE);
}

//
//Recibe el numero de linea del Lexico.l
//
void enviarLinea(int nLinea){
	_numLinea = nLinea;
}

// 
// Termina el programa liberando todos los buffers que esten abiertos
// 
void terminarPrograma(int codigo)
{
	if( yyin ) fclose(yyin);
	if( ts ) fclose(ts);
	if( inter ) fclose(inter);
	if( _tablaSimbolos ) free(_tablaSimbolos);
	if( _listaDeclaraciones ) free(_listaDeclaraciones);
	
	exit(codigo);
}

// 
// Finaliza el programa con el codigo de error correspondiente
//  
void lanzarError(int codError, const char* parametro, int terminar)
{
	if( terminar == TRUE)
		printf("\n[ERROR] en linea %d: ", _numLinea);
	else
		printf("\n[WARNING] en linea %d: ", _numLinea);
	
	switch(codError)
	{
		case ERROR_PARAMETROS:
			printf("Parametros incorrectos. Modo de ejecucion:\n\t\t%s <archivo de codigo>\n", parametro);
			break;
		case ERROR_ARCH_INVALIDO:
			printf("No fue posible abrir el archivo %s\n", parametro);
			break;
		case ERROR_SINTACTICO:
			printf("Error Sintactico\n");
			break;
		case ERROR_RANGO:
			printf("Simbolo %s definido fuera del rango permitido\n", parametro);
			break;
		case ERROR_MALLOC:
			printf("No es posible alocar memoria para la tabla de simbolos\n");
			break;
		case ERROR_CARACT_INVALIDO:
			printf("Caracter [%s] no reconocido por el Analizador Lexico\n", parametro);
			break;
		case ERROR_DOBLE_DECLARACION:
			printf("La variable \"%s\" ya fue declarada previamente\n", parametro);
			break;
		case ERROR_VAR_NO_DECLARADA:
			printf("La variable \"%s\" no fue declarada\n", parametro);
			break;
		case ERROR_ID_NO_NUMERICO:
			printf("la variable \"%s\" no es numerica\n", parametro);
			break;
		case ERROR_ID_NO_STRING:
			printf("la variable \"%s\" no es string\n", parametro);
			break;
		case WARN_TIPO_SIN_ID:
			printf("Tipo \"%s\" no corresponde a ninguna variable\n", parametro);
			break;
		case WARN_ID_SIN_TIPO:
			printf("Variable \"%s\" no corresponde a ningun tipo de dato\n", parametro);
			break;
		default:
			printf("ERROR INESPERADO (%d)\n", codError);
	}
	
	if( terminar == TRUE )
		terminarPrograma(codError);
}

// 
// Mete en la lista de declaraciones un tipo de dato
// 
void enlistarTipo(const char* dato)
{
	verificarEspacioLD(_idxTipos);

	strcpy(_listaDeclaraciones[_idxTipos].tipoDato,dato);

	_idxTipos++;
}

// 
// Mete en la lista de declaraciones una variable
// 
void enlistarId(const char* dato)
{
	verificarEspacioLD(_idxVariables);
	strcpy(_listaDeclaraciones[_idxVariables].nombre,dato);

	_idxVariables++;
}

// 
// Guarda en tabla de simbolos todos los IDs declarados
// 
void volcarDeclaracionTS()
{
	int idx;
	for (idx = 0; idx < _idxTipos || idx < _idxVariables; idx++)
	{
		if( buscarEnTabla(_listaDeclaraciones[idx].nombre) != NO_EXISTE )
			lanzarError(ERROR_DOBLE_DECLARACION,_listaDeclaraciones[idx].nombre, TRUE);

		if( idx >= _idxTipos )
		{
			lanzarError(WARN_ID_SIN_TIPO, _listaDeclaraciones[idx].nombre, FALSE);
			continue;
		}

		if( idx >= _idxVariables )
		{
			lanzarError(WARN_TIPO_SIN_ID, _listaDeclaraciones[idx].tipoDato, FALSE);
			continue;
		}

		verificarEspacioTS();

		strcpy(_tablaSimbolos[_idxTablaSimbolos].nombre, _listaDeclaraciones[idx].nombre);
		strcpy(_tablaSimbolos[_idxTablaSimbolos].tipoDato, _listaDeclaraciones[idx].tipoDato);
		strcpy(_tablaSimbolos[_idxTablaSimbolos].valor, "-");
		_tablaSimbolos[_idxTablaSimbolos].longitud = 0;
		_idxTablaSimbolos++;
	}
	
	_idxVariables = 0;
	_idxTipos = 0;
}

// 
// Guarda en tabla de simbolos un dato Constante string
// 
void guardarEnTablaCte(const char* dato, const char* tipo)
{
	if( buscarEnTabla(dato) != NO_EXISTE )
		return;

	verificarEspacioTS();

	sprintf(_tablaSimbolos[_idxTablaSimbolos].nombre, "_%s", dato);
	strcpy(_tablaSimbolos[_idxTablaSimbolos].tipoDato, tipo);
	strcpy(_tablaSimbolos[_idxTablaSimbolos].valor, dato);
	
	if( ! strcmp(tipo, "CTE_STRING")  )
		_tablaSimbolos[_idxTablaSimbolos].longitud = strlen(dato);
	else
		_tablaSimbolos[_idxTablaSimbolos].longitud = 0;

	_idxTablaSimbolos++;
}

//
// Busca si el dato recibido ya existe en la tabla de simbolos
//
int buscarEnTabla( const char* dato )
{
	int idx;
	
	for( idx = 0; idx < _idxTablaSimbolos; idx++ )
	{
		// los id los tengo que comparar por el nombre y las constantes por el valor
		if( ! strcmp(_tablaSimbolos[idx].nombre, dato) || ! strcmp(_tablaSimbolos[idx].valor, dato) )
				return idx;
	}
	return NO_EXISTE;
}

//
// Valida que la variable exista en la tabla de simbolos
// y que tenga el tipo de dato correcto
//
void verificarDeclaracion( const char* id, int tipo )
{
	int idx = buscarEnTabla( id );
	
	if( idx == NO_EXISTE )
		lanzarError(ERROR_VAR_NO_DECLARADA, id, TRUE);

	if( tipo == ID_STRING && strcmp( _tablaSimbolos[idx].tipoDato, "STRING" ) != 0 )
		lanzarError(ERROR_ID_NO_STRING, id, TRUE);
	
	if( tipo == ID_NUMERICO &&
		strcmp( _tablaSimbolos[idx].tipoDato, "INT" ) != 0 &&
		strcmp( _tablaSimbolos[idx].tipoDato, "FLOAT" ) != 0 )
			lanzarError(ERROR_ID_NO_NUMERICO, id, TRUE);
}

//
// Revisa si hay espacio en la tabla de simbolos y si no aloca 
//
void verificarEspacioTS()
{
	if( _sizeTS == 0 )
	{
		_tablaSimbolos = (_simbolo*) malloc ( sizeof(_simbolo) * BLOQUE_ALOCADO );
		if( _tablaSimbolos == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizeTS = BLOQUE_ALOCADO;
	}
	else if( _idxTablaSimbolos == _sizeTS )
	{
		_tablaSimbolos = (_simbolo*) realloc ( _tablaSimbolos, sizeof(_simbolo) * (_sizeTS + BLOQUE_ALOCADO) );
		if( _tablaSimbolos == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizeTS += BLOQUE_ALOCADO;
	}
}

//
// Revisa si hay espacio en la lista de variables declaradas y si no aloca 
//
void verificarEspacioLD( int indiceEnUso )
{
	if( _sizeLD == 0 )
	{
		_listaDeclaraciones = (_simbolo*) malloc ( sizeof(_simbolo) * BLOQUE_ALOCADO );
		if( _listaDeclaraciones == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizeLD = BLOQUE_ALOCADO;
	}
	else if( indiceEnUso == _sizeLD )
	{
		_listaDeclaraciones = (_simbolo*) realloc ( _listaDeclaraciones, sizeof(_simbolo) * (_sizeLD + BLOQUE_ALOCADO) );
		if( _listaDeclaraciones == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizeLD += BLOQUE_ALOCADO;
	}
}

//
// Revisa si hay espacio en la tabla de tercetos y si no aloca 
//
void verificarEspacioTT()
{
	if( _sizeTT == 0 )
	{
		_tablaTercetos = (_terceto*) malloc ( sizeof(_terceto) * BLOQUE_ALOCADO );
		if( _tablaTercetos == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizeTT = BLOQUE_ALOCADO;
	}
	else if( _idxTercetos == _sizeTT )
	{	
		_tablaTercetos = (_terceto*) realloc ( _tablaTercetos, sizeof(_terceto) * (_sizeTT + BLOQUE_ALOCADO) );
		if( _tablaTercetos == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizeTT += BLOQUE_ALOCADO;
	}
}

//
// Revisa si hay espacio en la cola de posiciones y si no aloca 
//
void verificarEspacioCola()
{
	if( _sizeCola == 0 )
	{
		_colaTercetos = (int*) malloc ( sizeof(int) * BLOQUE_ALOCADO );
		if( _colaTercetos == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizeCola = BLOQUE_ALOCADO;
	}
	else if( _finCola == _sizeCola )
	{
		_colaTercetos = (int*) realloc ( _colaTercetos, sizeof(_colaTercetos) + sizeof(int) * BLOQUE_ALOCADO );
		if( _colaTercetos == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizeCola += BLOQUE_ALOCADO;
	}
}

//
// Revisa si hay espacio en la pila de posiciones y si no aloca 
//
void verificarEspacioPila()
{
	if( _sizePila == 0 )
	{
		_pilaTercetos = (int*) malloc ( sizeof(int) * BLOQUE_ALOCADO );
		if( _pilaTercetos == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizePila = BLOQUE_ALOCADO;
	}
	else if( _topePila == _sizePila )
	{
		_pilaTercetos = (int*) realloc ( _pilaTercetos, sizeof(_pilaTercetos) + sizeof(int) * BLOQUE_ALOCADO );
		if( _pilaTercetos == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizePila += BLOQUE_ALOCADO;
	}
}

// 
// Abre los archivos de tabla de simbolos y de tercetos
// y escribe el contenido de los respectivos vectores
// 
void volcarTablas()
{
	int idx;
	char auxLong[11];

	if( ( ts = fopen(ARCH_TABLA_SIMBOLOS, "wt") ) == NULL )
		lanzarError(ERROR_ARCH_INVALIDO, ARCH_TABLA_SIMBOLOS, TRUE);

	fprintf(ts, " %-35s | %-15s | %-35s | %s \n","NOMBRE","TIPO","VALOR","LONGITUD");
	fprintf(ts, "---------------------------------------------------------------------------------------------------------\n");
	
	for( idx = 0; idx < _idxTablaSimbolos; idx++ )
	{
		if( _tablaSimbolos[idx].longitud != 0 )
			sprintf(auxLong, "%d", _tablaSimbolos[idx].longitud);
		else
			memset(auxLong, '\0', sizeof(auxLong));
		
		fprintf(ts, " %-35s | %-15s | %-35s | %s \n",
					_tablaSimbolos[idx].nombre,
					_tablaSimbolos[idx].tipoDato,
					_tablaSimbolos[idx].valor,
					auxLong);
	}
	
	if( ( inter = fopen(ARCH_TABLA_TERCETOS, "wt") ) == NULL )
		lanzarError(ERROR_ARCH_INVALIDO, ARCH_TABLA_TERCETOS, TRUE);

	for( idx = 0; idx < _idxTercetos; idx++ )
	{
		fprintf(inter, "[%d] = (%s,%s,%s)\n",
					idx,
					_tablaTercetos[idx].operando,
					_tablaTercetos[idx].operador1,
					_tablaTercetos[idx].operador2);
	}
}

// 
// Mete en la cola la posicion de un terceto
// 
void encolar( int posicion )
{
	verificarEspacioCola();
	_finCola++;
	_colaTercetos[_finCola] = posicion;
}

// 
// Devuelve el indice tope de cola 
// 
int desencolar()
{
	int res = _colaTercetos[_topeCola];
	_topeCola++;
	return res;
}

// 
// Regenera la cola para que no crezca indefinidamente
// 
void resetCola()
{
	free(_colaTercetos);
	_topeCola = 0;
	_finCola = -1;
	_sizeCola = 0;
	verificarEspacioCola();
}

// 
// Mete en la pila la posicion de un terceto
// 
void apilar( int posicion )
{
	verificarEspacioPila();
	_topePila++;
	_pilaTercetos[_topePila] = posicion;
}

// 
// Devuelve el indice tope de pila 
// 
int desapilar()
{
	int res = _pilaTercetos[_topePila];
	_topePila--;
	return res;
}

// 
// Guarda un nuevo terceto en la tabla 
// 
int guardarTerceto(const char* operador, const char* op1, const char* op2)
{
	verificarEspacioTT();
	
	strcpy(_tablaTercetos[_idxTercetos].operando, operador);
	strcpy(_tablaTercetos[_idxTercetos].operador1, op1);
	strcpy(_tablaTercetos[_idxTercetos].operador2, op2);

	_idxTercetos++;

	return _idxTercetos - 1;
}

// 
// Modifica un terceto ya guardado
// 
void modificarTerceto( int posicion, const char* operando, const char* op1, const char* op2 )
{
	if( operando != NULL )
		strcpy(_tablaTercetos[posicion].operando, operando);
	if( op1 != NULL )
		strcpy(_tablaTercetos[posicion].operador1, op1);
	if( op2 != NULL )
		strcpy(_tablaTercetos[posicion].operador2, op2);
}

// 
// Convierte un indice en un string para guardar en el terceto
// 
void idxToChar( char* destino, int idx )
{
	sprintf(destino, "[%d]", idx);
}

// 
// Si use un NOT tengo que invertir el operador de salto
// 
void invertirOperador()
{
	if( ! strcmp( _auxOpSalto, "BNE" ) ) strcpy(_auxOpSalto, "BEQ");
	else if( ! strcmp( _auxOpSalto, "BLE" ) ) strcpy(_auxOpSalto, "BGT");
	else if( ! strcmp( _auxOpSalto, "BGE" ) ) strcpy(_auxOpSalto, "BLT");
	else if( ! strcmp( _auxOpSalto, "BLT" ) ) strcpy(_auxOpSalto, "BGE");
	else if( ! strcmp( _auxOpSalto, "BGT" ) ) strcpy(_auxOpSalto, "BLE");
	else if( ! strcmp( _auxOpSalto, "BEQ" ) ) strcpy(_auxOpSalto, "BNE");
}
