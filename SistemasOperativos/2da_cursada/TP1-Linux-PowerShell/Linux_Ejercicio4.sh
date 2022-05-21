#!/bin/bash
######################################################
# Trabajo Practico Nº 1 - Ejercicio Nº 4             #
######################################################
#                                                    #
# Nombre:  Ejercicio4.sh                             #
# Autores: Alvarez, Patricio                         #
#          Gambacorta, Mariano                       #
#          Quintana, Carlos                          #
#          Salas, Miguel Angel                       #
#          Silva, Hugo                               #
# Entrega: 1º reentrega                              #
# Fecha:   06/05/2015                                #
#                                                    #
######################################################

function help()
{
	echo "Modo de uso:"
	echo "---> $0 -h:"
	echo "            Muestra este mensaje de ayuda"
	echo "---> $0 <path_entrada> [<archivo 1> ... <archivo n>]: "
	echo "            Convierta a mayuscula los nombres de todos los archivos con extension \".sh\" o \".log\" "
	echo "            de un directorio recibido como parámetro"
	echo "     - <path_entrada>: directorio donde se encuentran los archivos de entrada"
	echo "     - <archivo>: (opcional) son archivos del directorio que no seran modificados los cuales deberan"
	echo "                  informarse con el path relativo al directorio del primer parametro "
	exit $1
}

## Busca el los archivos en la lista de exentos
function busca_exento()
{
	for VAL in $ARRAY_EXENTOS
	do
		if [[ "$1" == "$VAL" ]]
		then
			echo "El archivo $1 se marco como exento por parametro, no se modificara"
			return 1
		fi
	done
}

## PRINCIPAL ##

## Valido que exista al menos un parametro
if [ $# -lt 1 ]
then
	echo "Error en los parametros"
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
	echo "$1 no es un directorio valido o no tiene permisos de lectura"
	help 2
fi	

## seteo el directorio de los archivos de entrada
cd $1
DIR_IN=$PWD ## este parche es porque no se si en el parametro recivi un path absoluto o relativo

## $1 ya no me interesa, lo saco de la lista de parametros
shift

## cargo la lista de exentos con path absoluto
for EXE in $*
do
	ARRAY_EXENTOS="$ARRAY_EXENTOS $DIR_IN/$EXE"
done

## recorro los archivos en forma recursiva
# TODO: probar si funciona: 
for ARCH in $(find $DIR_IN -iname "*.sh" -o -iname "*.log" 2>/dev/null)
do
	## Primero me fijo que no este en el grupo de exentos
	busca_exento $ARCH
	if [ $? -ne 0 ]; then continue; fi
	
	## Calculo la longitud del directorio para extraer el substring del nombre
	LONG_TOT=$(echo $ARCH | wc -c)
	LONG_NOM=$(basename $ARCH | wc -c)
	LONG_DIR=$(expr $LONG_TOT - $LONG_NOM)
	
	## Extraigo por separado el directorio, el nombre del archivo sin extension y la extension
	DIR=${ARCH:0:$LONG_DIR}
	EXT=$(basename $ARCH | cut -d'.' -f2)
	NOM=$(basename $ARCH .$EXT)
	
	## paso el nombre a mayusculas
	NOM=$(echo $NOM | tr "a-z" "A-Z")
	
	## cambio el nombre al archivo
	ARCH_NEW=$DIR/$NOM.$EXT
	
	## si ya esta en mayuscula no hago nada
	if [ $ARCH -eq $ARCH_NEW ]
	then
		continue
	fi
	
	## si ya existe con mayuscula no lo puedo renombrar
	if [ -f $ARCH_NEW ]
	then 
		echo "El archivo $ARCH_NEW ya existe, $ARCH no se modificara"
		continue
	fi
	
	mv $ARCH $ARCH_NEW 2>/dev/null
	
	## Si no pude cambiar el nombre lo informo y sigo con el proximo
	if [ $? -ne 0 ]
	then
		echo "Error al modificar el nombre del archivo $ARCH, se continua con el siguiente"
		continue
	fi
	
	## Si es un .sh le agrego el string de modificado, solo si tiene permisos
	case $EXT in
		"sh"|"Sh"|"sH"|"SH")
			FECHA=$(date +"%d/%m/%Y a las %H:%M hs")
			
			if [ -w $ARCH_NEW ]
			then
				echo "" >> $ARCH_NEW 
				echo "## Archivo modificado el $FECHA" >> $ARCH_NEW
				echo "" >> $ARCH_NEW
			else
				echo "No se puede agregar el string de modificacion en el archivo $ARCH_NEW por que el mismo no tiene permisos"
			fi
		;;
	esac
done
