
#include "includes/asmGen.h"

void generarAssembler()
{
	_sentencias = NULL;
	_idxSent = 0;
	_sizeSent = 0;

	tercetosToAssembler();

	fpAssm = fopen(ARCH_ASSEMBLER, "wt");

	printHeader();
	printCodigo();
	
	fclose(fpAssm);
}

void tercetosToAssembler()
{
	int idx;

	for( idx = 0; idx < _idxTercetos; idx++ )
	{

		/** Pongo etiqueta a todos los tercetos para eventual salto **/
		sprintf(_buffLnea, "\nETQ_%d:", idx);
		guardarLinea();

		if( 
			strcmp(_tablaTercetos[idx].operador, "BNE") == 0 ||
			strcmp(_tablaTercetos[idx].operador, "BLE") == 0 ||
			strcmp(_tablaTercetos[idx].operador, "BGE") == 0 ||
			strcmp(_tablaTercetos[idx].operador, "BLT") == 0 ||
			strcmp(_tablaTercetos[idx].operador, "BGT") == 0 ||
			strcmp(_tablaTercetos[idx].operador, "BEQ") == 0 ||
			strcmp(_tablaTercetos[idx].operador, "BI") == 0 
		)
		{
			grabarSalto( idx );
		}
		else if( 
			strcmp(_tablaTercetos[idx].operador, "+") == 0 || 
			strcmp(_tablaTercetos[idx].operador, "-") == 0 || 
			strcmp(_tablaTercetos[idx].operador, "*") == 0 || 
			strcmp(_tablaTercetos[idx].operador, "/") == 0 
		)
		{	
			grabarOperBin( idx );
		}
		else if( strcmp(_tablaTercetos[idx].operador, "PRINT") == 0 )
		{
			grabarPrint( idx );
		}
		else if( strcmp(_tablaTercetos[idx].operador, "READ") == 0 )
		{
			grabarRead( idx );	
		}
		else if( strcmp(_tablaTercetos[idx].operador, "CMP") == 0 )
		{
			grabarComparacion( idx );	
		}
		else if( strcmp(_tablaTercetos[idx].operador, ":=") == 0 )
		{
			grabarAsignacion( idx );
		}
		else // Si no coincide con niguno de los anteriores, es una variable o constante suelta
		{
			grabarVariable( idx );
		}
	}
}

void grabarSalto( int idx )
{
	char opSaltoNew[5];
	cambiaOpSalto(opSaltoNew, _tablaTercetos[idx].operador);
	
	sprintf(_buffLnea, "\t%s ETQ_%s", opSaltoNew, strtok( _tablaTercetos[idx].operando1, "[]" ) );
	guardarLinea();
}

void grabarOperBin( int idx )
{
	char varAux[30];
	
	if( _tablaTercetos[idx].operando1[0] == '[' )
		sprintf(_buffLnea, "\tfld @var_%s", strtok( _tablaTercetos[idx].operando1, "[]" ) );
	else
		sprintf(_buffLnea, "\tfld _%s", _tablaTercetos[idx].operando1 );

	guardarLinea();

	if( _tablaTercetos[idx].operando2[0] == '[' )
		sprintf(_buffLnea, "\tfld @var_%s", strtok( _tablaTercetos[idx].operando2, "[]" ) );
	else
		sprintf(_buffLnea, "\tfld _%s", _tablaTercetos[idx].operando2 );

	guardarLinea();

	if( strcmp(_tablaTercetos[idx].operador, "+") == 0 )
		sprintf(_buffLnea, "\tfadd");
	else if( strcmp(_tablaTercetos[idx].operador, "-") == 0 )
		sprintf(_buffLnea, "\tfsub");
	else if( strcmp(_tablaTercetos[idx].operador, "*") == 0 )
		sprintf(_buffLnea, "\tfmul");
	else if( strcmp(_tablaTercetos[idx].operador, "/") == 0 )
		sprintf(_buffLnea, "\tfdiv");
	
	guardarLinea();

	sprintf(_buffLnea, "\tfstp @var_%d", idx);
	guardarLinea();

	sprintf(varAux, "@var_%d", idx);
	guardarVarASM( varAux );
}

void grabarPrint( int idx )
{
	char auxStr[50];
	
	if( _tablaTercetos[idx].operando1[0] == '\"' )
	{
		getNombreCteString(auxStr, _tablaTercetos[idx].operando1);
		sprintf(_buffLnea, "\tdisplayString %s\n\tnewLine", auxStr );
	}
	else
		sprintf(_buffLnea, "\tdisplayString _%s\n\tnewLine", _tablaTercetos[idx].operando1 );

	guardarLinea();
}

void grabarRead( int idx )
{
	sprintf(_buffLnea, "\tgetString _%s", _tablaTercetos[idx].operando1 );
	guardarLinea();
}

void grabarComparacion( int idx )
{
	if( _tablaTercetos[idx].operando2[0] == '[' )
		sprintf(_buffLnea, "\tfld @var_%s", strtok( _tablaTercetos[idx].operando2, "[]" ) );
	else
		sprintf(_buffLnea, "\tfld _%s", _tablaTercetos[idx].operando2 );

	guardarLinea();

	if( _tablaTercetos[idx].operando1[0] == '[' )
		sprintf(_buffLnea, "\tfld @var_%s", strtok( _tablaTercetos[idx].operando1, "[]" ) );
	else
		sprintf(_buffLnea, "\tfld _%s", _tablaTercetos[idx].operando1 );

	guardarLinea();

	sprintf(_buffLnea, "\tfcom");
	guardarLinea();
	sprintf(_buffLnea, "\tfstsw ax");
	guardarLinea();
	sprintf(_buffLnea, "\tsahf");
	guardarLinea();	
}

