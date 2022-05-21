#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <stdio.h>
#include <stdlib.h>
#include "semaforo.h"

#define OK 0

int crearSemaforo(int nSem)
{
   key_t clave;
   int semid;

   clave = ftok("servidor.c", 33);
   if (clave == (key_t)-1)
   {
      printf("NO SE PUDO GENERAR CLAVE IPC\n");
      exit(-1);
   }

   semid = semget(clave, nSem, IPC_CREAT | 0600);
   if (semid == -1)
   {
      printf("NO SE PUDO CREAR EL SET DE SEMAFOROS\n");
      exit(-1);
   }
   
   return semid;
}

void setearSemaforo(int semid, int semNumero, int valor)
{
   if ( semctl(semid, semNumero, SETVAL, valor) == -1)
   {
      printf("NO SE PUDO INICIALIZAR SEMAFORO NUMERO: %i\n", semNumero);
      exit(-1);
   }
}

int P(int semid, int semNumero)
{
   struct sembuf oper;

   oper.sem_num = semNumero;
   oper.sem_op  = -1;
   oper.sem_flg = 0;

   return (semop(semid, &oper, 1) != -1);
}

int V(int semid, int semNumero)
{
   struct sembuf oper;

   oper.sem_num = semNumero;
   oper.sem_op  = 1;
   oper.sem_flg = 0;

   return (semop(semid, &oper, 1) != -1);
}

int eliminarSemaforo(int semid)
{
   if ( semctl(semid, 0, IPC_RMID, 0) == -1 )
   {
      printf("NO SE PUDO ELIMINAR EL SEMAFORO\n");
      exit(-1);
   }

   return OK;
}

int valorSemaforo(int semid, int semNumero)
{
   int valor;
   
   valor = semctl(semid, semNumero, GETVAL, 0);

   if ( valor == -1)
   {
      printf("NO SE PUDO LEER El SEMAFORO NUMERO: %i\n", semNumero);
      exit(-1);
   }
  
   return valor;
}

