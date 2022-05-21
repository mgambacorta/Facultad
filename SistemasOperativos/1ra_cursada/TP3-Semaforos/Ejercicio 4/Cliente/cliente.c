/*
######################################################
#	TP 3	Ejercicio 4 Cliente                      #
#Integrantes:                                        #
#Maximiliano Toledo 31982343                         #
#Marcelo Julio Romero 34140911                       #
#Juan Jose Martinez 30944263                         #
#Oscar Emilio Pereyra 32382585                       #
#Javier Collazo 31632425                             #
######################################################
*/


/*cliente.c - Crear y ligar un socket. Sintaxis: conectar_tcpip direccion ip*/

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/wait.h>
#include <sys/errno.h>

#define MAX 10

int descriptor_socket;

void manejadorSIGINT(int parametro);

typedef struct info{
	int f1;
	int f2;
	int c1;
	int c2;
} infoComunic;

typedef struct resultado{
	int matriz_sinresolver[MAX][MAX], matriz_resuelta,v1,v2;
} infoResultado;



int main(int argc, char *argv[]) 
{
//	struct sockaddr srv;

	signal(SIGINT,manejadorSIGINT);
	
	struct sockaddr_in struc_cliente; /* Estructura de socket del proceso cliente */
	socklen_t tamanio_direccion; /* Tamaño del miembro sockaddr de estructura struc_cliente */

	/*Se guardan como argumento de la linea de comandos la direccion IP */

	//archivo donde se guardaran los datos del cliente
	FILE * fileCliente;	

  time_t tiempo;
  char fechayhora[80];
  struct tm *tmPtr;

  tiempo = time(NULL);
  tmPtr = localtime(&tiempo);
  strftime( fechayhora, 80, "%H:%M:%S-%d%m%Y", tmPtr );

fileCliente=fopen(fechayhora,"w+");
 //system("CLS");

fprintf(fileCliente,"\n");
strftime( fechayhora, 80, "%H:%M:%S, %d/%m/%Y", tmPtr );
fprintf(fileCliente,"INICIO DE PARTIDA: %s\n\n", fechayhora);

	if (argc!=2) {
		puts("MODO DE EMPLEO: crear_socket <nombre de socket> ");
		exit(EXIT_FAILURE);
	}
	

	int i=1; /* Para setsockopt */

	/* crear socket */
	if((descriptor_socket = socket(PF_INET, SOCK_STREAM, 0))<0) {
//		err_quit("socket");
		perror("socket");	
		exit(1);
	}
	
	
//	/* Se desea utilizar la direccion local */
//	setsockopt(descriptor_socket, SOL_SOCKET, 0, &i, sizeof(i));
	
	/* Incializar y establecer la estructura de  del cliente*/
	memset(&struc_cliente, 0 , sizeof(struc_cliente));
	struc_cliente.sin_family = AF_INET;
	struc_cliente.sin_port = htons(50000); /* No olvidar el orden de los bytes en la red */
	
	if (!(inet_aton(argv[1],&struc_cliente.sin_addr))) {/* Esto hara terminar el programa si la direccion ip es invalida */
		perror("inet_aton");
		exit(1);
	}
	
	/* Conectarse al socket */
	tamanio_direccion = sizeof (struc_cliente);
	if(connect(descriptor_socket, (struct sockaddr *)&struc_cliente, tamanio_direccion)) {
		perror("connect");
		exit(1);
	}

	puts("\tCliente conectado a socket TCP/IP");
	printf ("\tPuerto %d\n", ntohs(struc_cliente.sin_port));
	printf("\tDireccion IP %s \n", inet_ntoa(struc_cliente.sin_addr));
	

	int miNumero;    
	
	//read(descriptor_socket,&miNumero,sizeof(int));
	//printf("\nMe asignaron este numero de cliente: %d\n",miNumero);
	/* Tomamos la hora de llegada del socket del servidor */ 
//	read(descriptor_socket,&horaServidor,sizeof(time_t));

/********************************/
char datos[256];
int longitud;
infoComunic info;	
int matriz[MAX][MAX];
int j;
infoResultado resultado;

printf("\n\n\n\n\n\n***************************MODO DE JUEGO*******************************\n\n");
printf("Ingresa 2 pares de filas y columnas para poner en juego a tu memoria\n\n");
printf("Para salir ingresar todos ceros\n\n");

//Pido ingresar los valores 
printf("\n\nIngrese Fila 1 : ");
scanf("%d", &info.f1);	
printf("Ingrese Columna 1 : ");
scanf("%d", &info.c1);
printf("Ingrese Fila 2 : ");
scanf("%d", &info.f2);
printf("Ingrese Columna 2 : ");
scanf("%d", &info.c2);

//loopeo hasta que la matriz quede resuelta o se ingresen todos ceros para salir
while(resultado.matriz_resuelta!=1 && (info.f1 != 0 && info.f2 != 0 && info.c1 != 0 && info.c2 != 0)){ //while 1

	//le envio al servidor los valores recuperados
	write(descriptor_socket, &info, sizeof(info));

	//recupero la respuesta
	read(descriptor_socket,&resultado,sizeof(resultado)); 

	//Guardo los datos de la comunicacion

	tiempo = time(NULL);
	tmPtr = localtime(&tiempo);
	strftime( fechayhora, 80, "%H:%M:%S, %d/%m/%Y", tmPtr );

	fprintf(fileCliente,"\n\nDatos de Jugada Enviado\n\n");

	fprintf(fileCliente,"\n\nHora y Fecha: %s\n\n", fechayhora);

	fprintf(fileCliente,"\tDireccion IP %s \n", inet_ntoa(struc_cliente.sin_addr));
	fprintf (fileCliente,"\tPuerto %d\n", ntohs(struc_cliente.sin_port));

	fprintf(fileCliente,"\nFila1 : %d - Columna1 : %d ; Fila2 : %d - Columna2 : %d\n", info.f1,info.f2,info.c1,info.c2); 

	fprintf(fileCliente,"\nResultados recibidos: (Fila1-Columna1 : %d - Fila2-Columana2 : %d) \n",resultado.v1,resultado.v2); 

	printf("\n\nResultados Obtenidos\n____________________\n\n");

	printf("\nFila1-Columna1 : %d - Fila2-Columana2 : %d \n",resultado.v1,resultado.v2); 

	//finalizo la grabacion de datos


	//muestro la matriz parcialmente resuelta
	  printf("\n\n\t\tMatriz parcial");
	  printf("\n\n");
	  for(i = 0;i < MAX;i++){
	      printf("\n\t\t");
	      for(j = 0;j < MAX;j++){
		  if(resultado.matriz_sinresolver[i][j] == 0){
			printf("  %s  ", "x");
		  }
		  else{	
		  printf("  %d  ", resultado.matriz_sinresolver[i][j]);
		}
	       }
	  }


	printf("\n\n");
	if(resultado.matriz_resuelta != 1){
		//Pido ingresar los valores 
		printf("\n\nIngrese Fila 1 : ");
		scanf("%d", &info.f1);	
		printf("Ingrese Columna 1 : ");
		scanf("%d", &info.c1);
		printf("Ingrese Fila 2 : ");
		scanf("%d", &info.f2);
		printf("Ingrese Columna 2 : ");
		scanf("%d", &info.c2);
	}
	else{
		printf("\n\nFIN DEL JUEGO - GANASTE");
	}


}//fin del while 1

	exit(EXIT_SUCCESS);
}

void manejadorSIGINT(int parametro) {
		//en caso de que me llegue la interrupcion de Crl+c cierro la conexion y salgo con falla
		close(descriptor_socket);
		exit(EXIT_FAILURE);
}
