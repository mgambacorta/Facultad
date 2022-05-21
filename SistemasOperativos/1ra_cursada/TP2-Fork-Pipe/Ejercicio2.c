/*****************************************************
* Trabajo Practico Nro 2 - Ejercicio Nro 2           *
******************************************************
*                                                    *
* Nombre:  Ejercicio2.c                              *
* Autores: Mariano Gambacorta DNI: 29279015          *
*          Pablo Carnovale    DNI: 33116185          *
*          Marcelo Romero     DNI: 34140911          *
* Entrega: 1ra entrega                               *
* Fecha:   06/10/2014                                *
*                                                    *
******************************************************/

#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>

#define ERROR_PARAMETROS 	1
#define ERROR_FORK			2
#define TRUE	1
#define FALSE	0

/**
 * Funcion que valida que se haya recibido un
 * parametro y que sea numerico
 */
int ValidaParametros( int argc, char** argv );

/**
 * Funcion que imprime los datos de cada proceso:
 * pid del proceso, pid del padre, parentezco con la raiz y pid de los hijos si los hay
 */
void Print(pid_t hijo1, pid_t hijo2, pid_t origen, int nivel);

int main( int argc, char** argv )
{
	int max;					/** La maxima cantidad de procesos a crear				**/	
	int nivel;					/** Esta varable muestra que tan lejos estoy de la raiz **/
	pid_t hijo1 = 0;			/** El pid del primer hijo                              **/
	pid_t hijo2 = 0;			/** El pid del segundo hijo                             **/
	pid_t origen = getpid();	/** El pid del proceso raiz                             **/
	
	/** Valido los argumentos **/
	if( ! ValidaParametros( argc, argv ) )
		return ERROR_PARAMETROS;
	
	/** Me traigo la cantidad de procesos que tengo que levantar **/
	max = atoi(argv[1]);

	for( nivel = 1; nivel < max; nivel++ )
	{
		/** Creo el primer hijo **/
		hijo1 = fork();

		if( hijo1 < 0 )
		{
			puts( "Error en fork()" );
			return ERROR_FORK;
		}
		else if( ! hijo1 )
		{
			/** Si soy el primer hijo, espero y vuelvo a empezar el for **/
			sleep(1);
			continue;
		}
		
		/** Creo el segundo hijo **/
		hijo2 = fork();
		
		if( hijo2 < 0 )
		{
			/** Antes de salir tengo que esperar al primer hijo **/
			wait(NULL);
			puts( "Error en fork()" );
			return ERROR_FORK;
		}
		else if( ! hijo2 )
		{
			/** Si soy el segundo hijo, tengo que poner en 0 a mi hermano **/
			hijo1 = 0;
			sleep(1);
		}
		else
		{
			/** Si soy el padre, salgo del for **/
			break;
		}
	} /** END FOR **/

	/** Me presento y espero a que terminen mis hijos **/
	Print( hijo1, hijo2, origen, nivel );
	wait(NULL);
	wait(NULL);
	
	/** Si soy el origen, aviso que terminamos todos **/
	if( getpid() == origen )
		puts("Toda mi familia termino - Fin de la ejecucion");
	
	return 0;
}

int ValidaParametros( int argc, char** argv )
{
	/** Valido que haya al menos un parametro **/
	if( argc < 2 )
	{
		puts("Error de parametros, se debe informar cantidad de procesos a crear");
		return FALSE;
	}
	
	/** Valido que el parametro sea numerico **/
	if( atoi(argv[1]) < 0 )
	{
		printf("Error de parametros: %s no es un numero valido", argv[1]);
		return FALSE;
	}
	
	return TRUE;
}

void Print(pid_t hijo1, pid_t hijo2, pid_t origen, int nivel)
{
	/** Si no tengo hijos no pase por el ultimo for, le tengo que agregar uno al nivel **/
	if( ! hijo1 ) nivel++;

	/** Primero que nada imprimo quien soy **/
	printf( "Soy %d, ", getpid() );
	
	/** Si no soy la raiz, imprimo quien es mi padre **/
	if( nivel != 1 )
		printf( "hijo de %d, ", getppid() );
	
	/** si no soy el proceso raiz, ni uno de sus hijos, calculo que parentezco me une **/
	switch( nivel )
	{
		case 1:
		case 2: 
			break;
		case 3:
			printf( "nieto de %d, ", origen );
			break;
		case 4:
			printf( "bisnieto de %d, ", origen );
			break;
		default:
			/** si soy mas que bisnieto, imprimo tantos "tatara" como haga falta **/
			for( ; nivel > 5; nivel-- )
				printf("tatara-");
			
			printf("tataranieto de %d, ", origen);
			break;			
	}
	
	/** Muestro el nombre de mis hijos, si los tengo **/
	if( hijo1 ) /** pregunto por un solo hijo porque o tengo 2 o no tengo ninguno **/
		printf( "padre de %d y %d\n", hijo1, hijo2 );
	else
		puts( "Sin hijos" );
}

/** EOF **/
