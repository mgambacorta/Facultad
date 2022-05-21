#!/bin/bash
######################################################
# Trabajo Practico Nro 1 - Ejercicio Nro 3           #
######################################################
#                                                    #
# Nombre:  Ejercicio3.sh                             #
# Autores: Mariano Gambacorta DNI: 29279015          #
#          Pablo Carnovale    DNI: 33116185          #
#          Fabian Mancini     DNI: 36530292          #
#          Marcelo Romero     DNI: 34140911          #
# Entrega: 2da entrega                               #
# Fecha:   06/10/2014                                #
#                                                    #
######################################################
# MODIFICACIONES:                                    #
#                                                    #
# 2da entrega: Se modifica la generacion de          #
#              ARRAY_UNICOS para contemplar lineas   #
#              con espacios.                         #
######################################################

## Archivo temporal
#TEMPORAL=/tmp/Ejercicio3.tmp

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
ARRAY_UNICOS=$(cat $ARCH | tr 'a-z' 'A-Z' | sort | uniq | tr '\n' ';') ## Modificacion: Agrego ';' para separar los campos

export IFS=';'

## recorro el array, cuento la cantidad de ocurrencias y las informo
for VAL in $ARRAY_UNICOS
do
	TOTAL=$(cat $ARCH | grep -i $VAL | wc -l)
	echo "$VAL($TOTAL)"
done

## EOF ##
