%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "y.tab.h"
#include "includes/declaraciones.h"
#include "includes/asmGen.h"
#include "includes/tercetos.h"
#include "includes/tablaSimbolos.h"

FILE *yyin;
int _numLinea = 0;

/////// Indices de No Terminales y auxiliares globales
int idxExpresion = -1;
int idxTermino = -1;
int idxFactor = -1;
int idxConstNum = -1;
int idxExpresionAnterior = -1;
int longInList = 0;

char _auxOpSalto[5];
char _auxVarInList[32];
char _auxIdxChar[5];
int _existeAnd = FALSE;
/////////////////////////////////////////////////////

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
programa : blq_declaraciones blq_sentencias 
			{ 
				volcarArchivoTT();
				generarAssembler();
				volcarArchivoTS();
			}

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
			} blq_sentencias R_UNTIL PAR_AB condicion_while PAR_CER {
				invertirOperador();
				idxToChar( desapilar() );
				guardarTerceto(_auxOpSalto, _auxIdxChar, "");
			};

desicion : condicion_if blq_sentencias R_ENDIF {
				idxToChar( _idxTercetos );
				modificarTerceto( desapilar(), NULL, _auxIdxChar, NULL );
				if( _existeAnd == TRUE ) // si hubo un and tengo que desapilar dos veces
				{
					modificarTerceto( desapilar(), NULL, _auxIdxChar, NULL );
					_existeAnd = FALSE;
				}
			};
		 | condicion_if blq_sentencias R_ELSE {
				idxToChar( _idxTercetos + 1 );
				modificarTerceto( desapilar(), NULL, _auxIdxChar, NULL );
				if( _existeAnd == TRUE ) // si hubo un and tengo que desapilar dos veces
				{
					modificarTerceto( desapilar(), NULL, _auxIdxChar, NULL );
					_existeAnd = FALSE;
				}
				apilar(guardarTerceto("BI", "", ""));
			} blq_sentencias R_ENDIF {
				idxToChar( _idxTercetos );
				modificarTerceto( desapilar(), NULL, _auxIdxChar, NULL );
				if( _existeAnd == TRUE ) // si hubo un and tengo que desapilar dos veces
				{
					modificarTerceto( desapilar(), NULL, _auxIdxChar, NULL );
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
				idxToChar( _idxTercetos + 1 );
				modificarTerceto( desapilar(), NULL, _auxIdxChar, NULL );
			};

condicion_while : comparacion
				| OP_NOT comparacion { invertirOperador(); }
				| comparacion {
						apilar( guardarTerceto(_auxOpSalto, "", "") );
					} OP_AND comparacion {
						idxToChar( _idxTercetos + 1 );
						modificarTerceto( desapilar(), NULL, _auxIdxChar, NULL );
					}
				| comparacion {
						int auxPos = desapilar();
						invertirOperador();
						idxToChar( auxPos );
						guardarTerceto(_auxOpSalto, _auxIdxChar, "");
						apilar( auxPos );	// Lo vuelvo a apilar para que lo toma la siguiente condicion
					} OP_OR comparacion {
						invertirOperador();
						idxToChar( desapilar() );
						guardarTerceto(_auxOpSalto, _auxIdxChar, "");
					};

comparacion : expresion {
				idxExpresionAnterior = idxExpresion;
			} op_comparacion expresion {
				char auxExp1[5];
				idxToChar(idxExpresionAnterior);
				strcpy(auxExp1, _auxIdxChar);
				idxToChar(idxExpresion);
				guardarTerceto("CMP", auxExp1, _auxIdxChar);
			}
			| inlist;

inlist : R_INLIST PAR_AB ID { 
			verificarDeclaracion($<str_val>3, ID_NUMERICO);
			strcpy( _auxVarInList, $<str_val>3 );
		} P_COMA COR_AB lista_expr_il COR_CER PAR_CER {
			strcpy(_auxOpSalto, "BI"); // Si se ejecuta es te terceto es porque ninguna expresion es igual, salto derecho a falsa
			idxToChar( _idxTercetos + 1 ); // El terceto que viene es el de comparacion, me lo salteo
			for( ; longInList != 0; longInList-- )
			{
				modificarTerceto( desapilar(), NULL, _auxIdxChar, NULL );
			}
		};

lista_expr_il : lista_expr_il P_COMA expresion {
					idxToChar( idxExpresion);
					guardarTerceto("CMP", _auxVarInList, _auxIdxChar);
					apilar(guardarTerceto("BEQ", "", ""));
					longInList++;
				}
			  | expresion{
					idxToChar( idxExpresion);
					guardarTerceto("CMP", _auxVarInList, _auxIdxChar);
					apilar(guardarTerceto("BEQ", "", ""));
					longInList++;
				};

op_comparacion : OP_IGUAL { strcpy(_auxOpSalto, "BNE"); }
			   | OP_MAYOR { strcpy(_auxOpSalto, "BLE"); }
			   | OP_MENOR { strcpy(_auxOpSalto, "BGE"); }
			   | OP_MAYIG { strcpy(_auxOpSalto, "BLT"); }
			   | OP_MENIG { strcpy(_auxOpSalto, "BGT"); }
			   | OP_DIST { strcpy(_auxOpSalto, "BEQ"); };

asignacion_simple : ID OP_ASIG expresion {
						verificarDeclaracion($<str_val>1, ID_NUMERICO);
						idxToChar( idxExpresion);
						guardarTerceto(":=", $<str_val>1, _auxIdxChar);
					};

asignacion_multiple : COR_AB lista_ids COR_CER OP_ASIG COR_AB lista_expresiones COR_CER { resetCola(); };

lista_ids : lista_ids COMA ID {
				verificarDeclaracion($<str_val>3, ID_NUMERICO);
				encolar( ":=", $<str_val>3, "" );
			}
		  | ID {
				verificarDeclaracion($<str_val>1, ID_NUMERICO);
				encolar( ":=", $<str_val>1, "" );
			};

lista_expresiones : lista_expresiones COMA expresion {
						_terceto auxTer = desencolar();
						idxToChar( idxExpresion);
						guardarTerceto(auxTer.operador, auxTer.operando1, _auxIdxChar);
					}
				  | expresion {
						_terceto auxTer = desencolar();
						idxToChar( idxExpresion);
						guardarTerceto(auxTer.operador, auxTer.operando1, _auxIdxChar);
					};

expresion : expresion OP_SUMA termino {
				char auxExp1[5];
				idxToChar(idxExpresion);
				strcpy(auxExp1, _auxIdxChar);
				idxToChar(idxTermino);
				idxExpresion = guardarTerceto("+", auxExp1, _auxIdxChar);
			}
		  | expresion OP_RESTA termino {
				char auxExp1[5];
				idxToChar(idxExpresion);
				strcpy(auxExp1, _auxIdxChar);
				idxToChar(idxTermino);
				idxExpresion = guardarTerceto("-", auxExp1, _auxIdxChar);
			}
		  | termino { idxExpresion = idxTermino; };

termino : termino OP_PROD factor {
			char auxFact[5];
			idxToChar( idxFactor);
			strcpy(auxFact, _auxIdxChar);
			idxToChar(idxTermino);
			idxTermino = guardarTerceto("*", _auxIdxChar, auxFact);
		}
		| termino OP_DIV factor {
			char auxFact[5];
			idxToChar( idxFactor);
			strcpy(auxFact, _auxIdxChar);
			idxToChar(idxTermino);
			idxTermino = guardarTerceto("/", _auxIdxChar, auxFact);
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

	iniciarTT();
	iniciarTS();
	
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
// Convierte un indice en un string para guardar en el terceto
// 
void idxToChar( int idx )
{
	sprintf(_auxIdxChar, "[%d]", idx);
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
