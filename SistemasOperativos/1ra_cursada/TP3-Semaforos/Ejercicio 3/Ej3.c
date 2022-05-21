/*
######################################################
#	TP 3	Ejercicio 3                          #
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
#include <string.h>
#include <errno.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <dirent.h>

#define TAM_NOMBRE_ARCHIVO 70
#define CAPACIDAD_MAX_DE_PROCESO 200
#define TAM_EXTENSION 30
#define PATH_MAXIMO 300
#define TAMANO_DE_LINEA_DE_ARCHIVO_ANALIZADO 300
#define CANT_THREADS 6


struct datos_fichero
	{
	char cod[TAM_EXTENSION];
	char nombre[TAM_NOMBRE_ARCHIVO];
	int leido,blanco,comentario,codigo;
	};


// ***************VARIABLES GLOBALES*************************************//
struct datos_fichero registro[CAPACIDAD_MAX_DE_PROCESO];
int Archivos_Encontrados=0;
char the_path[PATH_MAXIMO],the_path_AUX[PATH_MAXIMO];	//Aca guardo el path donde voy a buscar los archivos, el temporal es para cada archivo en particular
// ***********************FIN*****VARIABLES GLOBALES********************//


void Analizar_Archivo(void *ext);	//Declaracion del Hilo

void help(char *causa, char *binName)	//Funcion de ayuda
{
system("clear");
printf("\n*** Ejercicio 3 - Contabilizador de lineas de codigo de Archivos ***\n");
printf("Forma de Uso: ./Ej3 [[PATH_A_ANALIZAR]]\n");	
printf("El PATH es opcional, si se deja en blanco se analiza el directorio actual.\n");
}


