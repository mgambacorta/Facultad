.PHONY: clean compilar ejecutar gprof_call gprof_time

EJEMPLO=mandelbrot

CFLAGS:=-std=gnu99 -g -pg -I../comun
OBJS   := $(EJEMPLO).o mmio.o comun.o 

#-----------------------------------------------------------------------

all:compilar

clean:
	del /F /Q $(OBJS) $(EJEMPLO).exe $(EJEMPLO).exe gmon.*

compilar: $(OBJS)
	gcc $(CFLAGS) -o $(EJEMPLO).exe $(OBJS) -lm

$(EJEMPLO).o:$(EJEMPLO).c
	g++ $(CFLAGS) -c $(EJEMPLO).c

%.o: ../comun/%.c
	gcc  $(CFLAGS) -c -o $@ $< 

#-----------------------------------------------------------------------

ejecutar:
	./$(EJEMPLO).exe

gprof_call:
	gprof -b $(EJEMPLO).exe -q -b

gprof_time:
	gprof -b $(EJEMPLO).exe -Q -b

#-----------------------------------------------------------------------
