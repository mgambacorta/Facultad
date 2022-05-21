.PHONY: clean compilar ejecutar gprof_call gprof_time

EJEMPLO=pi

CFLAGS := -std=gnu99 -g -pg -I../comun
OBJS   := $(EJEMPLO).o comun.o mmio.o

all:compilar

#-----------------------------------------------------------------------

clean:
	del /F /Q $(OBJS) $(EJEMPLO) $(EJEMPLO).exe gmon.*  *.bak

compilar: $(OBJS)
	gcc $(CFLAGS) -o $(EJEMPLO) $(OBJS) -lm

$(EJEMPLO).o:$(EJEMPLO).c
	gcc $(CFLAGS) -c $(EJEMPLO).c

%.o: ../comun/%.c
	gcc -c -o $@ $< $(CFLAGS)

#-----------------------------------------------------------------------

ejecutar:
	$(EJEMPLO).exe

gprof_call:
	gprof -b $(EJEMPLO).exe -q -b

gprof_time:
	gprof -b $(EJEMPLO).exe -Q -b

#-----------------------------------------------------------------------
