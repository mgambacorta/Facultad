/*
######################################################
#	TP 3	Ejercicio 1                              #
#Integrantes:                                        #
#Maximiliano Toledo 31982343                         #
#Marcelo Julio Romero 34140911                       #
#Juan Jose Martinez 30944263                         #
#Oscar Emilio Pereyra 32382585                       #
#Javier Collazo 31632425                             #
######################################################
*/


#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/time.h>
#include <unistd.h>
#include <sys/wait.h>
#include <signal.h>
#include <semaphore.h>
#include <errno.h>
#include <fcntl.h>
#include <time.h>
#include <string.h>
#include <sys/shm.h>
#include <sys/ipc.h>
#include <sys/stat.h>

#define H 3

void crearSemaforos( void );
void cerrarSemaforos( void );
void eliminarSemaforos( void );
void leer_Archivo( FILE *pf);

int M, W, D, E, S, A, i, j, k;	
pid_t *vecM, *vecH, *vecW, pid;
sem_t *mutexD, *semDP, *semDC, *mutexE, *semEP, *semEC, *mutexS, *semSP, *semSC, *mutexA, *semAP, *semAC;
struct s_memoria{
		int minero;
		int herrero;
		int herreroE;
		int herreroS;
		int herreroA;
		};

///// Manejador Señales /////
void sig_handler(int sigNum)
{
int i, j, k;

for ( i=0; i<=M; i++)
	kill (vecM[i], SIGTERM);

for ( j=0; j<=H; j++)
	kill (vecH[j], SIGTERM);

for ( k=0; k<=W; k++)
	kill (vecW[k], SIGTERM);
	
eliminarSemaforos();

free (vecM);
free (vecH);
free (vecW);

exit(0);
}

/////////////////////// MAIN ////////////////////////

