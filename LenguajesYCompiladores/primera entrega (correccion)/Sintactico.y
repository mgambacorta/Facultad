%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "y.tab.h"

FILE *yyin;
FILE *ts;
int numLinea = 0;
int idxTipos = 0;
int idxVariables = 0;
int cantElementos = 0;
int tamanoBloque = 50;

typedef struct
{
	char tipo[31];
	char variable[31];
} declaracion;

declaracion* listaDeclaraciones = NULL;

typedef struct
{
	char nombre[36];
	char tipoDato[31];
	char valor[36];
	int longitud;
} simbolo;

simbolo* tablaSimbolos = NULL;

int yylex();
void enviarLinea(int nLinea);
void imprimir( int punto, const char* msg );
void enlistarTipo(const char* dato);
void enlistarId(const char* dato);
void volcarDeclaracionesATS();
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

#define COMPILACION_OK			0
#define ERROR_PARAMETROS		1000
#define ERROR_ARCH_INVALIDO		1001
#define ERROR_SINTACTICO		1002
#define ERROR_MALLOC			1003
#define ERROR_RANGO				1004
#define ERROR_CARACT_INVALIDO	1005

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
programa : blq_declaraciones {imprimir(INICIO, "sentencias");} blq_sentencias {imprimir(FIN, "sentencias");};

blq_declaraciones : R_VAR {imprimir(INICIO, "declaraciones");} lista_declaraciones {imprimir(FIN, "declaraciones");} R_ENDVAR;

lista_declaraciones : lista_declaraciones declaracion 
					| declaracion;

declaracion : COR_AB lista_tipos COR_CER DOS_P COR_AB lista_ids_dec COR_CER { volcarDeclaracionesATS(); imprimir(0, "declaracion");};

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

iteracion : R_REPEAT {imprimir(INICIO, "REPEAT");} blq_sentencias R_UNTIL PAR_AB condicion PAR_CER {imprimir(FIN, "REPEAT");};

desicion : R_IF PAR_AB condicion PAR_CER blq_sentencias R_ENDIF {imprimir(FIN, "IF");};
		 | R_IF PAR_AB condicion PAR_CER blq_sentencias R_ELSE {imprimir(INICIO, "IF FALSE");} blq_sentencias R_ENDIF {imprimir(FIN, "IF");};

escritura : R_PRINT PAR_AB ID PAR_CER { imprimir(0, "escritura (PRINT)");}
		  | R_PRINT PAR_AB CTE_STRING PAR_CER { guardarEnTablaCte($<str_val>3); imprimir(0, "escritura (PRINT)");};

lectura : R_READ PAR_AB ID PAR_CER { imprimir(0, "lectura (READ)");};

asignacion : asignacion_simple
		   | asignacion_multiple;

condicion : comparacion {imprimir(0, "condicion simple");}
		  | OP_NOT comparacion {imprimir(0, "condicion NOT");}
		  | comparacion OP_AND comparacion {imprimir(0, "condicion AND");}
		  | comparacion OP_OR comparacion {imprimir(0, "condicion OR");};

comparacion : expresion op_comparacion expresion
			| inlist {imprimir(0, "condicion INLIST");};

inlist : R_INLIST PAR_AB ID P_COMA COR_AB lista_expr_il COR_CER PAR_CER;

lista_expr_il : lista_expr_il P_COMA expresion
			  | expresion;

op_comparacion : OP_IGUAL
			   | OP_MAYOR
			   | OP_MENOR
			   | OP_MAYIG
			   | OP_MENIG
			   | OP_DIST;

asignacion_simple : ID OP_ASIG expresion { imprimir(0, "asignacion simple");};

asignacion_multiple : COR_AB lista_ids COR_CER OP_ASIG COR_AB lista_expresiones COR_CER {imprimir(0, "asignacion multiple");};

lista_ids : lista_ids COMA ID
		  | ID;


lista_expresiones : lista_expresiones COMA expresion
				  | expresion;

expresion : expresion OP_SUMA termino
		  | expresion OP_RESTA termino
		  | termino;

termino : termino OP_PROD factor
		| termino OP_DIV factor
		| factor;

