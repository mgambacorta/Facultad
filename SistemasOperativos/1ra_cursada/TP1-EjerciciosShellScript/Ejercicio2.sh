#!/bin/bash
######################################################
# Trabajo Practico Nº 1 - Ejercicio Nº 2             #
######################################################
#                                                    #
# Nombre:  Ejercicio2.sh                             #
# Autores: Mariano Gambacorta DNI: 29279015          #
# Entrega: 1º entrega                                #
# Fecha:                                             #
#                                                    #
######################################################


## Funcion que muestra los modos de uso
function ayuda()
{
	echo "Modos de uso:"
	echo "\t$0 -h : Muestra un mensaje de ayuda"
	echo "\t$0 -a <1ºnumero> [<2ºnumero> ... <nºnumero>] : Ordena los numeros pasados por parametro en forma ascendente"
	echo "\t$0 -d <1ºnumero> [<2ºnumero> ... <nºnumero>] : Ordena los numeros pasados por parametro en forma descendente"
}

## Validando parametros
if [ $# -lt 2 ] && [[ $1 != "-h" ]]
then 
	echo "Error de parametros"
	ayuda
	exit 1
fi

## Valido el primer parametro
case $1 in
	-a) COND="-n";;  ## le indica al sort que ordene en forma numerica
	-d) COND="-nr";; ## le indica al sort que ordene en forma numerica e inversa
	-h) ayuda; exit 0;;
	*) echo "Error de parametros"
		ayuda
		exit 1;;
esac

## Elimino el primer parametro
shift 1

## Valido que el resto de los parametros sea numerico
for NUM in $*
do
	if ! [ $NUM -eq $NUM ] 2>/dev/null ## Si no fuese numerico el test no anda y termina en false
	then 
		echo "$NUM no es un valor numerico. Para obtener ayuda usar: $0 -h"
		exit 2
	fi
done

## recorro los parametros y despues los ordeno segun la condicion correspondiente
ARRAY=$(for VAL in $*; do echo $VAL; done | sort $COND)

## Imprimo el resultado
echo $ARRAY

## EOF ##