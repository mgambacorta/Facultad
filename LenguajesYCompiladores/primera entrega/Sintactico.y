%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "y.tab.h"

FILE *yyin;
FILE *ts;
int numLinea = 0;
int cantElementos = 0;
int tamanoBloque = 50;

typedef struct
{
	char nombre[31];
	char valor[31];
	int longitud;
} simbolo;

simbolo* tablaSimbolos = NULL;

int yylex();
void enviarLinea(int nLinea);
void imprimir( int punto, const char* msg );
void guardarEnTablaId(const char* dato);
void guardarEnTablaCte(const char* dato);
void guardarEnTablaFloat(double dato);
void guardarEnTablaInt(int dato);
int  buscarEnTabla( const char* dato );
void verificarEspacioTabla();
void volcarTablaSimbolos();
void salirConError(int codError, const char* parametro);

#define TABLA_SIMBOLOS "ts.txt"

#define	INICIO 	1
#define FIN		2

#define COMPILACION_OK		0
#define ERROR_PARAMETROS	1000
#define ERROR_ARCH_INVALIDO	1001
#define ERROR_LEXICO 		1002
#define ERROR_MALLOC		1003
#define ERROR_RANGO			1004

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
%token R_ENDIF
%token R_VAR
%token R_ENDVAR
%token R_FILTER
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
%token OP_FILTRO


%%
programa : blq_declaraciones {imprimir(INICIO, "sentencias");} blq_sentencias {imprimir(FIN, "sentencias");};

blq_declaraciones : R_VAR {imprimir(INICIO, "declaraciones");} lista_declaraciones {imprimir(FIN, "declaraciones");} R_ENDVAR;

lista_declaraciones : lista_declaraciones declaracion 
					| declaracion;

declaracion : COR_AB lista_tipos COR_CER DOS_P COR_AB lista_ids COR_CER {imprimir(0, "declaracion");};

lista_tipos : lista_tipos COMA tipo
			| tipo;
			
tipo : R_INT
	 | R_FLOAT
	 | R_STRING;

lista_ids : lista_ids COMA ID { guardarEnTablaId($<str_val>3); }
		  | ID { guardarEnTablaId($<str_val>1); };
		  
blq_sentencias : blq_sentencias sentencia
			   | sentencia;

sentencia : iteracion
		  | desicion
		  | escritura
		  | lectura
		  | asignacion;

iteracion : R_REPEAT {imprimir(INICIO, "REPEAT");} blq_sentencias R_UNTIL PAR_AB condicion PAR_CER {imprimir(FIN, "REPEAT");};

desicion :	R_IF PAR_AB condicion PAR_CER {imprimir(INICIO, "IF");} blq_sentencias R_ENDIF {imprimir(FIN, "IF");};

escritura : R_PRINT PAR_AB ID PAR_CER { guardarEnTablaId($<str_val>3); imprimir(0, "escritura (PRINT)");}
		  | R_PRINT PAR_AB CTE_STRING PAR_CER { guardarEnTablaCte($<str_val>3); imprimir(0, "escritura (PRINT)");};

lectura : R_READ PAR_AB ID PAR_CER { guardarEnTablaId($<str_val>3); imprimir(0, "lectura (READ)");};

asignacion : asignacion_simple
		   | asignacion_multiple;

condicion : comparacion {imprimir(0, "condicion");}
		  | comparacion OP_AND comparacion {imprimir(0, "condicion");}
		  | comparacion OP_OR comparacion {imprimir(0, "condicion");};

comparacion : expresion op_comparacion expresion
			| OP_NOT expresion op_comparacion expresion
			| expresion op_comparacion OP_NOT expresion;

op_comparacion : OP_IGUAL
			   | OP_MAYOR
			   | OP_MENOR
			   | OP_MAYIG
			   | OP_MENIG
			   | OP_DIST;

