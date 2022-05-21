#!/bin/bash
######################################################
# Trabajo Practico Nro 1 - Ejercicio Nro 4           #
######################################################
#                                                    #
# Nombre:  Ejercicio4.sh                             #
# Autores: Mariano Gambacorta DNI: 29279015          #
#          Pablo Carnovale    DNI: 33116185          #
#          Fabian Mancini     DNI: 36530292          #
#          Marcelo Romero     DNI: 34140911          #
# Entrega: 2da entrega                               #
# Fecha:   06/10/2014                                #
#                                                    #
######################################################
#                                                    #
# MODIFICACIONES 2da ENTREGA: Se agrega validacion   #
#                             para que eliminar del  #
#                             informe a los docentes #
#                                                    #
######################################################

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
    VALID_DATE_UNO=$(echo $2 | grep -c '^[0-9][0-9]/[0-9][0-9]/[0-9][0-9]$')
    VALID_DATE_DOS=$(echo $2 | grep -c '^[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]$')
   
    if [ $VALID_DATE_UNO -eq 0 ] && [ $VALID_DATE_DOS -eq 0 ]
    then
        echo "Fecha invalida"
        exit 3
    fi

	## Genero una fecha desde el segundo parametro
    FECHA=$(date -d ${2:6:4}"-"${2:3:2}"-"${2:0:2} +"%Y %m %d")
else
	## Trigo la fecha del sistema
    FECHA=$(date +"%Y %m %d")
fi

## divido la fecha en tres variables distintas para pasarsela al awk
AA=$(echo $FECHA | cut -d' ' -f1)
MM=$(echo $FECHA | cut -d' ' -f2)
DD=$(echo $FECHA | cut -d' ' -f3)

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
						if( comision[4] != "docentes" ) ## Modificacion 2da entrega
						{
							print comision[4],"\t",alumno[1],"\t",$1
							coincidencias ++;
						}
                    }

    ($8 == fecha2)  {
                        split($6,comision,"/");
                        split($5,alumno,",");
						if( comision[4] != "docentes" ) ## Modificacion 2da entrega
						{
							print comision[4],"\t",alumno[1],"\t",$1
							coincidencias ++;
						}
                    }

    (substr($8,0,11) == fecha3) {
                                    split($6,comision,"/");
                                    split($5,alumno,",");
									if( comision[4] != "docentes" ) ## Modificacion 2da entrega
									{
										print comision[4],"\t",alumno[1],"\t",$1
										coincidencias ++;
									}
                                }
    END {
            if (coincidencias == 0)
            {
                print "No hubo resultados"
            }
        }
' $1

## EOF ##
