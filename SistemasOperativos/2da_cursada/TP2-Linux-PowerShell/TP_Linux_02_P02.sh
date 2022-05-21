#!/bin/bash

work=$PWD
dirlog=$work/logs
fecha=`date +"%Y%m%d%H%M%S"`

function Ayuda {
	echo ""
	echo "AYUDA:"
	echo "	Este script es un demonio planificador a largo plazo (launcher) de otros ejecutables existentes en un determinado directorio."
	echo "	SYNOPSIS"
	echo "		./TP_Linux_02_P02.sh <<ACCION>> <<DIRECTORIO_EJECUTABLES>> <<CANTIDAD_PROCESOS>>"
	echo "	<<ACCION>>"
	echo "		- start: para iniciar el demonio"
	echo "		- stop: para apagar el demonio"
	
	exit 0
}

function EjecutarProcesos {
	echo "Archivos:"
	directorio="$1"
	for arch in $(ls $directorio)
	do
		if [ -f $directorio/$arch ] && [ -x $directorio/$arch ] ## AGREGADO: que solo ejecute los que tienen permiso x
		then
			echo "EJECUTANDO: $arch. log de salida: $dirlog/$arch.$fecha.log"
			$directorio/$arch > $dirlog/$arch.$fecha.log
		fi
	done
}

function MatarProcesos {
	exit 0
}
function VerificarDemonio {
	q=`ps -ef | grep $0 | grep -v "grep" | grep -v $$ | wc -l`

	if [ $q != "0" ]; then
		echo "Otra instancia de $0 está ejecutando..."
		exit 1
	fi
}

function EncenderDemonio {
	VerificarDemonio
	echo "Comienza demonio..."

	main $1 $2

	pid=$! ## Con esto capturo el pid del proceso que acabo de generar en background
	echo "PID=$pid"
}

function ApagarDemonio {
	echo "Terminando demonio..."
	kill `ps -ef | grep $0 | grep -v $$ | grep -v "grep" | awk '{print($2)}'`
}

function main {
	{
		while [ 1 ]; do
			a=1
			trap "EjecutarProcesos $1 $2" 30 10 16
			trap "MatarProcesos $1" 15  
		done
	} &
}


case $1 in
	"start")			
		vError=0
		# Verifico si no se pasan parámetros
		if [[ $# == 0 ]]
		then
			echo "Debe ingresar parámetros."
			vError=1			
		else
			if [[ $# < 3 ]]
			then
				echo "Faltan ingresar parámetros."
				vError=1
			else
				if [ ! -d $2 ] 
				then
					echo "$2 no es un directorio válido."
					vError=1	                	
				fi
			
			fi
		fi
		if [ $vError == 1 ]
		then
			Ayuda
		fi
		
		EncenderDemonio $2 $3 $0 $1
	;;
	"stop")
		ApagarDemonio
	;;
	*)
		Ayuda
	;;
esac
