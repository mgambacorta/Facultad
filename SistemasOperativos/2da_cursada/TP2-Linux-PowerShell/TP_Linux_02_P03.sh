#!/bin/bash
######################################################
# Trabajo Practico Nº 2 - Ejercicio Nº 1             #
######################################################
#                                                    #
# Nombre:  EjercicioN.sh                             #
# Autores: DNI: 30577162 Alvarez, Patricio           #
#          DNI: XXXXXXXX Gambacorta, Mariano         #
#          DNI: 30466883 Quintana, Carlos Carlos     #
#          DNI: 40459956 Salas, Miguel Angel         #
#          DNI: 31554105 Silva, Hugo Ricardo         #
# Entrega: 1º entrega                                #
# Fecha:   11/05/2015                                #
#                                                    #
######################################################

# Imprime la ayuda y sale del programa
function usage(){
  echo "Usage: $0 filename key value"
  echo "Usage: $0 -?"
  echo "Usage: $0 -h"
  echo "Usage: $0 -help"
  exit 0
}

# Determina si se pidio ayuda
if test "-?" = "$1" || test "-h" = "$1" || test "-help" = "$1"; then
  usage
fi

# Setea variables
archivo=$1
clave=$2
valor=$3

TFILE="/tmp/out.tmp.$$"

# Verifica que el i-nodo exista
if ! test -e "$archivo"; then
  echo "File does not exist: $archivo" >&2
  exit 1
fi
# Verifica que sea un archivo
if ! test -f "$archivo"; then
  echo "It is not a file: $archivo" >&2
  exit 1
fi
# Verifica que se tenga permisos de lectura
if ! test -r "$archivo"; then
  echo "No read permission: $archivo" >&2
  exit 1
fi
# Verifica que se tenga permisos de escritura
if ! test -w "$archivo"; then
  echo "No write permission: $archivo" >&2
  exit 1
fi

contiene=`sed -n "/^[' ']*$clave=/p" $archivo`
if test -z "$contiene"; then
  sed -i "$ a $clave=$valor # user: $USER, date: $(date +'%m-%d-%Y %T')" "$archivo"
else
  sed "s/^[' ']*$clave=/$clave=$valor # user: $USER, date: $(date +"%m-%d-%Y %T"), old_value: /g" "$archivo" > $TFILE && mv $TFILE "$archivo"
fi
