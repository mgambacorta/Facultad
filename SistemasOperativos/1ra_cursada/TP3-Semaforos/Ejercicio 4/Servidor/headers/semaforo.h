#ifndef SEMAFORO_H
#define SEMAFORO_H

//LE INDICO EL NUMERO DE SEMAFOROS A CREAR. RETORNA SEMID
int crearSemaforo(int); 

//LE PASO: SEMID, INDICE, VALOR.
void setearSemaforo(int, int, int); //SEMID, INDICE, VALOR

//LE PASO: SEMID
int eliminarSemaforo(int);

//LE PASO: SEMID, INDICE
int P(int, int);

//LE PASO: SEMID, INDICE
int V(int, int);

//LE PASO: SEMID, INDICE Y RETORNA EL VALOR DEL SEMAFORO.
int valorSemaforo(int, int);

#endif //SEMAFORO_H
