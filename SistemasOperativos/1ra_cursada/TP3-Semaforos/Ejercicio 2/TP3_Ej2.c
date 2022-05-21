/*
######################################################
#	TP 3	Ejercicio 2                              #
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
#include <sys/ipc.h>
#include <sys/types.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <signal.h>
#include <string.h>
#define SUMA 1
#define RESTA 1
#define DIVI 1
#define MULTI 1

typedef struct operaciones
{
int operando1;
int operando2;
char operacion[3];
int hay_op;//variable auxiliar podria no ir
int hay_resultado;
float resultado;//variable auxiliar podria no ir	
int hijo_suma;//variable auxiliar podria no ir
}t_operaciones;

typedef struct operaciones2
{
int operando1;
int operando2;
char operacion[3];
}t_ope;

int SALIDA=1;
t_operaciones *suma;
t_operaciones *divi;
t_operaciones *multi;
t_operaciones *resta;

/************************Semaforos**********************************************/
union semun {
int val;
struct semid_ds *buf;
unsigned short *array;
struct seminfo *__buf;
};
int obtenerMutex(key_t clave);
int obtenerSemaforo(key_t clave, int valor);
void pedirSemaforo(int IdSemaforo);
void devolverSemaforo(int IdSemaforo);
void eliminarSemaforo(int IdSemaforo);
void eliminarMutex(int IdSemaforo);
/*******************************************************************************/
int main(int argc, char *argv[])
{
int p=0;
key_t mutex_suma,mutex_prod,mutex_cons;
key_t mutex_multi,multi_prod,multi_cons;
key_t mutex_divi,divi_prod,divi_cons;
key_t mutex_resta,resta_prod,resta_cons;
/***********************************************************************************/
int id_suma,prod_suma,cons_suma,hay_ope;//varibles para los semaforos de la suma
int id_multi,prod_multi,cons_multi;//varibles para los semaforos de la multiplicacion
int id_resta,prod_resta,cons_resta;//varibles para los semaforos de resta 
int id_divi,prod_divi,cons_divi;//varibles para los semaforos de division
/***************************************************************************************************/
t_operaciones vec_ope[2];//vector donde guardo los datos en la mem compartida
t_ope vec[100];//vector de estrucutura donde se guardaran todas las operaciones que hay en el archivo
char operaciones[3][3];//con esta variable guardo las divisiones de las operaciones al vector de estrucutra
char cadena[20];//variable donde guardo la linea(operacion) que leo desde el archivo
int i,j=-1,estado,x=0;
FILE *pf,*graba_rdos;//puntero a archivo

pid_t pid; //guardo el pid
char * pch;//para dividir las operaciones y poder guardarlas en el vector estructura
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*inicializamos los contadores*/
int contador_gral=0,cont_suma=0,cs=0,cont_resta=0,cr=0,cont_divi=0,cd=0,cont_multi=0,cm=0,cant_op=0;
char codigo;
key_t clave_suma,clave_resta,clave_multi,clave_divi;
int Id_Memoria_suma,Id_Memoria_resta,Id_Memoria_multi,Id_Memoria_divi;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////*Abro el archivo y guardo las operaciones en un vector*//////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

if(strcmp(argv[1],"-?" )==0|| strcmp(argv[1],"-h")==0 ||strcmp(argv[1],"help" )==0) 
{
printf("Debe ingresar por parametro un archivo con operaciones separadas por un espacio. Ej. 4 + 7 etc. el programa calculara las operaciones y guardara los resultado en un archivo de salida llamado resultados.txt \n");
exit(1);

}

if((pf=fopen(argv[1], "r"))==NULL)
{
printf("Error al abrir archivo de configuracion del servidor.");
exit(1);
}
if((graba_rdos=fopen("resultados.txt", "w"))==NULL)
{
printf("Error al abrir archivo de resultados");
exit(1);
}

///************************************************///
//////////////*Semaforos para la suma*////////////////
mutex_suma= ftok("/", 6000);
mutex_prod= ftok("/", 6001);
mutex_cons= ftok("/", 6002);
id_suma = obtenerMutex(mutex_suma);
prod_suma=obtenerSemaforo(mutex_prod,1);	
cons_suma=obtenerSemaforo(mutex_cons,0);
//////////////////////////////////////////////////////////
//***Semaforos para la multiplicacion****/////////////////
mutex_multi= ftok("/", 6003);
multi_prod= ftok("/", 6004);
multi_cons= ftok("/", 6005);
id_multi = obtenerMutex(mutex_multi);
prod_multi=obtenerSemaforo(multi_prod,1);	
cons_multi=obtenerSemaforo(multi_cons,0);
//////////////////////////////////////////////////////////
//***Semaforos para la resta****/////////////////
mutex_resta= ftok("/", 6006);
resta_prod= ftok("/", 6007);
resta_cons= ftok("/", 6008);
id_resta = obtenerMutex(mutex_resta);
prod_resta=obtenerSemaforo(resta_prod,1);	
cons_resta=obtenerSemaforo(resta_cons,0);
//////////////////////////////////////////////////////////
//***Semaforos para la division****///////////////////////
mutex_divi= ftok("/", 6009);
divi_prod= ftok("/", 6010);
divi_cons= ftok("/", 6011);
id_divi = obtenerMutex(mutex_divi);
prod_divi=obtenerSemaforo(divi_prod,1);	
cons_divi=obtenerSemaforo(divi_cons,0);

/*****************************************************************************************************************/


/**************************************************************************************************************************/
//**Creo la memoria compartida para las 4 operaciones**///////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*Memoria para la suma*/        
	clave_suma = ftok ("/bin/ls", 33);
	if (clave_suma == -1)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit(0);
	}
        Id_Memoria_suma = shmget (clave_suma, sizeof(suma[2]), 0777 | IPC_CREAT);
	if (Id_Memoria_suma == -1)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit (0);
	}

	suma = (t_operaciones*)shmat(Id_Memoria_suma, NULL, 0);//Buffer de memoria para las operaciones de suma
	if (suma == NULL)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit (0);
	}
	/*Memoria para la multiplicacion*/
	clave_multi = ftok ("/bin/ls", 35);
	if (clave_multi == -1)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit(0);
	}
        Id_Memoria_multi = shmget (clave_multi, sizeof multi[2], 0777 | IPC_CREAT);
	if (Id_Memoria_multi == -1)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit (0);
	}

	multi = (t_operaciones*)shmat(Id_Memoria_multi,NULL, 0);//Buffer de memoria para las operaciones de multiplicacion
	if (multi == NULL)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit (0);
	}


	//**************Memoria para la resta****************************************************/
	clave_resta = ftok ("/bin/ls", 34);
	if (clave_resta == -1)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit(0);
	}
        Id_Memoria_resta = shmget (clave_resta, sizeof resta[2], 0777 | IPC_CREAT);
	if (Id_Memoria_resta == -1)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit (0);
	}

	resta = (t_operaciones*)shmat (Id_Memoria_resta,NULL, 0);
	if (resta == NULL)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit (0);
	}


	/******************************Memoria para la division*******************************/
	
	clave_divi = ftok ("/bin/ls", 36);
	if (clave_divi == -1)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit(0);
	}
        Id_Memoria_divi = shmget (clave_divi, sizeof(int)*100, 0777 | IPC_CREAT);
	if (Id_Memoria_divi == -1)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit (0);
	}
	divi = (t_operaciones*)shmat (Id_Memoria_divi,NULL, 0);
	if (divi == NULL)
	{
		printf ("No consigo clave para memoria compartida \n");
		exit (0);
	}
