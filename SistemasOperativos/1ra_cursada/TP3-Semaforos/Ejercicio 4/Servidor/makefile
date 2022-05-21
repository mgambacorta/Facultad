DIRHEAD=headers/
LD=gcc
CC=gcc
HEAD= $(DIRHEAD)transferir_datos.h $(DIRHEAD)semaforo.h $(DIRHEAD)memotec.h 
OBJETOS=servidor.o $(DIRHEAD)transferir_datos.o $(DIRHEAD)semaforo.o $(DIRHEAD)memotec.o
CFLAGS=-c -g -Iheaders
LFLAGS=-Llibrerias -lpthread

HolaMundo: $(OBJETOS)
	$(LD) $(OBJETOS) $(LFLAGS) -o servidor 

$(DIRHEAD)transferir_datos.o: $(DIRHEAD)transferir_datos.c 
	$(CC) $(CFLAGS) $(DIRHEAD)transferir_datos.c -o $(DIRHEAD)transferir_datos.o
	
$(DIRHEAD)semaforo.o: $(DIRHEAD)semaforo.c 
	$(CC) $(CFLAGS) $(DIRHEAD)semaforo.c -o $(DIRHEAD)semaforo.o

$(DIRHEAD)memotec.o: $(DIRHEAD)memotec.c 
	$(CC) $(CFLAGS) $(DIRHEAD)memotec.c -o $(DIRHEAD)memotec.o

servidor.o: servidor.c $(HEAD)
	$(CC) $(CFLAGS) servidor.c -o servidor.o


