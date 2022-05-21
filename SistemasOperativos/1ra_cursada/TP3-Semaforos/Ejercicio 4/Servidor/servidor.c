/*
######################################################
#	TP 3	Ejercicio 4Servidor                      #
#Integrantes:                                        #
#Maximiliano Toledo 31982343                         #
#Marcelo Julio Romero 34140911                       #
#Juan Jose Martinez 30944263                         #
#Oscar Emilio Pereyra 32382585                       #
#Javier Collazo 31632425                             #
######################################################
*/



/*servidor.c - Ligarse a un socket TCP/IP y quedar a la espera de conecciones*/
#include <pthread.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <sys/errno.h>
#include <time.h>
#include <sys/types.h>
#include "semaforo.h"
#include "memotec.h"
#include <sys/wait.h>


#define CONEX_MAX 5
#define MAX 10

/* Estructura para la lista de datos enlazados */

typedef struct t_nodo{	
	char IPCliente[50];
	int nroCliente;
	int descriptor;
	//int comm_puerto;
	struct t_nodo *sig;	
} nodo;

typedef struct t_thread{
	struct sockaddr_in struc_servidor,struc_cliente; 
	socklen_t tamano_direccion;
	int descriptor_socket, descriptorArchivoDatos,semid,idCliente;	
	char IPCliente[50];
	//int comm_puerto;	
	int cantClientes;
	int contClientes;
	int matriz[MAX][MAX], matriz_sinresolver[MAX][MAX], matriz_resuelta;
	FILE * registroJugadas;
	//infoComunic infoArchivo;
	nodo *pnodo,*act; 
} info_thread;


typedef struct info{
	int f1;
	int f2;
	int c1;
	int c2;
} infoComunic;


typedef struct resultado{
	int matriz_sinresolver[MAX][MAX], matriz_resuelta,v1,v2;
} infoResultado;

info_thread tinfo;

void * thread(void *);
void manejadorSIGINT(int parametro);

