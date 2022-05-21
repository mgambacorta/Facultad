#!/bin/bash
######################################################
# Trabajo Practico Nº 1 - Ejercicio Nº 1             #
######################################################
#                                                    #
# Nombre:  Ejercicio1.sh                             #
# Autores: Mariano Gambacorta DNI: 29279015          #
# Entrega: 1º entrega                                #
# Fecha:                                             #
#                                                    #
######################################################

if test $# -lt 2
then
	echo "Error de parametros, modo de uso:" # Linea modificada
	echo "\t$0 <archivo_entrada> <patron_1> [<patron_2> ... <patron_n>]" # Linea modificada
	exit 1;
fi

if ! test -f $1
then
	echo "Error de parametros, $1 no es un archivo valido" # Linea modificada
	exit 1;
fi

TOT=$#
((TOT-=2))
FILE=$1
shift 1
COUNTER=0

while test $COUNTER -le $TOT
do
	SET[$COUNTER]=$1
	shift 1
	((COUNTER++))
done

for X in "${SET[@]}"
do
	CANT=0
	for WORD in `cat $FILE`
	do
		if [[ "$WORD" == "$X" ]]
		then
			((CANT++))
		fi
	done
	echo "$X : $CANT"
done

exit 0

#########################################################################
## CUESTIONARIO
#########################################################################
## a) Analice el código y complete las líneas que tienen puntos suspensivos.
##		ver lineas 15, 16 y 22
## b) El script necesita parámetros. ¿Cuáles son?
##		Requiere un archivo y al menos una palabra para buscar dentro
## c) Analice los resultados al ejecutar el script, ¿Que significa lo que se muestra por pantalla?.
##		Muestra la cantidad de veces que aparece cada palabra dentro del archivo
## d) Explique el objetivo general.
##		Recibe como parametro un archivo y N palabras e imprime por pantalla la cantidad de veces que aparecen las
##		palabras en el archivo
## e) Investigue y redacte en pocas líneas sobre cada uno de los comandos: shift, test y cat.
##		shift: Desplaza los parametro la cantidad de lugares que se le pase por parametro (1 por defecto)
##		test: Evalua expresiones condicionales y devuelve 0(false) o 1(true) si la condicion evaluada se cumple o no
##		cat: Imprime for salida estandar el contenido de un archivo, variable, etc.
## f) Explique cómo se utilizan en bash las sentencias if, for y while.
##		if: if <condicion>; then <comandos>; [elif <condicion>; then <comandos>;] [else; <comandos>;] fi
##		for: for <variable> in <coleccion>; do <comandos>; done
##		while: while <condicion>; do <comandos>; done
#########################################################################

## EOF ##


