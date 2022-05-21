%{
#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <string.h>
#include <float.h>
#include <limits.h>
#include "y.tab.h"

#define VERIF_INT 	1
#define VERIF_FLOAT 2
#define VERIF_STR 	3
#define VERIF_ID 	4

#define LIM_FLOAT 2147483647
#define LIM_INT 32768
#define LIM_STR 30

#define ERROR_TAMANIO	1004
#define ERROR_CARACTER	1005

//prototipos
int verificarTamanio(int opc);
void salirConError(int codError, const char* parametro);
void enviarLinea(int nLinea);

%}

%option noyywrap  
%option yylineno 

DIGITO 			[0-9]
LETRA 			[a-zA-Z]
ID				{LETRA}({LETRA}|{DIGITO}|_)*
CTE_FLOAT		({DIGITO}*"."{DIGITO}+)|({DIGITO}+"."{DIGITO}*)
CTE_INT		    {DIGITO}+
CTE_STRING		\"[^"\n]+\"
COMENTARIOS		--\/({LETRA}|{DIGITO}|.[^\n])+\/--

%%
"+"				{ return OP_SUMA;}
"-"				{ return OP_RESTA;}
"*"				{ return OP_PROD;}
"/"				{ return OP_DIV;}
"("				{ return PAR_AB;}
")"				{ return PAR_CER;}
"["				{ return COR_AB;}
"]"				{ return COR_CER;}
":="			{ return OP_ASIG;}
":"				{ return DOS_P;}
","				{ return COMA;}
";"				{ return P_COMA; }
">="			{ return OP_MAYIG;}
"<="			{ return OP_MENIG;}
">"				{ return OP_MAYOR;}
"<"				{ return OP_MENOR;}
"=="			{ return OP_IGUAL;}
"<>"			{ return OP_DIST;}
"REPEAT"		{ return R_REPEAT;}
"UNTIL"			{ return R_UNTIL;}
"IF"			{ return R_IF;}
"ELSE"			{ return R_ELSE;}
"ENDIF"			{ return R_ENDIF;}
"AND"			{ return OP_AND;}
"OR"			{ return OP_OR;}
"NOT"			{ return OP_NOT;}
"VAR"			{ return R_VAR;}
"ENDVAR"		{ return R_ENDVAR;}
"PRINT"			{ return R_PRINT;}
"READ"			{ return R_READ;}
"INLIST"		{ return R_INLIST;}
"STRING"		{ strcpy(yylval.str_val, yytext); return R_STRING;}
"FLOAT"			{ strcpy(yylval.str_val, yytext); return R_FLOAT;}
"INT"			{ strcpy(yylval.str_val, yytext); return R_INT;}

{ID} { 
	verificarTamanio(VERIF_ID);
	strcpy(yylval.str_val, yytext);
	return ID;
}

{CTE_INT} { 	
	verificarTamanio(VERIF_INT);
	yylval.intval = atoi(yytext);
	return CTE_INT;
}

{CTE_FLOAT} { 
	verificarTamanio(VERIF_FLOAT);
	yylval.doubleval = atof(yytext);
	return CTE_FLOAT;
}

{CTE_STRING} {
	verificarTamanio(VERIF_STR);
	strcpy(yylval.str_val, yytext);
	return CTE_STRING;
}

"\n"			{ enviarLinea(yylineno); }
{COMENTARIOS}
"\t"
"\ "

.				{ strcpy(yylval.str_val, yytext); salirConError( ERROR_CARACTER, yytext ); }

%%

int verificarTamanio(int opc)
{
	switch(opc)
	{
		case VERIF_INT:
			if( atoi(yytext) > LIM_INT || atoi(yytext) < LIM_INT * (-1) ) salirConError( ERROR_TAMANIO, yytext);
			break;
		case VERIF_FLOAT:
			if( atoi(yytext) > LIM_FLOAT || atoi(yytext) < LIM_FLOAT * (-1) ) salirConError( ERROR_TAMANIO, yytext);
			break;
		case VERIF_STR:
			if( strlen(yytext) > LIM_STR + 2 ) salirConError( ERROR_TAMANIO, yytext);
			break;
		case VERIF_ID:
			if( strlen(yytext) > LIM_STR ) salirConError( ERROR_TAMANIO, yytext);
			break;
	}
}
