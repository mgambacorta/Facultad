.PHONY: clean compilar ejecutar gprof_call gprof_time

EJEMPLO=pi
NUM=2

CFLAGS := -std=gnu99 -g -pg -I../comun
OBJS   := $(EJEMPLO).o comun.o mmio.o

#-----------------------------------------------------------------------

all:compilar

clean:
	del /F /Q $(OBJS) $(EJEMPLO) $(EJEMPLO).exe gmon.* *.bak

compilar: $(OBJS)
	gcc $(CFLAGS) -fopenmp -o $(EJEMPLO) $(OBJS) -lm

$(EJEMPLO).o:$(EJEMPLO).c
	gcc $(CFLAGS) -fopenmp -c $(EJEMPLO).c

%.o: ../comun/%.c
	gcc -c -o $@ $< $(CFLAGS)

#-----------------------------------------------------------------------

ejecutar:
	set OMP_NUM_THREADS=$(NUM) & $(EJEMPLO).exe

gprof_call:
	gprof -b $(EJEMPLO).exe -q -b

gprof_time:
	gprof -b $(EJEMPLO).exe -Q -b

#-----------------------------------------------------------------------
