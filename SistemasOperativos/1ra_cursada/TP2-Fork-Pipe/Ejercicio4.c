/*****************************************************
* Trabajo Practico Nro 2 - Ejercicio Nro 4           *
******************************************************
*                                                    *
* Nombre:  Ejercicio4.c                              *
* Autores: Mariano Gambacorta DNI: 29279015          *
*          Pablo Carnovale    DNI: 33116185          *
*          Marcelo Romero     DNI: 34140911          *
* Entrega: 1ra reentrega                             *
* Fecha:   27/10/2014                                *
*                                                    *
******************************************************
* MODIFICACIONES 1ra Reentrega:                      *
*                                                    *
* Se modifica el control de errores del pclose para  *
* que loguee tanto en consola como en el archivo, el *
* error en caso de falla                             *
******************************************************/

#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <time.h>
#include <string.h>
#include <errno.h>

#define DATASIZE 512
#define COMMANDSIZE 90

int main(int argc, char *argv[])
{
    int i;                      /** iterador para el for y el while      **/
    FILE *pf;                   /** puntero para el pipe                 **/
    FILE *file;                 /** puntero al archivo de log            **/
    char data[DATASIZE];        /** para guardar los resultados del pipe **/
    char comando[COMMANDSIZE];  /** para guardar el comando a ejecutar   **/
    char h_inicio[50];          /** hora de inicio del comando           **/
    char h_fin[50];             /** hora del fin                         **/
    time_t now;                 /** auxiliar para informar  la hora      **/
    struct tm* now_tm;          /** auxiliar para informar  la hora      **/

    if(argc < 3)
    {
        printf("La cantidad de parametros es incorrecta\n");
        printf("modo de uso: %s <archivo_log> <comando> [<parametros>]");
        return 0;
    }


    /** Armo el comando con los respectivos parametros, si los hay **/
    strcpy(comando,argv[2]);

    if (argc > 3)
    {
        for ( i=3; i < argc; i++)
        {
            strcat(comando, " ");
            strcat(comando, argv[i]);
        }
    }

    /** Abro el archivo de log **/
    file = fopen(argv[1],"w");

    if (!file)
    {
        printf ("Ocurrio una falla al crear el archivo\n");
        return 0;
    }

    /** Calculo hora de inicio **/
    time(&now);
    now_tm = localtime(&now);
    strftime (h_inicio, 50, "Fecha y hora de inicio %d-%m-%Y %H:%M:%S.", now_tm);

    /** abro el pipe **/
    pf = popen(comando,"r");

    if (!pf)
    {
        printf ("Ocurrio una falla al crear el archivo pipe\n");
        return 0;
    }

    fprintf(file,"------------------------------------------------------------\n");
    fprintf(file,"Comando: %s | %s\n",comando,h_inicio);
    fprintf(file,"------------------------------------------------------------\n");

    /** Imprimo el resultado en forma paginada **/
    i=0;
    while (fgets(data,DATASIZE,pf))
    {
        fprintf(file,"%s",data);
        fprintf(stdout,"%s",data);
        if (i > 19)
        {
            getchar();
            i=0;
        }
        i++;
    }

    if( pclose(pf) )
    {
        perror("Ocurrio el siguiente error en la ejecucion");  /* MODIFICADO PARA REENTREGA */
        fprintf(file, "Error en la ejecucion del comando \"%s\": %d\n", comando, strerror(errno)); /* MODIFICADO PARA REENTREGA */
    }

    /** Calculo la hora de fin **/
    time(&now);
    now_tm = localtime(&now);
    strftime (h_fin, 50, "Fecha y hora de fin %d-%m-%Y %H:%M:%S.", now_tm);

    fprintf(file,"------------------------------------------------------------\n");
    fprintf(file,"Comando finalizado | %s\n",h_fin);
    fprintf(file,"------------------------------------------------------------\n");

    fclose(file);
}