int main(int argc, char **argv)
{
srand (time(NULL)); //Para generar el nombre aleatorio del archivo de salida.
  DIR *dir; // Con un puntero a DIR abriremos el directorio 
  struct dirent *ent;  // en *ent habrá información sobre el archivo que se está "sacando" a cada momento 
pthread_t threads[CANT_THREADS];
char copia[TAM_NOMBRE_ARCHIVO],copia2[TAM_NOMBRE_ARCHIVO],AuxNom[TAM_NOMBRE_ARCHIVO],*pos;

int i=0,i2=0,aleatorio;
char analiza[TAM_EXTENSION],AuxExt[TAM_EXTENSION],auxiliar[10];
int codigo=0,blanco=0,comentario=0;
char cabecera[256],archivoSalida[20];	

FILE *temp;

aleatorio = rand()%99999;	//genero un numero aleatorio para que forme parte del nombre del archivo de salida
sprintf(auxiliar,"%d",aleatorio);

// *********************************** INVOCAR AYUDA ***********************************
if (argc == 2)
{
if ( strcmp(argv[1], "-?")==0 || strcmp(argv[1], "-h")==0 || strcmp(argv[1], "-help")==0 )
	{
	help("-AYUDA-",argv[0]);
	return 0;
	}
}
// ******************************FIN ***** INVOCAR AYUDA ***********************************

// *****************************DETERMINO SOBRE QUE DIRECTORIO VOY A TRABAJAR*********************
if (argc > 1)
	{
	struct stat s;
	int err = stat(argv[1], &s);
	if(-1 == err) 
		{
		if(ENOENT == errno) 
			{	// si el directorio no existe 
			printf("El directorio NO existe\n");
			exit(1);	
    			} 
			else {	
				printf("Directorio ingresado en blanco\n");
        			exit(1);
    			}
		} 
		else {
    			if(S_ISDIR(s.st_mode)) 
				{  
				dir = opendir (argv[1]); // Empezaremos a leer en el directorio actual  
				strcpy(the_path,argv[1]);				
				if(the_path[strlen(the_path)-1]!='/')
					{strcat(the_path,"/");
					}
    				} else {		// existe pero no es un directorio
				printf("Existe, pero no es un directorio, es un arhivo\n");
        			exit(1);
    				}
		}
	}
else
	{
	getcwd(the_path, 255);
	strcat(the_path,"/");
	dir = opendir ("."); // Empezaremos a leer en el directorio actual
	}

// Función para devolver un error en caso de que ocurra */

  
  // Miramos que no haya error */
  if (dir == NULL) 
    error("No puedo abrir el directorio");
// **************************FIN***DETERMINO SOBRE QUE DIRECTORIO VOY A TRABAJAR*********************

 
  // Leyendo uno a uno todos los archivos que hay 

// ***********************ARMO VECTOR DE ESTRUCTURAS CON LOS ARCHIVOS ENCONTRADOS********************
while ((ent = readdir (dir)) != NULL) 
    	{
     	 // Nos devolverá el directorio actual (.) y el anterior (..), como hace ls 
	if ( (strcmp(ent->d_name, ".")!=0) && (strcmp(ent->d_name, "..")!=0) )
		{
		if(ent->d_type != 4)	//el valor de type nos dice si son archivos, asi descartamos subdirectorios.
			{
			registro[Archivos_Encontrados].leido=0;
			registro[Archivos_Encontrados].blanco=0;
			registro[Archivos_Encontrados].codigo=0;
			registro[Archivos_Encontrados].comentario=0;
			strcpy(copia,ent->d_name); //copio el nombre para despues trabajarlo, lo voy a ir cortando
			strcpy(AuxNom,ent->d_name);//salvo el nombre completo del archivo para despues si me sirve poder guardarlo
			if(strchr(copia,'.'))
				{	
				if(pos=strtok(copia,".")) //se encontro un punto, es decir, una extension
					{
					strcpy(copia2,pos);
					pos=strtok(NULL,".");
					strcpy(copia2,pos);
					for(i2=0;strlen(copia2)>=i2;i2++)					
					copia2[i2]=tolower(copia2[i2]); //pongo en minuscula la extension para comparar
					if((strcmp(copia2,"c")==0)||(strcmp(copia2,"cpp")==0)||(strcmp(copia2,"ini")==0)||(strcmp(copia2,"sh")==0)||(strcmp(copia2,"h")==0))
						{
						strcpy(registro[Archivos_Encontrados].cod,copia2);
						strcpy(registro[Archivos_Encontrados].nombre,AuxNom);
						Archivos_Encontrados++;
						}
					}
				
				}
			else    {
				for(i2=0;strlen(copia)>=i2;i2++)
					copia[i2]=tolower(copia[i2]);	//paso a minusculas para la comparacion
				if(strcmp(copia,"makefile")==0)	
					{
					strcpy(copia2,"makefile");
					strcpy(registro[Archivos_Encontrados].cod,copia2);
					strcpy(registro[Archivos_Encontrados].nombre,AuxNom);
					Archivos_Encontrados++;
					}
				}
			i++;
    			}
		}
	
	}
closedir (dir);

// ********************FIN***ARMO VECTOR DE ESTRUCTURAS CON LOS ARCHIVOS ENCONTRADOS********************


// *****************************RECORRO LO ENCONTRADO Y VOY CREANDO LOS HILOS POR CADA TIPO DE ARCHIVO***************

int aux=0,aux2=0,contHilos=0;
for(aux2=0;aux2<Archivos_Encontrados;aux2++)
	{
	if(registro[aux2].leido==0)
		{
		strcpy(analiza,registro[aux2].cod);
		for(aux=0;aux<Archivos_Encontrados;aux++)
			{
			if ((strcmp(analiza,registro[aux].cod))==0)
				registro[aux].leido=1;
			}
		pthread_create(&threads[contHilos], NULL,(void *) Analizar_Archivo,(void *)registro[aux2].cod);
		contHilos++;
		}
	}	
for(aux=0;aux<contHilos;aux++)		//espero la finalizacion de los hilos.
{
pthread_join(threads[aux],NULL);
}

// ***********************FIN****RECORRO LO ENCONTRADO Y VOY CREANDO LOS HILOS POR CADA TIPO DE ARCHIVO*******




// ****************************GENERAR EL ARCHIVO SE SALIDA**************************************

for(aux=0;aux<Archivos_Encontrados;aux++) registro[aux].leido=0;


strcpy(archivoSalida,"Salida_");
strcat(archivoSalida,auxiliar);	
strcat(archivoSalida,".txt");			//Genero el nombre del archivo de salida, para hacerlo aleatorio

temp = fopen(archivoSalida,"w");
if (temp == NULL){printf("NO SE PUDO ABRIR EL ARCHIVO TEMPORAL\n");exit(1);}

sprintf(cabecera,"Directorio analizado: %s\n",the_path);
fwrite(cabecera,1,strlen(cabecera),temp);

for(aux2=0;aux2<Archivos_Encontrados;aux2++)
	{
	if(registro[aux2].leido==0)
		{
		strcpy(analiza,registro[aux2].cod);
		for(aux=0;aux<Archivos_Encontrados;aux++)
			{
			if ((strcmp(analiza,registro[aux].cod))==0)
				{
				registro[aux].leido=1;
				blanco+=registro[aux].blanco;
				comentario+=registro[aux].comentario;
				codigo+=registro[aux].codigo;
				}
			}
		sprintf(cabecera,"Archivos %s: codigo: %d, blanco: %d, comentario: %d.\n",registro[aux2].cod,codigo,blanco,comentario);
		fwrite(cabecera,1,strlen(cabecera),temp);
		codigo=0;
		blanco=0;
		comentario=0;
		}
	}
if(Archivos_Encontrados==0)
	{
	sprintf(cabecera,"No se encontraron archivos(.c, .cpp, .h, .sh, .ini o Makefile ) en el directorio especificado.\n");
	fwrite(cabecera,1,strlen(cabecera),temp);
	}	
fclose(temp);
printf("Informe Generado: %s\n",archivoSalida);
// ****************************FIN *******GENERAR EL ARCHIVO SE SALIDA***********************************

pthread_exit(NULL);
return EXIT_SUCCESS;

} 

