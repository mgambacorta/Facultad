######################################################
#	TP 3	Ejercicio 3                          #
#Integrantes:                                        #
#Maximiliano Toledo 31982343                         #
#Marcelo Julio Romero 34140911                       #
#Juan Jose Martinez 30944263                         #
#Oscar Emilio Pereyra 32382585                       #
#Javier Collazo 31632425                             #
######################################################

# Makefile
CC= gcc 
LD= gcc
CFLAGS= -c -g -I ./include/
LFLAGS= -lpthread -L ./libs/
OBJS= Ej3.o
main: launcher
	echo "OK Listo."
launcher: $(OBJS)
	$(LD) $(LFLAGS) $(OBJS) -o Ej3
launcher.o: Ej3.c
	$(CC) $(CFLAGS) Ej3.c

