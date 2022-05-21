.PHONY:  clean compilar ejecutar gprof_call gprof_time

EJEMPLO=pi
NUM=2
MPI_HDR="C:\Program Files\Microsoft HPC Pack 2012\Inc"
MPI_LIB="C:\TDM-GCC-64\lib\gcc\x86_64-w64-mingw32\5.1.0"

CFLAGS := -std=gnu99 -g -pg -I../comun -I$(MPI_HDR)
OBJS   := $(EJEMPLO).o comun.o mmio.o


all:compilar

#-----------------------------------------------------------------------

clean:
	del /F /Q $(OBJS) $(EJEMPLO) $(EJEMPLO).exe gmon.* *.bak

compilar: $(OBJS)
	gcc $(CFLAGS) -o $(EJEMPLO) $(OBJS) -L$(MPI_LIB) -lmsmpi -lm

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
