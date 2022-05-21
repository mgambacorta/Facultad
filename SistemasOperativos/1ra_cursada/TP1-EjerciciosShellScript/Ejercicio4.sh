#!/bin/bash
######################################################
# Trabajo Practico Nº 1 - Ejercicio Nº 4             #
######################################################
#                                                    #
# Nombre:  Ejercicio4.sh                             #
# Autores: Mariano Gambacorta DNI: 29279015          #
# Entrega: 1º entrega                                #
# Fecha:                                             #
#                                                    #
######################################################

## Funcion para validar la fecha
function validaFecha()
{
	## Valido que el anio sea numerico
	if ! [ $3 -eq $3 ] 2>/dev/null ## Si no fuese numerico el test no anda y termina en false
	then
		return 1
	fi
	
	## Si viene con dos digitos, le agrego por defecto 20xx
	if [ $3 -lt  ]
}


## Valido la cantidad de parametros
if [ $# -eq 0 ] || [ $# -gt 2 ]; then
    echo "Error de parametros, modo de uso:"
    echo "$0 <archivo> [<fecha>]"
    exit 1
fi

## Valido que el archivo exista y tenga datos
if ! [ -f $1 ] || ! [ -s $1 ]
then
    echo "El archivo $1 no existe o esta vacio"
    exit 2
fi

## Valido la fecha si la recibi, sino seteo la del sistema
if [ "$2" != "" ]
then
	DD=$(echo $2 | cut -d'/' -f1)
	MM=$(echo $2 | cut -d'/' -f2)
	AA=$(echo $2 | cut -d'/' -f3)

    validaFecha $DD $MM	$AA
	
	## TODO
   
else
	## Traigo la fecha del sistema
    FECHA=$(date +"%d %m %Y")
	
	DD=$(echo $2 | cut -d' ' -f1)
	MM=$(echo $2 | cut -d' ' -f2)
	AA=$(echo $2 | cut -d' ' -f3)
fi

## divido la fecha en tres variables distintas para pasarsela al awk

awk -v dia=$DD -v mes=$MM -v anio=$AA '
    BEGIN   {
                FS=":";
                coincidencias = 0;
                print "Usuarios creados el " dia "/" mes "/" anio;
                print "Comision\tApellido y Nombre\tUsuario";
                fecha1 = dia"/"mes"/"(anio % 100);		## Me armo los tres patrones de busqueda
                fecha2 = dia " de " mes " de " anio;
                fecha3 = dia"-"mes"-"anio;
            }

    ($8 == fecha1)  {
                        split($6,comision,"/");
                        split($5,alumno,",");
                        print comision[4],"\t",alumno[1],"\t",$1
                        coincidencias ++;
                    }

    ($8 == fecha2)  {
                        split($6,comision,"/");
                        split($5,alumno,",");
                        print comision[4],"\t",alumno[1],"\t",$1
                        coincidencias ++;
                    }

    (substr($8,0,11) == fecha3) {
                                    split($6,comision,"/");
                                    split($5,alumno,",");
                                    print comision[4],"\t",alumno[1],"\t",$1
                                    coincidencias ++;
                                }
    END {
            if (coincidencias == 0)
            {
                print "No hubo resultados"
            }
        }
' $1

## EOF ##