void main (int argc, char *argv[])
{
int i, j, k, espera, Id_Memoria, val1, val2, time;
pid_t padre;
key_t clave;
struct s_memoria *memoria;
FILE * pf;	


/////-Lectura Archivo Configuracion-/////
if( ( pf = fopen( argv[1], "r" ) ) == NULL )
	{
	printf( "Error al abrir el archivo de configuracion\n" );
	exit( 1 );
	}

leer_Archivo( pf );	//Lee y valida el archivo de configuracion

printf( "W: %d\n", W );
printf( "D: %d\n", D );
printf( "M: %d\n", M );
printf( "H: %d\n", H );
printf( "E: %d\n", E );
printf( "S: %d\n", S );
printf( "A: %d\n", A );


/////Creacion e inicializacion de semaforos /////
crearSemaforos();


///// Capturo señales /////
signal(SIGTERM, sig_handler); 
signal(SIGUSR1, sig_handler);
signal(SIGUSR2, sig_handler);


///// Vector de pids /////
vecM = (pid_t *) malloc(sizeof(pid_t) * M);
if( vecM == NULL)
	{
	printf("\nMemoria insuficiente.\n");
	cerrarSemaforos();		
	eliminarSemaforos();
	exit(1);
	}

vecH = (pid_t *) malloc(sizeof(pid_t) * H);
if( vecH == NULL)
	{
	printf("\nMemoria insuficiente.\n");
	cerrarSemaforos();
	eliminarSemaforos();
	exit(1);
	}

vecW = (pid_t *) malloc(sizeof(pid_t) * W);
if( vecW == NULL)
	{
	printf("\nMemoria insuficiente.\n");
	cerrarSemaforos();
	eliminarSemaforos();
	exit(1);
	}

///// Definicion Memoria Compartida /////
clave = ftok( ".", 'M' );
if( clave == -1 )
	{
	printf( "Error al crear la clave de la Memoria Compartida\n" );
	exit(0);
	}

Id_Memoria = shmget( clave, sizeof(struct s_memoria ), IPC_CREAT | 0660 );
if( Id_Memoria == -1 )
	{
	printf( "Error al crear la Memoria Compartida\n" );
	exit(0);
	}

memoria = (struct s_memoria*) shmat( Id_Memoria, 0, 0 );
if( memoria == NULL )
	{
	printf( "Error, no hay Memoria\n" );
	exit(0);
	}

///// ------ /////

memoria->minero = W * 4;
memoria->herrero = W * 4;
memoria->herreroE = memoria->herreroS = memoria->herreroA = W;
//memoria->soldado = 3;
//memoria->soldadoE = memoria->soldadoS = memoria->soldadoA = W;

padre = getpid(); //Guardo el pid del proceso padre.
printf( "Iniciamos, Soy Padre <%d>\n\n", padre );

/////-Procesos Mineros-/////
for (i=0 ; i<M; i++)
	{
	pid = fork();
	if( pid == 0 )  //Ejecuta el hijo.
		{
		vecM[i] = getpid();
		while( memoria->minero > 0 )
			{
			sem_wait( semDP );	//P(semDP)		
			sem_wait( mutexD );	//P(mutexD)
			
			if( memoria->minero <= 0 )
				{				
				sem_post( mutexD );
				break;
				}
			memoria->minero--;	//Uso de RC(Mem Comp.)	
			printf("Minero <%d> genero 1 de hierro\n",vecM[i]);
			
			sem_post( mutexD );	//V(mutexD)	
			sem_post( semDC );	//V(semDC)
			
			usleep( 500000 );	//Duerme 0.5 seg	
			}	
		sem_close( mutexD );
		sem_close( semDP );
		sem_close( semDC );
		exit(0);	   
		}
	else
		{
		if( pid < 0)
			{
			printf("Error al crear el proceso Minero \n");
			exit(1);
			}
		}
	}


/////-Proceso Herrero-/////

for (j=0 ; j<H; j++)
	{
	pid = fork();
	if( pid == 0 )  //Ejecuta el hijo.
		{
		srand( getpid() );
		time = rand () % (3-0) + 1;	//Para dormir entre 1 y 3 seg.
		vecH[j] = getpid();
		while( memoria->herrero > 0 )
			{
			sem_wait( semDC );
			sem_wait( mutexD );
			
			if( memoria->herrero <= 0 )
				{
				sem_post( mutexD );
				break;
				}
			memoria->herrero--;	//Uso de RC(Mem Comp.)	

			sem_post( mutexD );		
			sem_post( semDP );
	
			if( getpid() == vecH[0] )
				{
				while(  memoria->herreroE > 0  )
					{
					sem_wait( semEP );		
					sem_wait( mutexE );		
					if( memoria->herreroE <= 0 )
						{
						sem_post( mutexE );
						break;
						}	
					printf("Herrero <%d> fabrico un escudo en %d segundos \n",vecH[j], time );
					memoria->herreroE--;	
					sem_post( mutexE );	
					sem_post( semEC );		
					}
				}
			if( getpid() == vecH[1] )
				{
				while(  memoria->herreroS > 0  )
					{
					sem_wait( semSP );		
					sem_wait( mutexS );		
					if( memoria->herreroS <= 0 )
						{
						sem_post( mutexS );
						break;
						}
					printf("Herrero <%d> fabrico una espada en %d segundos \n",vecH[j], time );
					memoria->herreroS--;	
					sem_post( mutexS );	
					sem_post( semSC );		
					}
				}
			if( getpid() == vecH[2] )
				{
				while(  memoria->herreroA > 0  )
					{
					sem_wait( semDC );
					sem_wait( semAP );		
					sem_wait( mutexA );		
					if( memoria->herreroA <= 0 )
						{
						sem_post( mutexA );
						break;
						}
					printf("Herrero <%d> fabrico una armadura en %d segundos \n",vecH[j], time );
					memoria->herreroA--;	
					sem_post( mutexA );	
					sem_post( semAC );		
					}	
				}

			sleep( time );	//Duerme 1-3 seg
		
			}
	
		sem_close( mutexD );
		sem_close( semDP );
		sem_close( semDC );
		if( getpid() == vecH[0] )
			{
			sem_close( semEP );
			sem_close( semEC );
			sem_close( mutexE );
			exit(EXIT_SUCCESS);
			}
		if( getpid() == vecH[1] )
			{
			sem_close( semSP );
			sem_close( semSC );
			sem_close( mutexS );
			exit(EXIT_SUCCESS);
			}
		if( getpid() == vecH[2] )
			{
			sem_close( semAP );
			sem_close( semAC );
			sem_close( mutexA );
			exit(EXIT_SUCCESS);
			}
		}
	else
		{
		if( pid < 0)
			{
			printf("Error al crear el proceso Herrero \n");
			exit(1);
			}
		}
	}



/////-Proceso Soldado-/////

for (k=0 ; k<W; k++)
	{
	pid = fork();
	if( pid == 0 )  //Ejecuta el hijo.
		{
		vecW[k] = getpid();
	
		sem_wait( semEC );		
		sem_wait( mutexE );			
		printf( "Soldado <%d> retiro un escudo\n",vecW[k] );
		sem_post( mutexE );	
		sem_post( semEP );		
		
		sem_wait( semSC );		
		sem_wait( mutexS );		
		printf( "Soldado <%d> retiro una espada\n", vecW[k] );
		sem_post( mutexS );	
		sem_post( semSP );		
					
		sem_wait( semAC );		
		sem_wait( mutexA );		
		printf( "Soldado <%d> retiro una armadura\n", vecW[k] );
		sem_post( mutexA );	
		sem_post( semAP );
		
		sem_close( semEP );	
		sem_close( semEC );
		sem_close( mutexE );
		sem_close( semSP );
		sem_close( semSC );
		sem_close( mutexS );
		sem_close( semAP );
		sem_close( semAC );
		sem_close( mutexA );
		exit(EXIT_SUCCESS);		
		}
	else
		{
		if( pid < 0)
			{
			printf("Error al crear el proceso Soldado \n");
			exit(1);
			}
		}
	}



///////////////////////////////////////////////////////////////////////////

for (espera=0 ; espera<(M+H+W); espera++)
	{
	wait(NULL);
	}

shmdt( memoria );
shmctl( Id_Memoria, IPC_RMID, 0 );

eliminarSemaforos();

//printf("fin de programa\n");
exit(0);
}


