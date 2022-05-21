#!/bin/bash
######################################################
# Trabajo Practico Nº 1 - Ejercicio Nº 3             #
######################################################
#                                                    #
# Nombre:  Ejercicio3.sh                             #
# Autores: Mariano Gambacorta DNI: 29279015          #
# Entrega: 1º entrega                                #
# Fecha:                                             #
#                                                    #
######################################################

## Valido que haya al menos un parametro
if [ $# -lt 1 ]
then
	echo "Errror de parametros,modo de uso:"
	echo "$0 <archivo>"
	exit 1
fi

## Valido que el parametro sea un archivo
if [ -f $1 ]
then
	ARCH=$1
else
	echo "El erchivo $1 no existe"
	exit 2
fi

## si el archivo esta vacio no puedo hacer nada
if ! [ -s $ARCH ]
then
	echo "El archivo $ARCH esta vacio"
	exit 3
fi

## creo un array con los valores ordenados, unicos y en mayusculas 
ARRAY_UNICOS=$(cat $ARCH | tr 'a-z' 'A-Z' | sort | uniq)

## recorro el array, cuento la cantidad de ocurrencias (ignorando mayusculas y minusculas) y las informo
for VAL in $ARRAY_UNICOS
do
	TOTAL=$(cat $ARCH | grep -i $VAL | wc -l)
	echo "$VAL($TOTAL)"
done

## EOF ##