asignacion_simple : ID OP_ASIG expresion { guardarEnTablaId($<str_val>1); imprimir(0, "asignacion simple");};

asignacion_multiple : COR_AB lista_ids COR_CER OP_ASIG COR_AB lista_expresiones COR_CER {imprimir(0, "asignacion multiple");};

lista_expresiones : lista_expresiones COMA expresion
				  | expresion;

expresion : expresion OP_SUMA termino
		  | expresion OP_RESTA termino
		  | termino;

termino : termino OP_PROD factor
		| termino OP_DIV factor
		| factor;

factor : PAR_AB expresion PAR_CER
	   | filtro
	   | ID { guardarEnTablaId($<str_val>1); }
	   | constante_numerica;

filtro : R_FILTER PAR_AB comparacion_filtro COMA COR_AB lista_ids COR_CER PAR_CER {imprimir(0, "funcion FILTER");};

comparacion_filtro : OP_FILTRO op_comparacion expresion
				   | OP_FILTRO op_comparacion OP_NOT expresion;

constante_numerica : CTE_FLOAT { guardarEnTablaFloat($<doubleval>1); }
				   | CTE_INT { guardarEnTablaInt($<intval>1); };

%%

int main(int argc,char *argv[])
{
	if( argc != 2 )
		salirConError(ERROR_PARAMETROS, argv[0]);

	if( ( yyin = fopen(argv[1], "rt" ) ) == NULL)
		salirConError(ERROR_ARCH_INVALIDO, argv[1]);

	yyparse();

	volcarTablaSimbolos();
	salirConError(COMPILACION_OK, NULL);
}

//
// recibe errores del lexico
// 
int yyerror(void)
{
	salirConError(ERROR_LEXICO, NULL);
}

//
//Recibe el numero de linea del Lexico.l
//
void enviarLinea(int nLinea){
	numLinea = nLinea;
}

// 
// Finaliza el programa con el codigo de error correspondiente
//  
void salirConError(int codError, const char* parametro)
{
	if( codError != COMPILACION_OK)
		printf("\n[ERROR] en linea %d:\n", numLinea);
	
	switch(codError)
	{
		case COMPILACION_OK:
			printf("***** COMPILACION EXITOSA *****\n\n");
			break;
		case ERROR_PARAMETROS:
			printf("\tParametros incorrectos. Modo de ejecucion:\n\t\t%s <archivo de codigo>\n\n", parametro);
			break;
		case ERROR_ARCH_INVALIDO:
			printf("\tNo fue posible abrir el archivo %s\n\n", parametro);
			break;
		case ERROR_LEXICO:
			printf("\tError lexico no definido\n\n");
			break;
		case ERROR_RANGO:
			printf("\tSimbolo %s definido fuera del rango permitido\n\n", parametro);
			break;
		case ERROR_MALLOC:
			printf("\tNo es posible alocar memoria para la tabla de simbolos\n\n");
		default:
			printf("\tERROR INESPERADO (%d)\n\n", codError);
	}
	
	if( yyin ) fclose(yyin);
	if( ts ) fclose(ts);
	
	exit(codError);
}

// 
// Abre el archivo de tabla de simbolos y escribe los headers
// y los simbolos guardados en el vector
// 
void volcarTablaSimbolos()
{
	int idx;
	char auxLong[11];
	
	if( ( ts = fopen(TABLA_SIMBOLOS, "wt") ) == NULL )
		salirConError(ERROR_ARCH_INVALIDO, TABLA_SIMBOLOS);

	fprintf(ts, " %-30s | %-15s | %-30s | %s \n","NOMBRE","TIPO","VALOR","LONGITUD");
	
	for( idx = 0; idx < cantElementos; idx++ )
	{
		if( tablaSimbolos[idx].longitud != 0 )
			sprintf(auxLong, "%d", tablaSimbolos[idx].longitud);
		else
			memset(auxLong, '\0', sizeof(auxLong));
		
		fprintf(ts, " %-30s | %-15s | %-30s | %s \n",
					tablaSimbolos[idx].nombre,
					"", // El tipo de dato de momento queda vacio
					tablaSimbolos[idx].valor,
					auxLong);
	}
}