void grabarAsignacion( int idx )
{
	if( _tablaTercetos[idx].operando2[0] == '[' )
		sprintf(_buffLnea, "\tfld @var_%s", strtok( _tablaTercetos[idx].operando2, "[]" ) );
	else
		sprintf(_buffLnea, "\tfld _%s", _tablaTercetos[idx].operando2 );

	guardarLinea();

	sprintf(_buffLnea, "\tfstp _%s", _tablaTercetos[idx].operando1);
	guardarLinea();
}

void grabarVariable( int idx )
{
	char varAux[30];

	sprintf(_buffLnea, "\tfld _%s", _tablaTercetos[idx].operador );
	guardarLinea();

	sprintf(_buffLnea, "\tfstp @var_%d", idx);
	guardarLinea();

	sprintf(varAux, "@var_%d", idx);
	guardarVarASM( varAux );
}


void guardarLinea()
{
	verificarEspacioSent();
	strcpy( _sentencias[_idxSent].str, _buffLnea );
	_idxSent++;
}

void printHeader()
{
	int idx;

	fputs( "include includes/macros2.asm\n", fpAssm);
	fputs( "include includes/number.asm\n", fpAssm);
	fputs( ".MODEL\n", fpAssm);
	fputs( "LARGE\n", fpAssm);
	fputs( ".STACK 200h\n", fpAssm);
	fputs( ".386\n", fpAssm);
	fputs( ".DATA\n", fpAssm);
	fputs( "MAXSTRSIZE equ 33\n\n", fpAssm);
	
	for( idx = 0; idx < _idxTablaSimbolos; idx++ )
	{
		if( 
			strcmp( _tablaSimbolos[idx].tipoDato, "INT" ) == 0 || 
			strcmp( _tablaSimbolos[idx].tipoDato, "FLOAT" ) == 0 
		)
		{
			if( _tablaSimbolos[idx].nombre[0] == '@' )
				fprintf(fpAssm, "\t%s\tdd\t?\n", _tablaSimbolos[idx].nombre );
			else
				fprintf(fpAssm, "\t_%s\tdd\t?\n", _tablaSimbolos[idx].nombre );
		}
		else if( 
			strcmp( _tablaSimbolos[idx].tipoDato, "CTE_INT" ) == 0 ||
			strcmp( _tablaSimbolos[idx].tipoDato, "CTE_FLOAT" ) == 0
		)
		{
			fprintf(fpAssm, "\t%s\tdd\t%s\n", _tablaSimbolos[idx].nombre, _tablaSimbolos[idx].valor );
		}
		else if( strcmp( _tablaSimbolos[idx].tipoDato, "STRING" ) == 0 )
		{
			fprintf(fpAssm, "\t_%s\tdb\tMAXTEXTSIZE dup (?),'$'\n", _tablaSimbolos[idx].nombre );
		}
		else if( strcmp( _tablaSimbolos[idx].tipoDato, "CTE_STRING" ) == 0 )
		{
			fprintf(fpAssm, "\t%s\tdb\t%s,'$', %d dup (?)\n", _tablaSimbolos[idx].nombre, 
							_tablaSimbolos[idx].valor, _tablaSimbolos[idx].longitud );
		}
	}
}

void printCodigo()
{
	int idx; 

	fputs( "\n.CODE\n", fpAssm );
	fputs( ".startup\n", fpAssm );
	
	for( idx = 0; idx < _idxSent; idx++ )
	{
		fprintf(fpAssm, "%s\n", _sentencias[idx].str);
	}
	
	fputs( "\nEND\n", fpAssm );
}

void verificarEspacioSent()
{
	if( _sizeSent == 0 )
	{
		_sentencias = (_linea*) malloc ( sizeof(_linea) * BLOQUE_ALOCADO );
		_sizeSent = BLOQUE_ALOCADO;
	}
	else if( _idxSent == _sizeSent )
	{
		_sentencias = (_linea*) realloc ( _sentencias, sizeof(_linea) * (_sizeSent + BLOQUE_ALOCADO) );
		_sizeSent += BLOQUE_ALOCADO;
	}
}

void cambiaOpSalto( char* destino, const char* origen )
{
	if( strcmp(origen, "BNE") == 0 )
		strcpy( destino, "jnz");
	else if( strcmp(origen, "BLE") == 0 )
		strcpy( destino, "jbe");
	else if( strcmp(origen, "BGE") == 0 )
		strcpy( destino, "jae");
	else if( strcmp(origen, "BLT") == 0 )
		strcpy( destino, "jb");
	else if( strcmp(origen, "BGT") == 0 )
		strcpy( destino, "ja");
	else if( strcmp(origen, "BEQ") == 0 )
		strcpy( destino, "jz");
	else if( strcmp(origen, "BI") == 0 )
		strcpy( destino, "jmp");
}
