 .PHONY: clean compilar ejecutar gprof_call gprof_time

EJEMPLO=hello
NUM=2
MPI_HDR="C:\Program Files\Microsoft HPC Pack 2012\Inc"
MPI_LIB="C:\MinGW\lib\gcc\mingw32\6.3.0"

CFLAGS := -std=gnu99 -g -pg -I../comun -I$(MPI_HDR)
OBJS   := $(EJEMPLO).o comun.o mmio.o

#-----------------------------------------------------------------------

all:compilar

clean:
	del /F /Q $(OBJS) $(EJEMPLO).exe $(EJEMPLO).exe gmon.*

compilar: $(OBJS)
	gcc $(CFLAGS) -o $(EJEMPLO).exe $(OBJS) -lm -L$(MPI_LIB) -lmsmpi

$(EJEMPLO).o:$(EJEMPLO).c
	gcc $(CFLAGS) -c $(EJEMPLO).c

%.o: ../comun/%.c
	gcc -c -o $@ $< $(CFLAGS)

#-----------------------------------------------------------------------

ejecutar:
	mpiexec -np $(NUM) $(EJEMPLO).exe

gprof_call:
	gprof -b $(EJEMPLO).exe -q -b

gprof_time:
	gprof -b $(EJEMPLO).exe -Q -b

#-----------------------------------------------------------------------