int main(void) 
{

//FILE * registroJugadas;
	
//int matriz[MAX][MAX], 
int col, reng, x=0, j=0; 
int matriz[MAX][MAX] = {{1,2,3,4,5,6,7,8,9,10},{1,2,3,4,5,6,7,8,9,10},{11,12,13,14,15,16,17,18,19,20},{11,12,13,14,15,16,17,18,19,20},{21,22,23,24,25,26,27,28,29,30},{21,22,23,24,25,26,27,28,29,30},{31,32,33,34,35,36,37,38,39,40},{31,32,33,34,35,36,37,38,39,40},{41,42,43,44,45,46,47,48,49,50},{41,42,43,44,45,46,47,48,49,50}
};
int matriz_sinresolver[MAX][MAX], matriz_resuelta=0;
int f1,c1,f2,c2;



  time_t tiempo;
  char fechayhora[80];
  struct tm *tmPtr;

  tiempo = time(NULL);
  tmPtr = localtime(&tiempo);
  strftime( fechayhora, 80, "%H:%M:%S, %d/%m/%Y", tmPtr );


//asigno matriz
  for(x = 0;x < MAX;x++)
  {
      for(j = 0;j < MAX;j++){
          tinfo.matriz[x][j] = matriz[x][j];
      }
  }


//lleno la matriz de aciertos
  for(x = 0;x < MAX;x++)
  {
      for(j = 0;j < MAX;j++){
          matriz_sinresolver[x][j] = 0;
      }
  }


//Muestro la matriz original del juego
  printf("\n\n\t\tMatriz original");
  printf("\n\n");
  for(x = 0;x < MAX;x++){
      printf("\n\t\t");
      for(j = 0;j < MAX;j++){
          printf("  %6d  ", matriz[x][j]);
          }
  }

  printf("\n\n");       

tinfo.registroJugadas=fopen("resultado","w+");
 //system("CLS");

fprintf(tinfo.registroJugadas,"\n");
fprintf(tinfo.registroJugadas,"INICIO DE PARTIDA: %s\n\n", fechayhora);

////grabo la matriz original
  fprintf(tinfo.registroJugadas,"MATRIZ DE JUEGO\n");
  for(x = 0;x < MAX;x++){
      fprintf(tinfo.registroJugadas,"\n\t\t");
      for(j = 0;j < MAX;j++){
          fprintf(tinfo.registroJugadas,"  %6d  ", matriz[x][j]);
          }
  }

	signal(SIGINT,manejadorSIGINT);

	int descriptor_socket, descriptorArchivoDatos;

	struct sockaddr_in struc_servidor,struc_cliente; /* Estructura de socket en el servidor */
	socklen_t tamano_direccion; /* Tamaño del miembro sockaddr de estructura struc_servidor */

//	int conex_simul=0;  /* variable para las conexiones simultaneas*/
	pthread_t id;


	/* Inicializamos algunos valores necesarios de la estructura */
	tinfo.idCliente=0;
	tinfo.semid=0;
	tinfo.semid=crearSemaforo(2);
	setearSemaforo(tinfo.semid,0,CONEX_MAX); /* Maxima contidad de conexiones permitidas simultaneas */
	setearSemaforo(tinfo.semid,1,1); /* Quien puede escribir en la lista de datos uno a la vez */
	

	int i=1; /* Para setsockopt */

	/* crear socket */
	if((tinfo.descriptor_socket = socket(PF_INET, SOCK_STREAM, 0))<0) {
//		err_quit("socket");
		perror("socket");	
		exit(1);
	}
	
	/* Se desea utilizar la direccion local */
	setsockopt(tinfo.descriptor_socket, SOL_SOCKET, 0, &i, sizeof(i));
	
	/* Incializar y configurar la estructura de socket del servidor */
	memset(&tinfo.struc_servidor, 0 , sizeof(tinfo.struc_servidor));
	tinfo.struc_servidor.sin_family = AF_INET; //acepta conexiones de distintas maquinas
	tinfo.struc_servidor.sin_port = htons(50000);
	//tinfo.struc_servidor.sin_addr.s_addr = inet_ntoa(adrhost.sin_addr);
	
	/* Ligar el socket a una direccion */
	tinfo.tamano_direccion= sizeof(tinfo.struc_servidor);
	if ((bind(tinfo.descriptor_socket, (struct sockaddr *)&tinfo.struc_servidor,tinfo.tamano_direccion))<0) {
//		err_quit("bind");
		perror("bind");
		exit(1);
	}		
	/* Aguardas por las conexiones que arriben */
	if ((listen(tinfo.descriptor_socket,5))<0) {
//		err_quit("Listen");
		perror("Listen");
		exit(1);
	}

	puts("Socket TCP/IP conectado....");
	printf("\tDireccion IP %s \n", inet_ntoa(tinfo.struc_servidor.sin_addr));
	printf ("\tPuerto %d\n", ntohs(tinfo.struc_servidor.sin_port));
		

	/* Inicializamos el primer nodo de la lista y a pnodo lo dejamos apuntando al principio para tenerlo de referencia al mostrar todo al final */
	tinfo.act=(nodo*) malloc(sizeof(nodo));
   if (!tinfo.act) {
   	perror("malloc");
   	exit(1);	
   }
	tinfo.pnodo=tinfo.act;

//	printf("\nPermitido hasta 5 conexiones simultaneas con hilos\n\n");	

   tinfo.cantClientes=0;	
   tinfo.contClientes=1;	

	while (1) {
			/*****************************************************************************************************************/
			/* Utilizamos un semaforo inicializado en 5 para que solo puedan utilizarse 5 a la vez */
			P(tinfo.semid,0);	
	
			if((tinfo.descriptorArchivoDatos = accept(tinfo.descriptor_socket, (struct sockaddr *)&tinfo.struc_servidor, & tinfo.tamano_direccion))>=0) {
				
	
				printf("\tNueva conexion ---> IP del cliente: %s\n", inet_ntoa(tinfo.struc_servidor.sin_addr));
				strcpy(tinfo.IPCliente,inet_ntoa(tinfo.struc_servidor.sin_addr));
				
				/* Creamos un hilo para manejar el envio de datos */ 
				info_thread *pointer=&tinfo;
				if (pthread_create(&id,NULL,thread,(void *)(info_thread *)pointer)!=0)
					perror("pthread_create");
					
				if (!(inet_aton("255.255.255.255",&tinfo.struc_servidor.sin_addr))) {/* Esto hara terminar el programa si la direccion ip es invalida */
					perror("inet_aton");
					exit(1);
				}
				
			}
			else
				perror("accept()");
				
	}			

	exit(EXIT_SUCCESS);
	
}