//////////////////////////////////////////////////////////////////////////
/////////////////////////////- FUNCIONES -////////////////////////////////



void leer_Archivo( FILE *pf )
{
char linea[10];

while( fgets( linea, sizeof( linea ), pf ) )
	{

	if( linea[1] != ':' )
		{
		printf( "Error en el archivo de configuracion\n" );
		exit( 0 );
		}

	switch( linea[0] )
		{
		case 'W':
			{
			W = atoi( (linea+2) );
			break;
			}
		case 'D':
			{
			D = atoi( (linea+2) );
			break;
			}
		case 'M':
			{
			M = atoi( (linea+2) );
			break;
			}
		case 'A':
			{
			A = atoi( (linea+2) );
			break;
			}
		case 'E':
			{
			E = atoi( (linea+2) );
			break;
			}
		case 'S':
			{
			S = atoi( (linea+2) );
			break;
			}
		default:
			{
			printf( "Error en el archivo de configuracion\n" );
			exit(0);
			}
		}
	}
}


void crearSemaforos()
{

mutexD = sem_open ("/mutexD", O_CREAT, 0644, 1);	//Comprueba si existe el semáforo,y sino lo crea.
if( mutexD == (sem_t *) -1 )
	{
	perror( "Error al crear el semaforo mutexD\n");	//Validación
	exit(1);
	}

semDP = sem_open ("/semDP", O_CREAT, 0644, D);
if( semDP == (sem_t *) - 1 )
	{
	perror("Error al crear el semaforo semDP\n");
	sem_close(mutexD);
	sem_unlink("/mutexD");
	exit(1);
	}

semDC = sem_open ("/semDC", O_CREAT, 0644, 0);
	if( semDC == (sem_t *)-1 ) 
	{	perror("Error al crear el semaforo semDC\n"); 
		sem_close(mutexD);
		sem_unlink("/mutexD");
		sem_close(semDP);
		sem_unlink("/semDP");
		exit(1);
	}

mutexE = sem_open ("/mutexE", O_CREAT, 0644, 1);
	if( mutexE == (sem_t *)-1 ) 
	{	perror("Error al crear el semaforo mutexE\n"); 
		sem_close(mutexD);
		sem_unlink("/mutexD");
		sem_close(semDP);
		sem_unlink("/semDP");
		sem_close(semDC);
		sem_unlink("/semDC");
		exit(1);
	}

semEP = sem_open ("/semEP", O_CREAT, 0644, E);
	if( semEP == (sem_t *)-1 ) 
	{	perror("Error al crear el semaforo semEP\n"); 
		sem_close(mutexD);
		sem_unlink("/mutexD");
		sem_close(semDP);
		sem_unlink("/semDP");
		sem_close(semDC);
		sem_unlink("/semDC");
		sem_close(mutexE);
		sem_unlink("/mutexE");
		exit(1);
	}

semEC = sem_open ("/semEC", O_CREAT, 0644, 0);
	if( semEC == (sem_t *)-1 ) 
	{	perror("Error al crear el semaforo semEC\n"); 
		sem_close(mutexD);
		sem_unlink("/mutexD");
		sem_close(semDP);
		sem_unlink("/semDP");
		sem_close(semDC);
		sem_unlink("/semDC");
		sem_close(mutexE);
		sem_unlink("/mutexE");
		sem_close(semEP);
		sem_unlink("/semEP");
		exit(1);
	}

mutexS = sem_open ("/mutexS", O_CREAT, 0644, 1);
	if( mutexS == (sem_t *)-1 ) 
	{	perror("Error al crear el semaforo mutexS\n"); 
		sem_close(mutexD);
		sem_unlink("/mutexD");
		sem_close(semDP);
		sem_unlink("/semDP");
		sem_close(semDC);
		sem_unlink("/semDC");
		sem_close(mutexE);
		sem_unlink("/mutexE");
		sem_close(semEP);
		sem_unlink("/semEP");
		sem_close(semEC);
		sem_unlink("/semEC");
		exit(1);
	}

semSP = sem_open ("/semSP", O_CREAT, 0644, S);
	if( semSP == (sem_t *)-1 ) 
	{	perror("Error al crear el semaforo semSP\n"); 
		sem_close(mutexD);
		sem_unlink("/mutexD");
		sem_close(semDP);
		sem_unlink("/semDP");
		sem_close(semDC);
		sem_unlink("/semDC");
		sem_close(mutexE);
		sem_unlink("/mutexE");
		sem_close(semEP);
		sem_unlink("/semEP");
		sem_close(semEC);
		sem_unlink("/semEC");
		sem_close(mutexS);
		sem_unlink("/mutexS");
		exit(1);
	}

semSC = sem_open ("/semSC", O_CREAT, 0644, 0);
	if( semSC == (sem_t *)-1 ) 
	{	perror("Error al crear el semaforo semSC\n"); 
		sem_close(mutexD);
		sem_unlink("/mutexD");
		sem_close(semDP);
		sem_unlink("/semDP");
		sem_close(semDC);
		sem_unlink("/semDC");
		sem_close(mutexE);
		sem_unlink("/mutexE");
		sem_close(semEP);
		sem_unlink("/semEP");
		sem_close(semEC);
		sem_unlink("/semEC");
		sem_close(mutexS);
		sem_unlink("/mutexS");
		sem_close(semSP);
		sem_unlink("/semSP");
		exit(1);
	}

mutexA = sem_open ("/mutexA", O_CREAT, 0644, 1);
	if( mutexA == (sem_t *)-1 ) 
	{	perror("Error al crear el semaforo mutexA\n"); 
		sem_close(mutexD);
		sem_unlink("/mutexD");
		sem_close(semDP);
		sem_unlink("/semDP");
		sem_close(semDC);
		sem_unlink("/semDC");
		sem_close(mutexE);
		sem_unlink("/mutexE");
		sem_close(semEP);
		sem_unlink("/semEP");
		sem_close(semEC);
		sem_unlink("/semEC");
		sem_close(mutexS);
		sem_unlink("/mutexS");
		sem_close(semSP);
		sem_unlink("/semSP");
		sem_close(semSC);
		sem_unlink("/semSC");
		exit(1);
	}

semAP = sem_open ("/semAP", O_CREAT, 0644, A);
	if( semAP == (sem_t *)-1 ) 
	{	perror("Error al crear el semaforo semAP\n"); 
		sem_close(mutexD);
		sem_unlink("/mutexD");
		sem_close(semDP);
		sem_unlink("/semDP");
		sem_close(semDC);
		sem_unlink("/semDC");
		sem_close(mutexE);
		sem_unlink("/mutexE");
		sem_close(semEP);
		sem_unlink("/semEP");
		sem_close(semEC);
		sem_unlink("/semEC");
		sem_close(mutexS);
		sem_unlink("/mutexS");
		sem_close(semSP);
		sem_unlink("/semSP");
		sem_close(semSC);
		sem_unlink("/semSC");
		sem_close(mutexA);
		sem_unlink("/mutexA");
		exit(1);
	}

semAC = sem_open ("/semAC", O_CREAT, 0644, 0);
	if( semAC == (sem_t *)-1 ) 
	{	perror("Error al crear el semaforo semAC\n"); 
		sem_close(mutexD);
		sem_unlink("/mutexD");
		sem_close(semDP);
		sem_unlink("/semDP");
		sem_close(semDC);
		sem_unlink("/semDC");
		sem_close(mutexE);
		sem_unlink("/mutexE");
		sem_close(semEP);
		sem_unlink("/semEP");
		sem_close(semEC);
		sem_unlink("/semEC");
		sem_close(mutexS);
		sem_unlink("/mutexS");
		sem_close(semSP);
		sem_unlink("/semSP");
		sem_close(semSC);
		sem_unlink("/semSC");
		sem_close(mutexA);
		sem_unlink("/mutexA");
		sem_close(semAP);
		sem_unlink("/semAP");
		exit(1);
	}
}


void cerrarSemaforos()
{
sem_close( mutexD );
sem_close( semDP );
sem_close( semDC );
sem_close( mutexE );
sem_close( semEP );
sem_close( semEC );
sem_close( mutexS );
sem_close( semSP );
sem_close( semSC );
sem_close( mutexA );
sem_close( semAP );
sem_close( semAC );
}


void eliminarSemaforos()
{
sem_unlink("/mutexD");
sem_unlink("/semDP");
sem_unlink("/semDC");
sem_unlink("/mutexE");
sem_unlink("/semEP");
sem_unlink("/semEC");
sem_unlink("/mutexS");
sem_unlink("/semSP");
sem_unlink("/semSC");
sem_unlink("/mutexA");
sem_unlink("/semAP");
sem_unlink("/semAC");
}




