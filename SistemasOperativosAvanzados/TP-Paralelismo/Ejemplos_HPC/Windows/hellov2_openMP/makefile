.PHONY: clean compilar ejecutar gprof_call gprof_time

EJEMPLO=hello
NUM=2

all:compilar

clean:
	del /F /Q $(EJEMPLO).o $(EJEMPLO) $(EJEMPLO).exe gmon.* *.bak

compilar: $(EJEMPLO).o
	gcc -fopenmp -g -pg -o $(EJEMPLO) $(EJEMPLO).o

$(EJEMPLO).o:$(EJEMPLO).c
	gcc -fopenmp -c -g -pg $(EJEMPLO).c

ejecutar:
	set OMP_NUM_THREADS=$(NUM) & $(EJEMPLO).exe

gprof_call:
	gprof -b $(EJEMPLO).exe -q -b

gprof_time:
	gprof -b $(EJEMPLO).exe -Q -b
