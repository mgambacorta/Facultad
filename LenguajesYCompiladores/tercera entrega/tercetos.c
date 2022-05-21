
#include "includes/tercetos.h"

//
// inicializa las variables
//
void iniciarTT()
{
	_tablaTercetos = NULL;
	_colaTercetos = NULL;
	_pilaTercetos = NULL;

	_idxTercetos = 0;
	_topeCola = 0;
	_finCola = -1;
	_topePila = -1;

	_sizeTT = 0;
	_sizeCola = 0;
	_sizePila = 0;
}

// 
// Mete en la cola la posicion de un terceto
// 
void encolar( const char* oper, const char* op1, const char* op2 )
{
	verificarEspacioCola();

	_finCola++;

	strcpy(_colaTercetos[_finCola].operador, oper);
	strcpy(_colaTercetos[_finCola].operando1, op1);
	strcpy(_colaTercetos[_finCola].operando2, op2);
}

// 
// Devuelve el indice tope de cola 
// 
_terceto desencolar()
{
	_terceto res;
	
	strcpy(res.operador, _colaTercetos[_topeCola].operador);
	strcpy(res.operando1, _colaTercetos[_topeCola].operando1);
	strcpy(res.operando2, _colaTercetos[_topeCola].operando2);

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
	
	strcpy(_tablaTercetos[_idxTercetos].operador, operador);
	strcpy(_tablaTercetos[_idxTercetos].operando1, op1);
	strcpy(_tablaTercetos[_idxTercetos].operando2, op2);

	_idxTercetos++;

	return _idxTercetos - 1;
}

// 
// Modifica un terceto ya guardado
// 
void modificarTerceto( int posicion, const char* operador, const char* op1, const char* op2 )
{
	if( operador != NULL )
		strcpy(_tablaTercetos[posicion].operador, operador);
	if( op1 != NULL )
		strcpy(_tablaTercetos[posicion].operando1, op1);
	if( op2 != NULL )
		strcpy(_tablaTercetos[posicion].operando2, op2);
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
		_colaTercetos = (_terceto*) malloc ( sizeof(_terceto) * BLOQUE_ALOCADO );
		if( _colaTercetos == NULL ) lanzarError(ERROR_MALLOC, NULL, TRUE);
		_sizeCola = BLOQUE_ALOCADO;
	}
	else if( _finCola == _sizeCola )
	{
		_colaTercetos = (_terceto*) realloc ( _colaTercetos, sizeof(_colaTercetos) + sizeof(_terceto) * BLOQUE_ALOCADO );
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

void volcarArchivoTT()
{
	int idx;
	
	if( ( inter = fopen(ARCH_TABLA_TERCETOS, "wt") ) == NULL )
		lanzarError(ERROR_ARCH_INVALIDO, ARCH_TABLA_TERCETOS, TRUE);

	for( idx = 0; idx < _idxTercetos; idx++ )
	{
		fprintf(inter, "[%d] = (%s,%s,%s)\n",
					idx,
					_tablaTercetos[idx].operador,
					_tablaTercetos[idx].operando1,
					_tablaTercetos[idx].operando2);
	}

	fclose(inter);
}