///////////////////////////////////////////////////////////////////////////////////////////
//****************************************************************************************************************/
//**Lectura del archivo y realizacion de las validaciones**//////////////////////////////////////////////////////

while (fgets(cadena,12, pf) != NULL) //leo el archivo y guardamos los datos en un vector de estructura "vec"
{
  p=0;
  pch = strtok (cadena," ");
  while (pch != NULL)
  {
    strcpy(operaciones[p],pch);
    p++;
    pch = strtok (NULL, " ,.");
   }
vec[cant_op].operando1=atoi(operaciones[0]);
if(vec[cant_op].operando1 < 0 || vec[cant_op].operando1 > 100)//Valida que el operando dos este entre 0 y 99
{
printf("Error de numero , debe ingresar un numero entre 0 y 99 \n");
exit(1);
}

strcpy(vec[cant_op].operacion,operaciones[1]);
if(strcmp(operaciones[1],"+")!=0&&strcmp(operaciones[1],"-")!=0&&strcmp(operaciones[1],"*")!=0&&strcmp(operaciones[1],"/")!=0)//Valida que la operacion sea una valida
{
printf("Error de Operacion , debe ingresar una operacion valida +, - , * o / \n");
exit(1);
}
if(strcmp(operaciones[1],"+")==0) cont_suma++;//contador que cuenta las operaciones de suma que hay en el archivo
if(strcmp(operaciones[1],"*")==0) cont_multi++;//contador que cuenta las operaciones de multiplicacion que hay en el archivo
if(strcmp(operaciones[1],"/")==0) cont_divi++;//contador que cuenta las operaciones de division que hay en el archivo
if(strcmp(operaciones[1],"-")==0) cont_resta++;//contador que cuenta las operaciones de resta que hay en el archivo
vec[cant_op].operando2=atoi(operaciones[2]);
if(vec[cant_op].operando2 < 0 || vec[cant_op].operando2 > 100)//Valida que el operando dos este entre 0 y 99
{
printf("Error de numero , debe ingresar un numero entre 0 y 99 \n");
exit(1);
}
cant_op++;//cuenta la cantidad de operaciones que hay en el archivo
}