// 
// Guarda en tabla de simbolos un dato ID
// 
void guardarEnTablaId(const char* dato)
{
	printf( "RECIBI: [%s], de tipo ID en linea %d\n", dato, numLinea);

	if( buscarEnTabla(dato) )
		return;

	verificarEspacioTabla();

	strcpy(tablaSimbolos[cantElementos].nombre, dato);
	strcpy(tablaSimbolos[cantElementos].valor, "-");
	tablaSimbolos[cantElementos].longitud = 0;

	cantElementos++;
}

// 
// Guarda en tabla de simbolos un dato Constante string
// 
void guardarEnTablaCte(const char* dato)
{
	printf( "RECIBI: [%s], de tipo CONSTANTE en linea %d\n", dato, numLinea);

	if( buscarEnTabla(dato) )
		return;

	verificarEspacioTabla();

	sprintf(tablaSimbolos[cantElementos].nombre, "_%s", dato);
	strcpy(tablaSimbolos[cantElementos].valor, dato);
	tablaSimbolos[cantElementos].longitud = strlen(dato);

	cantElementos++;
}

// 
// Guarda en tabla de simbolos un dato entero
// 
void guardarEnTablaInt(int dato)
{
	char aux[11];
	sprintf(aux, "%d", dato);

	if( buscarEnTabla(aux) )
		return;

	verificarEspacioTabla();

	sprintf(tablaSimbolos[cantElementos].nombre, "_%d", dato);
	sprintf(tablaSimbolos[cantElementos].valor, "%d", dato);
	tablaSimbolos[cantElementos].longitud = 0;

	cantElementos++;
}

// 
// Guarda en tabla de simbolos un dato double
// 
void guardarEnTablaFloat(double dato)
{
	char aux[11];
	sprintf(aux, "%f", dato);

	if( buscarEnTabla(aux) )
		return;

	verificarEspacioTabla();

	sprintf(tablaSimbolos[cantElementos].nombre, "_%f", dato);
	sprintf(tablaSimbolos[cantElementos].valor, "%f", dato);
	tablaSimbolos[cantElementos].longitud = 0;

	cantElementos++;
}

int buscarEnTabla( const char* dato )
{
	int idx;
	
	for( idx = 0; idx < cantElementos; idx++ )
	{
		// los id los tengo que comparar por el nombre y 
		// las constantes por el valor
		if( ! strcmp(tablaSimbolos[idx].nombre, dato  ) ||
			! strcmp(tablaSimbolos[idx].valor, dato  ) )
				return 1;
	}
	return 0;
}

//
// Revisa si hay espacio en la tabla y si no aloca 
//
void verificarEspacioTabla()
{
	if( cantElementos == 0 )
	{
		tablaSimbolos = (simbolo*) malloc ( sizeof(simbolo) * tamanoBloque );
		if( tablaSimbolos == NULL ) salirConError(ERROR_MALLOC, NULL);
	}
	else if( cantElementos % tamanoBloque == 0 )
	{
		tablaSimbolos = (simbolo*) realloc ( tablaSimbolos, sizeof(simbolo) * tamanoBloque );
		if( tablaSimbolos == NULL ) salirConError(ERROR_MALLOC, NULL);
	}	
}

// 
// Imprime en pantalla inicio y fin de bloques
// y las sentencias del programa
// 
void imprimir( int punto, const char* msg )
{
	// if( punto == INICIO )
		// printf("*** INICIANDO BLOQUE: %s\n", msg);
	// else if( punto == FIN )
		// printf("*** FINALIZANDO BLOQUE: %s\n", msg);
	// else
		// printf("*** %s\n", msg);
}
