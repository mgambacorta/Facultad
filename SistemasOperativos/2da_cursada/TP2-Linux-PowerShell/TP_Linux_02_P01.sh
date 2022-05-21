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
  echo "Usage: $0 filename delimiter"
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
delimitador=$2

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

echo "PRIMER: -"$1"-"
echo "SEGUNDO: -"$2"-"

# Ejecuta la logica que se pidio
awk -F$delimitador -v archivo=$archivo '
                     # Arma los nombres de los archivos de salida
                     BEGIN { i = index(archivo, ".")
                                      if(i == 0) {
                                         archivoOK = archivo ".ok"
                                         archivoERR = archivo ".err"
                                      } else {
                                         archivoOK = substr(archivo,0,i) ".ok"
                                         archivoERR = substr(archivo,0,i) ".err"
                                      }
                                    }

                     # Con el primer registro se inicializa vaiables y archivos
                     NR==1 { cantCampos = NF
                                       for(i = 1; i <= cantCampos; i++) {
                                         titulo[i] = $i
                                       }
                                       print $0> archivoOK
                                       print $0 ";ERROR"> archivoERR
                                     }

                     # Procesa todos los registro, excepto el primero
                     NR>1 { error = 0

                                   # Valida si hay campos vacios, que tengan titulos
                                    for(i = 1; i <= cantCampos; i++) {
                                      if($i == "") {
                                        print $0 ";El campo " titulo[i] " está vacío">> archivoERR
                                        error = 1
                                      }
                                    }

                                   # Valida si la cantidad de titulos es diferente a la cantidad de campos del registro actual
                                    if(cantCampos  !=  NF) {
                                      if(NF - cantCampos == 1) {
                                        print $0 ";El registro tiene 1 campo de más">> archivoERR
                                      } else {
                                        print $0 ";El registro tiene " (NF - cantCampos) " campos de más">> archivoERR
                                      }
                                      error = 1
                                    }

                                   # Valida si se retipe el indice
                                    if($1 in vectorIndice) {
                                      # Valida si imprimio la primer instancia de duplicado
                                      if(!($1 in vectorPrimerInstancia)) {
                                        print vectorIndice[$1] ";Clave duplicada">> archivoERR
                                        vectorPrimerInstancia[$1]=$0
                                      }
                                      print $0 ";Clave duplicada">> archivoERR
                                      error = 1
                                    }
                                    vectorIndice[$1]=$0

                                   # Valida si hubo errores
                                    if(error ==  0) {
                                      print $0 >> archivoOK
                                    }
                                  }' $archivo
