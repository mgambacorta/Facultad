
#include "includes/tablaSimbolos.h"

// 
// inicializa todas las variables
// 
void iniciarTS()
{
	_tablaSimbolos = NULL;
	_listaDeclaraciones = NULL;

	_idxTipos = 0;
	_idxVariables = 0;
	_idxTablaSimbolos = 0;
	_sizeTS = 0;
	_sizeLD = 0;	
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

	strcpy(_tablaSimbolos[_idxTablaSimbolos].tipoDato, tipo);
	strcpy(_tablaSimbolos[_idxTablaSimbolos].valor, dato);
	
	if( ! strcmp(tipo, "CTE_STRING")  )
	{
		getNombreCteString( _tablaSimbolos[_idxTablaSimbolos].nombre, dato );
		_tablaSimbolos[_idxTablaSimbolos].longitud = strlen(dato);
	}
	else
	{
		sprintf(_tablaSimbolos[_idxTablaSimbolos].nombre, "_%s", dato);
		_tablaSimbolos[_idxTablaSimbolos].longitud = 0;
	}

	_idxTablaSimbolos++;
}

void getNombreCteString( char* destino, const char* origen )
{
	char* ptr;
	char auxStr[50];
	
	memset( destino, '\0', sizeof(destino) );
	
	/** Lo meto en un auxiliar para que el strtok no me rompa el origen **/
	strcpy(auxStr, origen);

	ptr = strtok(auxStr, "\" ");
	while( ptr )
	{
		sprintf(destino, "%s_%s", destino, ptr);
		ptr = strtok(NULL, "\" ");
	}
}

// 
// Guarda en tabla de simbolos las variables que genere en assembler
// 
void guardarVarASM(const char* dato)
{
	if( buscarEnTabla(dato) != NO_EXISTE )
		return;

	verificarEspacioTS();

	sprintf(_tablaSimbolos[_idxTablaSimbolos].nombre, "%s", dato);
	strcpy(_tablaSimbolos[_idxTablaSimbolos].tipoDato, "FLOAT");
	strcpy(_tablaSimbolos[_idxTablaSimbolos].valor, "-");

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

void volcarArchivoTS()
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
	
	fclose(ts);
}
