######################################################
# Trabajo Practico Nro 2                             #
######################################################
#                                                    #
# Nombre:  makefile                                  #
# Autores: Mariano Gambacorta DNI: 29279015          #
#          Pablo Carnovale    DNI: 33116185          #
#          Marcelo Romero     DNI:                   #
# Entrega: 1ra entrega                               #
# Fecha:   15/09/2014                                #
#                                                    #
######################################################

CC=gcc
CPP=g++
TARGETS=Ejercicio2 Ejercicio3 Ejercicio4 Ejercicio5

all: ${TARGETS}

Ejercicio2:
	${CC} -c Ejercicio2.c
	${CC} -o $@ Ejercicio2.o

Ejercicio3:
	${CC} -c Ejercicio3.c
	${CC} -o $@ Ejercicio3.o

Ejercicio4:
	${CC} -c Ejercicio4.c
	${CC} -o $@ Ejercicio4.o

Ejercicio5:
	${CC} -c Ejercicio5.c
	${CC} -o $@ Ejercicio5.o

clean:
	rm *.o
	rm ${TARGETS}
