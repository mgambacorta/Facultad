C:\Compiladores\GnuWin32\bin\flex Lexico.l
pause
C:\Compiladores\GnuWin32\bin\bison -dyv Sintactico.y
pause
C:\Compiladores\MinGW\bin\gcc.exe lex.yy.c y.tab.c asmGen.c tercetos.c tablaSimbolos.c -o Grupo29.exe
pause
del lex.yy.c
del y.tab.c
del y.output
del y.tab.h
pause


