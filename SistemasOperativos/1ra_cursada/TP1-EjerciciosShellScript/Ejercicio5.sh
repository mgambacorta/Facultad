#!/bin/bash
######################################################
# Trabajo Practico Nº 1 - Ejercicio Nº 5             #
######################################################
#                                                    #
# Nombre:  Ejercicio5.sh                             #
# Autores: Mariano Gambacorta DNI: 29279015          #
#          Pablo Carnovale    DNI: 33116185          #
#          Fabian Mancini     DNI:                   #
# Entrega: 1º entrega                                #
# Fecha:   15/09/2014                                #
#                                                    #
######################################################

## Funcion que muestra los modos de uso
function ayuda()
{
	echo "Modos de uso:"
	echo "\t$0 -h : Muestra un mensaje de ayuda"
	echo "\t$0 <archivo> -p : Muestra el numero total de alumnos y un reporte por cada parcial"
	echo "\t$0 <archivo> -a : Muestra para cada alumno el promedio y condición"
	echo "\t$0 <archivo> -a <DNI> : Muestra las notas de cada parcial del alumno indicado, su nombre, dni, el promedio y condicion"
}

## Funcion que realiza el reporte de parciales
function reporte_parcial()
{
	awk ' BEGIN { FS=","; }
					{ alumno[$3]; } ## Armo un array con los DNI como clave y sin datos
		($1 == "1") { 
						## Cuento la cantidad de promocionados, cursados o aplazados para el primer parcial
						if ( $4 >= 7 ) primero["Promocion"]++; 
						else if ( $4 >= 4 ) primero["Cursada"]++;
						else primero["Desaprobado"]++;
						
						## Cuento cantidad de examenes y total de las notas para despues hacer el promedio
						cant_1++;
						total_1 += $4;
					}
		($1 == "2") { 
						## Cuento la cantidad de promocionados, cursados o aplazados para el segundo parcial
						if ( $4 >= 7 ) segundo["Promocion"]++;
						else if ( $4 >= 4 ) segundo["Cursada"]++;
						else segundo["Desaprobado"]++;
						
						## Cuento cantidad de examenes y total de las notas para despues hacer el promedio
						cant_2++;
						total_2 += $4;
					}
		END {
				## Calculo la cantidad de alumnos
				for ( i in alumno ) cant_a++;
				
				## Imprimo los resultados
				printf ( "Alumnos: %d\n", cant_a );
				printf ( "Parcial 1: Promocionados: %d, entre 4 y 6: %d, Desaprobados: %d, Promedio: %1.1f\n",
							primero["Promocion"], primero["Cursada"], primero["Desaprobado"], total_1/cant_1);
				printf ( "Parcial 2: Promocionados: %d, entre 4 y 6: %d, Desaprobados: %d, Promedio: %1.1f\n",
							segundo["Promocion"], segundo["Cursada"], segundo["Desaprobado"], total_2/cant_2);
			}
	' $ARCH
}

##funcion que realiza el reporte de todos los alumnos
function reporte_alumnos()
{
	awk ' BEGIN { FS=","; }
					{ nombre[$3] = $2 } ## Me guardo en un vector los nombres con el DNI como clave
		($1 == "1")	{ parcial1[$3] = $4 } ## Guardo la nota del primer parcial
		($1 == "2")	{ parcial2[$3] = $4 } ## Guardo la nota del segundo parcial
		END {
				for ( key in nombre ) ## Recorro cualquiera de los tres vectores, total la clave es la misma siempre
				{
                    ## Imprimo el nombre y DNI
                    printf ( "%s, DNI: %s, ", nombre[key], key );

                    if ( parcial1[key] >= 7 && parcial2[key] >= 7 ) ## Si promociono los dos esta promocionado
                        print ( "Materia promocionada" );
                    else if ( parcial1[key] < 4 || parcial2[key] < 4 ) ## Si desaprobo alguno de los dos esta desaprobado
                        print ( "Materia desaprobada" );
                    else
                        print ( "Materia cursada" ); ## Si no se dieron ninguno de los casos anteriores, esta cursada
				}
			}
	' $ARCH
}

## funcion que realiza un reporte para un unico alumno
function reporte_x_alumno()
{
	## Ejecuto el awk con la variable que recibi como parametro
    awk -v dni=$1 '
        BEGIN   {
                    FS=",";

                    ## Inicializo las variables
                    nombre = "";
                    parcial1 = 0;
                    parcial2 = 0;
                }
        ($3 == dni) {
                        nombre = $2; ## Me guardo el nombre
                        if ( $1 == "1" )   ## Guardo la nota del 1º o 2º parcial segun corresponda
                            parcial1 = $4;
                        else
                            parcial2 = $4;
                    }
        END {
                if ( nombre != "" ) ## Solo imprimo los resultados si encontre al alumno
                {
                    ## Imprimo el nombre y DNI
                    print ( "Alumno: " nombre ", DNI: " dni );

                    ## Imprimo los titulos
                    print ( "Parcial\tNota");

                    ## Si rindio el primer parcial lo informo
                    if ( parcial1 != 0 )
                        print ( "      1\t" parcial1 );

                    ## Si rindio el segundo parcial lo informo
                    if ( parcial2 != 0 )
                        print ( "      2\t" parcial2 );

                    ## calculo el promedio y lo informo
                    prom = (parcial1 + parcial2) / 2;
                    printf( "Promedio: %1.1f - ", prom );

                    ## Informo el resultado de la materia
                    if ( parcial1 >= 7 && parcial2 >= 7 ) ## Si promociono los dos esta promocionado
                        print ( "Materia promocionada" );
                    else if ( parcial1 < 4 || parcial2 < 4 ) ## Si desaprobo alguno de los dos esta desaprobado
                        print ( "Materia desaprobada" );
                    else
                        print ( "Materia cursada" ); ## Si no se dieron ninguno de los casos anteriores, esta cursada
                }
                else ## Si no encontre al alumno aviso
                    print ( "El alumno con DNI " dni " no esta registrado" );
            }
    ' $ARCH
}

#######################
## PROCESO PRINCIPAL ##
#######################

## Si el primer parametro es "-h" muestro la ayuda y me voy
if [ "$1" = "-h" ]
then
	ayuda
	exit 0
fi

## Valido que haya al menos dos parametros
if [ $# -lt 2 ]
then
	echo "Error de parametros"
	ayuda
	exit 1
fi

## Valido que el primer parametro sea un archivo
if ! [ -f $1 ]
then
	echo "$1 no es un archivo"
	ayuda
	exit 2
fi

## Guardo el archivo en una variable
ARCH=$1

## Valido el segundo parametro y ejecuto lo que corresponda
case $2 in
	-p) 
		reporte_parcial
		;;
	-a)
		if [ -n "$3" ] ## si $3 no esta vacio es porque recibi un nombre como tercer parametro
		then
			reporte_x_alumno $3
		else
			reporte_alumnos
		fi
		;;
	*)
		echo "Error de parametros"
		ayuda
		exit 1
		;;
esac
	
## EOF ##