factor : PAR_AB expresion PAR_CER
	   | ID
	   | constante_numerica;

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
	salirConError(ERROR_SINTACTICO, NULL);
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
		case ERROR_SINTACTICO:
			printf("\tError Sintactico\n\n");
			break;
		case ERROR_RANGO:
			printf("\tSimbolo %s definido fuera del rango permitido\n\n", parametro);
			break;
		case ERROR_MALLOC:
			printf("\tNo es posible alocar memoria para la tabla de simbolos\n\n");
		case ERROR_CARACT_INVALIDO:
			printf("\tCaracter [%s] no reconocido por el Analizador Lexico\n\n", parametro);
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

	fprintf(ts, " %-35s | %-15s | %-35s | %s \n","NOMBRE","TIPO","VALOR","LONGITUD");
	
	for( idx = 0; idx < cantElementos; idx++ )
	{
		if( tablaSimbolos[idx].longitud != 0 )
			sprintf(auxLong, "%d", tablaSimbolos[idx].longitud);
		else
			memset(auxLong, '\0', sizeof(auxLong));
		
		fprintf(ts, " %-35s | %-15s | %-35s | %s \n",
					tablaSimbolos[idx].nombre,
					tablaSimbolos[idx].tipoDato,
					tablaSimbolos[idx].valor,
					auxLong);
	}
}

// 
// Mete en la lista de declaraciones un tipo de dato
// 
void enlistarTipo(const char* dato)
{
	if( idxTipos == 0 )
	{
		listaDeclaraciones = (declaracion*) malloc ( sizeof(declaracion) * tamanoBloque );
		if( listaDeclaraciones == NULL ) salirConError(ERROR_MALLOC, NULL);
	}
	else if( idxTipos % tamanoBloque == 0 )
	{
		listaDeclaraciones = (declaracion*) realloc ( listaDeclaraciones, sizeof(declaracion) * tamanoBloque );
		if( listaDeclaraciones == NULL ) salirConError(ERROR_MALLOC, NULL);
	}

	printf("Inserto tipo [%s] en la posicion [%d]\n", dato, idxTipos);

	strcpy(listaDeclaraciones[idxTipos].tipo,dato);
	idxTipos++;
}

// 
// Mete en la lista de declaraciones una variable
// 
void enlistarId(const char* dato)
{
	if( listaDeclaraciones == NULL )
	{
		listaDeclaraciones = (declaracion*) malloc ( sizeof(declaracion) * tamanoBloque );
		if( listaDeclaraciones == NULL ) salirConError(ERROR_MALLOC, NULL);
	}
	else if( idxVariables % tamanoBloque == 0 )
	{
		listaDeclaraciones = (declaracion*) realloc ( listaDeclaraciones, sizeof(declaracion) * tamanoBloque );
		if( listaDeclaraciones == NULL ) salirConError(ERROR_MALLOC, NULL);
	}

	strcpy(listaDeclaraciones[idxVariables].variable,dato);
	idxVariables++;
}

// 
// Guarda en tabla de simbolos todos los IDs declarados
// 
void volcarDeclaracionesATS()
{
	int idx;
	for (idx = 0; idx < idxTipos || idx < idxVariables ; idx++)
	{
		if( buscarEnTabla(listaDeclaraciones[idx].variable) )
			return;

		verificarEspacioTabla();

		strcpy(tablaSimbolos[cantElementos].nombre, listaDeclaraciones[idx].variable);
		strcpy(tablaSimbolos[cantElementos].tipoDato, listaDeclaraciones[idx].tipo);
		strcpy(tablaSimbolos[cantElementos].valor, "-");
		tablaSimbolos[cantElementos].longitud = 0;

		cantElementos++;
	}
}

// 
// Guarda en tabla de simbolos un dato Constante string
// 
void guardarEnTablaCte(const char* dato)
{
	if( buscarEnTabla(dato) )
		return;

	verificarEspacioTabla();

	sprintf(tablaSimbolos[cantElementos].nombre, "_%s", dato);
	strcpy(tablaSimbolos[cantElementos].tipoDato, "CTE_STRING");
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
	strcpy(tablaSimbolos[cantElementos].tipoDato, "CTE_INT");
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
	strcpy(tablaSimbolos[cantElementos].tipoDato, "CTE_FLOAT");
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
	if( punto == INICIO )
		printf("*** INICIANDO BLOQUE: %s\n", msg);
	else if( punto == FIN )
		printf("*** FINALIZANDO BLOQUE: %s\n", msg);
	else
		printf("*** %s\n", msg);
}