// *****************************FIN********MAIN************************************************************



void Analizar_Archivo(void *ext)
{
FILE *Arch;
int i,i2,tipo,multilinea,banderaDEcodigo,banderaDEcomentario,banderaDEescritura,contComillas;

char lineaDEarchivo[TAMANO_DE_LINEA_DE_ARCHIVO_ANALIZADO],lineaDEarchivoAUX[TAMANO_DE_LINEA_DE_ARCHIVO_ANALIZADO],*pos,*caracter;
char analiza[TAM_EXTENSION];
strcpy(analiza,(char *)ext);


for(i=0;i<Archivos_Encontrados;i++)
	{
	if((strcmp(registro[i].cod,"c")==0)||(strcmp(registro[i].cod,"cpp")==0)||(strcmp(registro[i].cod,"h")==0)) 
		{
		tipo=1;	//los arhicos c, cpp y h van a ser tipo 1.
		}
	else {
			tipo=2; //los arhicos ini, sh y Makefile van a ser tipo 2.
		}
	if(strcmp(analiza,registro[i].cod)==0) 
		{
		strcpy(the_path_AUX,the_path);
		strcat(the_path_AUX,registro[i].nombre);
		multilinea=0;	
			
		Arch = fopen(the_path_AUX,"r");
		if (Arch == NULL){printf("NO SE PUDO ABRIR EL ARCHIVO %s PARA LEER\n",registro[i].nombre);exit(1);}
		fgets(lineaDEarchivo,TAMANO_DE_LINEA_DE_ARCHIVO_ANALIZADO+1,Arch);
while (!feof(Arch))
	{
	banderaDEescritura=0;
	banderaDEcomentario=0;
	contComillas=0;
	strcpy(lineaDEarchivoAUX,lineaDEarchivo);
	if(multilinea==0)
		{
		if(strcmp(lineaDEarchivo,"\n")==0)	
			{
			registro[i].blanco++;
			}
		else	{
			for(i2=0;strlen(lineaDEarchivo)>i2;i2++)
				{							
				if((toascii(lineaDEarchivo[i2])!=32)&&(toascii(lineaDEarchivo[i2])!=10))
					banderaDEescritura=1;		//hay codigo antes del comentario	
				}
			if(banderaDEescritura==0)
				registro[i].blanco++;
			}
		if (banderaDEescritura==1)
			{
			if(tipo==1)	
				{
				if((strstr(lineaDEarchivo,"//")!=NULL)||(strstr(lineaDEarchivo,"/*")!=NULL))
					{
					banderaDEcomentario=1;
					banderaDEcodigo=0;
					registro[i].comentario++;
					if(lineaDEarchivo[0]!='/')	//para saber si la linea arranca con el comentario
						{
						caracter=strchr(lineaDEarchivo,'/');
						if(*(caracter+1)=='/')  //es comentario de una linea
							{
							pos=strtok(lineaDEarchivo,"//"); 
							}
						else	
							{
							if(*(caracter+1)=='*') //es comentario multilinea
								{
								pos=strtok(lineaDEarchivo,"/*");
								multilinea=1;
								}
							else	{
								if(strstr(lineaDEarchivo,"/*")!=NULL)
									multilinea=1;
								}
							} 
						strcpy(lineaDEarchivo,pos);
						for(i2=0;strlen(lineaDEarchivo)>i2;i2++)
							{						
							if(toascii(lineaDEarchivo[i2])!=32)
								banderaDEcodigo=1;		//hay codigo antes del comentario
							if(toascii(lineaDEarchivo[i2])==34)
								contComillas++;
							}

						if(banderaDEcodigo==1)
							registro[i].codigo++;
						if((contComillas%2)||(contComillas==1))	//me fijo si hubo impar de comillas dobles en la linea antes del /*
							{
							multilinea=0;
							registro[i].comentario--;
							}
						}
					else	{
						if(lineaDEarchivo[1]=='*')
							multilinea=1;
						}
					if (multilinea==1)
						{
						if(strstr(lineaDEarchivoAUX,"*/")!=NULL) //por si es comentario multilinea de solo una linea
							multilinea=0;
						}
							
					}
				
				}
			if(tipo==2)
				{
				if(strstr(lineaDEarchivo,"#")!=NULL)
					{
					banderaDEcomentario=1;
					registro[i].comentario++;					
					if(lineaDEarchivo[0]!='#')	//para saber si la linea arranca con el comentario
						{
						pos=strtok(lineaDEarchivo,"#"); 
						strcpy(lineaDEarchivo,pos);
						for(i2=0;strlen(lineaDEarchivo)>i2;i2++)
							{							
							if(toascii(lineaDEarchivo[i2])!=32)
								banderaDEcodigo=1;		//hay codigo antes del comentario	
							}
						if(banderaDEcodigo==1)
							registro[i].codigo++;
						}
					
					}
				}
			
			if (banderaDEcomentario==0)
				registro[i].codigo++;				
			} // fin del if escritura=1
		 
		} // fin verdadero de multilinea
	else		//COMENTARIO MULTILINEA detectada
		{
		registro[i].comentario++;
		if(strstr(lineaDEarchivo,"*/")!=NULL)
			multilinea=0;
		}
	
	fgets(lineaDEarchivo,TAMANO_DE_LINEA_DE_ARCHIVO_ANALIZADO+1,Arch);
	}//fin del while
     /* cierre del archivo */                            
fclose(Arch);
}	// fin del if de registro encontrado
}	// fin del for que recorre todos los registros
pthread_exit(NULL);
 
}	// fin del hilo
