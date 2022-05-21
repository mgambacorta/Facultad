DIRHEAD=headers/
LD=gcc
CC=gcc
OBJETOS=cliente.o $(DIRHEAD)transferir_datos.o
CFLAGS=-c -g -Iheaders
LFLAGS=-Llibrerias

HolaMundo: $(OBJETOS)
	$(LD) $(OBJETOS) $(LFLAGS) -o cliente 

$(DIRHEAD)transferir_datos.o: $(DIRHEAD)transferir_datos.c 
	$(CC) $(CFLAGS) $(DIRHEAD)transferir_datos.c -o $(DIRHEAD)transferir_datos.o

cliente.o: cliente.c $(DIRHEAD)funciones.h 
	$(CC) $(CFLAGS) cliente.c -o cliente.o
