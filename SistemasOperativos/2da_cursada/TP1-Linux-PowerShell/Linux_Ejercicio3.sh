#!/bin/bash
######################################################
# Trabajo Practico Nº 1 - Ejercicio Nº 3             #
######################################################
#                                                    #
# Nombre:  Ejercicio1.sh                             #
# Autores: Alvarez, Patricio                         #
#          Gambacorta, Mariano                       #
#          Quintana, Carlos                          #
#          Salas, Miguel Angel                       #
#          Silva, Hugo                               #
# Entrega: 1º reentrega                              #
# Fecha:   06/05/2015                                #
#                                                    #
######################################################

## Funcion que muestra un mensaje de ayuda
function help()
{
	echo "Modo de uso:"
	echo "---> $0 -h:"
	echo "            Muestra este mensaje de ayuda"
	echo "---> $0 <path_entrada> [<path_salida>]: "
	echo "            <path_entrada>: directorio donde se encuentran los archivos de entrada"
	echo "            <path_salida>: (opcional) directorio dode se ubicara el archivo de salida,"
	echo "                           en caso de no ser informado se utilizara el directorio de ejecucion"
	exit $1
}

FECHA_HORA=$(date "+%d%m%Y.%H%M%S")
ARCH_LOG=$PWD/$FECHA_HORA.log

> $ARCH_LOG

## Valido que exista al menos un parametro
if [ $# -lt 1 ]
then
	echo "Error en los parametros" >> $ARCH_LOG
	help 1
fi

## si el primer parametro es "-h" muestro la ayuda y salgo
if [[ "$1" == "-h" ]]
then
	help 0
fi

## valido que $1 sea un directorio
if ! [ -d "$1" ] || ! [ -r "$1" ]
then
	echo "$1 no es un directorio valido o no tiene permisos de lectura" >> $ARCH_LOG
	help 2
fi	

## seteo el directorio de los archivos de entrada
DIR_IN=$1

## seteo el directorio del archivo de salida
if [ -d "$2" ]
then
	DIR_OUT=$2
else
	echo "No se especifico un directorio de salida o el directorio especificado no es valido" >> $ARCH_LOG
	echo "Se utilizara directorio por defecto: $PWD" >> $ARCH_LOG
	DIR_OUT=$PWD
fi

ARCH_OUT=$DIR_OUT/file.$FECHA_HORA
echo "Archivo;Fecha;Materia;Comisión;Apellido;Nombre;Usuario;" > $ARCH_OUT

cd $DIR_IN
for ARCH_IN in $(ls -1 | grep -v "procesado")
do
	## si el archivo no tiene permisos no puedo hacer nada
	if ! [ -r $ARCH_IN ]
	then
		echo "El archivo $ARCH_IN no tiene permisos de lectura, se ignorara y se seguira procesando el resto" >> $ARCH_LOG
		continue
	fi

	FECHA=$(cat $ARCH_IN | grep -i fecha | cut -d';' -f2)
	if [ -z "$FECHA" ]
	then 
		echo "El archivo $ARCH_IN no tiene fecha informada, se ignorara y se seguira procesando el resto" >> $ARCH_LOG
		continue
	fi
	
	MATERIA=$(cat $ARCH_IN | grep -i materia | cut -d';' -f2)
	if [ -z "$MATERIA" ]
	then 
		echo "El archivo $ARCH_IN no tiene materia informada, se ignorara y se seguira procesando el resto" >> $ARCH_LOG
		continue
	fi
	
	## cuento la cantidad de lineas del archivo y le resto las 4 primeras lineas
	LONGITUD=$(cat $ARCH_IN | wc -l)
	LONGITUD=$(expr $LONGITUD - 3)
	
	## Paso los datos al archivo de salida
	for LINEA in $(tail -$LONGITUD $ARCH_IN)
	do
		echo "$ARCH_IN;$FECHA;$MATERIA;$LINEA" >> $ARCH_OUT
	done
	
	## renombro el archivo
	mv $ARCH_IN $ARCH_IN.procesado 2>/dev/null
	
	if [ $? -ne 0 ]
	then
		echo "No se pudo renombrar el archivo $ARCH_IN a $ARCH_IN.procesado" >> $ARCH_LOG
	fi
	
done

## EOF ##