///////////////////////////////////////////////////////////////////////////////////////////
	pid = fork();
	 
	  if(pid==0)
	    {  /*Hijo que se encarga de hacer las operaciones de multiplicacion*/
	        while( cm < cont_multi )		
		{
		pedirSemaforo(cons_multi);		
		pedirSemaforo(id_multi);
		multi->resultado=multi->operando1 * multi->operando2;
		fprintf(graba_rdos,"%d %s %d = %f \n",multi->operando1,multi->operacion,multi->operando2,multi->resultado);		
		devolverSemaforo(id_multi);
		devolverSemaforo(prod_multi);
		cm++;			
		}		
		exit(1);	    
	     }
	 
	  else {

		        pid = fork();		    
			if(pid==0)
                                {/*Hijo que se encarga de hacer las operaciones de suma*/
			        while( cs < cont_suma )		
				{
				pedirSemaforo(cons_suma);		
				pedirSemaforo(id_suma);
				suma->resultado=suma->operando1 + suma->operando2;
				fprintf(graba_rdos,"%d %s %d = %f \n",suma->operando1,suma->operacion,suma->operando2,suma->resultado);			
				suma->hay_resultado=1;			
				devolverSemaforo(id_suma);
				devolverSemaforo(prod_suma);
				cs++;	
				}						
				exit(1);
				}	    
			else

			{
						pid = fork();		    
						if(pid==0)
						        {/*Hijo que se encarga de hacer las operaciones de division*/
							  while( cd < cont_divi )		
							{
							pedirSemaforo(cons_divi);		
							pedirSemaforo(id_divi);
							divi->resultado=divi->operando1 / divi->operando2;
				fprintf(graba_rdos,"%d %s %d = %f \n",divi->operando1,divi->operacion,divi->operando2,divi->resultado);			
							divi->hay_resultado=1;			
							devolverSemaforo(id_divi);
							devolverSemaforo(prod_divi);
							cd++;	
							}						
							exit(1);
							}	    
						
						else		
					{
						pid = fork();		    
						if(pid==0)
						        {
							/*Hijo que se encarga de hacer las operaciones de resta*/	 
				         		  while( cr < cont_resta )		
							{
											
							pedirSemaforo(cons_resta);		
							pedirSemaforo(id_resta);
							resta->resultado=resta->operando1 - resta->operando2;
								
			fprintf(graba_rdos,"%d %s %d = %f \n",resta->operando1,resta->operacion,resta->operando2,resta->resultado);			
												
							devolverSemaforo(id_resta);
							devolverSemaforo(prod_resta);
							cr++;	
							}						
							exit(1);
							}
							else
							{
							/*Padre que se encargara las guardar las operaciones en la memoria compartida */
							while(contador_gral < cant_op )		
									{		
			
									vec[contador_gral].operacion;
							if(strcmp(vec[contador_gral].operacion,"*")==0)//Si es operacion multiplicacion	pasa		
									{			
									pedirSemaforo(prod_multi);			
									pedirSemaforo(id_multi);					
								  	multi->operando1=vec[contador_gral].operando1;
									multi->operando2=vec[contador_gral].operando2;
									strcpy(multi->operacion,vec[contador_gral].operacion);
									devolverSemaforo(id_multi);					
									devolverSemaforo(cons_multi);		 					
								}
						
						
									if(strcmp(vec[contador_gral].operacion,"+")==0)//Si es operacion suma	pasa		
									{
									pedirSemaforo(prod_suma);			
									pedirSemaforo(id_suma);
									suma->operando1=vec[contador_gral].operando1;
									suma->operando2=vec[contador_gral].operando2;
									strcpy(suma->operacion,vec[contador_gral].operacion);
									devolverSemaforo(id_suma);					
									devolverSemaforo(cons_suma);		 					
									}			
								
									if(strcmp(vec[contador_gral].operacion,"/")==0)//Si es operacion multiplicacion	pasa		
									{			
												
									pedirSemaforo(prod_divi);			
									pedirSemaforo(id_divi);					
								  	divi->operando1=vec[contador_gral].operando1;
									divi->operando2=vec[contador_gral].operando2;
									strcpy(divi->operacion,vec[contador_gral].operacion);
													
									devolverSemaforo(id_divi);					
									devolverSemaforo(cons_divi);		 					
									}

									if(strcmp(vec[contador_gral].operacion,"-")==0)//Si es operacion multiplicacion	pasa		
									{			
												
									pedirSemaforo(prod_resta);			
									pedirSemaforo(id_resta);					
								  	resta->operando1=vec[contador_gral].operando1;
									resta->operando2=vec[contador_gral].operando2;
									strcpy(resta->operacion,vec[contador_gral].operacion);
												
									devolverSemaforo(id_resta);					
									devolverSemaforo(cons_resta);		 					
									}



						contador_gral++;	
					}
					
							}	    
		    //**Cosas del padre**/////////// //////////////////////////////////// 
			
//**********************************************/////////////////////
			}				

	}


}