/* Desde que entramos al hilo solo se podra salir cuando se termine el cliente que lo llamo o si se coloca ctrl+c */
void * thread(void *vtinfo) {
		
	//info_thread *tinfos=(info_thread *)vtinfo;
  // infoSocket * inf = (infoSocket *) param; //
  //infoComunic * infoArchivo = (infoComunic * ) &(inf->infoArchivo); // PUNTERO A LA ESTRUCTURA DE ARCHIVO
	

   char buffer[50];
   char datos[256];
   
int read_size;	

   infoComunic info;

	infoResultado resultado;

  time_t tiempo;
  char fechayhora[80];
  struct tm *tmPtr;


resultado.matriz_resuelta=0;


//lee hasta que dejen de transmitir
while( (read_size = read(tinfo.descriptorArchivoDatos,&info,sizeof(info))) > 0){ 
printf("la Cadena recibida es : %d - %d - %d - %d\n", info.f1,info.c1,info.f2,info.c2); 
//strcpy(datos, "Recibido");



//printf("la Cadena recibida es : %s", datos); 

//devolver los valores ingresados y la matriz parcil y el resultado

resultado.v1=tinfo.matriz[info.f1-1][info.c1-1];
resultado.v2=tinfo.matriz[info.f2-1][info.c2-1];


//en caso de coincidencia guardo el dato en matriz sin resolver
if(tinfo.matriz[info.f1-1][info.c1-1] == tinfo.matriz[info.f2-1][info.c2-1]){
	 printf("\n\n\t\tENTRA");
	resultado.matriz_sinresolver[info.f1-1][info.c1-1] = tinfo.matriz[info.f1-1][info.c1-1];
	resultado.matriz_sinresolver[info.f2-1][info.c2-1] = tinfo.matriz[info.f2-1][info.c2-1];
	tinfo.matriz_sinresolver[info.f1-1][info.c1-1] = tinfo.matriz[info.f1-1][info.c1-1];
	tinfo.matriz_sinresolver[info.f2-1][info.c2-1] = tinfo.matriz[info.f2-1][info.c2-1];
}


int i,j;

/*
  printf("\n\n\t\tMatriz sin resolver");
  printf("\n\n");
  for(i = 0;i < MAX;i++){
      printf("\n\t\t");
      for(j = 0;j < MAX;j++){
	  if(tinfo.matriz_sinresolver[i][j] == 0){
		resultado.matriz_sinresolver[i][j] = 0;
	  }
	  else{	
          printf("  %d  ", tinfo.matriz_sinresolver[i][j]);
	}
       }
  }
*/

int cont=0;
    for(i = 0;i < MAX;i++){
      printf("\n\t\t");
      for(j = 0;j < MAX;j++){
	
	  if(tinfo.matriz_sinresolver[i][j] == 0){
		cont=cont+1;
	  }
	}
       }
  
    if(cont==0){

	resultado.matriz_resuelta=1;
    }


  tiempo = time(NULL);
  tmPtr = localtime(&tiempo);
  strftime( fechayhora, 80, "%H:%M:%S, %d/%m/%Y", tmPtr );


   /* Guardamos la info y no dejamos que nadie nos interrumpa */
// P(tinfo.semid,1);
fprintf(tinfo.registroJugadas,"\n\nDatos de Jugada Recibida\n\n");

fprintf(tinfo.registroJugadas,"\n\nHora y Fecha: %s\n\n", fechayhora);

fprintf(tinfo.registroJugadas,"\tDireccion IP %s \n", inet_ntoa(tinfo.struc_servidor.sin_addr));
fprintf (tinfo.registroJugadas,"\tPuerto %d\n", ntohs(tinfo.struc_servidor.sin_port));

fprintf(tinfo.registroJugadas,"\nFila1 : %d - Columna1 : %d ; Fila2 : %d - Columna2 : %d\n", info.f1,info.f2,info.c1,info.c2); 

fprintf(tinfo.registroJugadas,"\nResultados enviados: (Fila1-Columna1 : %d - Fila2-Columana2 : %d) \n",resultado.v1,resultado.v2); 

//V(tinfo.semid,1);


write(tinfo.descriptorArchivoDatos, &resultado, sizeof(resultado));
} //fin de while

	
	V(tinfo.semid,0);
	
	return (void *)0;
}

/* Imprimimos la lista de datos */

void manejadorSIGINT(int parametro) {
	printf("\n\n\tTotal Clientes: %d\n",tinfo.cantClientes);
	while (tinfo.pnodo->sig) {
		printf("\tNumero de Cliente: %d \nDireccion IP:  %s \n",tinfo.pnodo->nroCliente,tinfo.pnodo->IPCliente);
		close(tinfo.pnodo->descriptor);
  		tinfo.pnodo=tinfo.pnodo->sig;
  	}
  	
  	printf("\n\n");
  	
  	exit(0);
}