for (i=0 ; i<4;i++)
{
wait(NULL);
}

/////////////////////*Elimina la memoria compartida*//////////////////////////////	
	shmdt ((t_operaciones*)suma);
	shmctl (Id_Memoria_suma, IPC_RMID, (struct shmid_ds *)NULL);
	shmdt ((t_operaciones*)multi);
	shmctl (Id_Memoria_multi, IPC_RMID, (struct shmid_ds *)NULL);
	shmdt ((t_operaciones*)resta);
	shmctl (Id_Memoria_resta, IPC_RMID, (struct shmid_ds *)NULL);	
	shmdt ((t_operaciones*)divi);
	shmctl (Id_Memoria_divi, IPC_RMID, (struct shmid_ds *)NULL);
	/**Elimino los semaforos**/
	eliminarSemaforo(id_suma);
	eliminarSemaforo(prod_suma);
	eliminarSemaforo(cons_suma);
	
	eliminarSemaforo(id_multi);
	eliminarSemaforo(prod_multi);
	eliminarSemaforo(cons_multi);
	
	eliminarSemaforo(id_resta);
	eliminarSemaforo(prod_resta);
	eliminarSemaforo(cons_resta);

	eliminarSemaforo(id_divi);
	eliminarSemaforo(prod_divi);
	eliminarSemaforo(cons_divi);

printf("Los resultado se guardan en el archivo : resultados.txt\n");

return 1;
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////***Declaro las funciones***//////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

int obtenerMutex(key_t clave) 
{
int IdSemaforo;
union semun CtlSem;
IdSemaforo = semget(clave, 1, IPC_CREAT | 0600);
CtlSem.val = 1;
semctl(IdSemaforo, 0, SETVAL, CtlSem);
return IdSemaforo;
}

int obtenerSemaforo(key_t clave, int valor) 
{
int IdSemaforo;
union semun CtlSem;
IdSemaforo = semget(clave, 1, IPC_CREAT | 0600);
CtlSem.val = valor;
semctl(IdSemaforo, 0, SETVAL, CtlSem);
return IdSemaforo;
}
void pedirSemaforo(int IdSemaforo) 
{
struct sembuf OpSem;
OpSem.sem_num = 0;
OpSem.sem_op = -1;
OpSem.sem_flg = 0;
semop(IdSemaforo, &OpSem, 1);
}
void devolverSemaforo(int IdSemaforo) 
{
struct sembuf OpSem;
OpSem.sem_num = 0;
OpSem.sem_op = 1;
OpSem.sem_flg = 0;
semop(IdSemaforo, &OpSem, 1);
}
void eliminarSemaforo(int IdSemaforo) 
{
semctl(IdSemaforo, 0, IPC_RMID);
}
void eliminarMutex(int IdSemaforo) 
{
semctl(IdSemaforo, 0, IPC_RMID);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
