<#
    .SYNOPSIS 
      Muestra en formato de tabla la cantidad de veces que se repite el nombre de un archivo en un determinado directorio ingresado como parámetro.
    .EXAMPLE
     .\TP1_P3.ps1 .\prueba
    .NOTES
         Nombre:  .\TP1_P3.ps1
         Autores: DNI: 30577162 Alvarez, Patricio
                  DNI: 29279015 Gambacorta, Mariano
                  DNI: 30466883 Quintana, Carlos Carlos
                  DNI: 40459956 Salas, Miguel Angel
                  DNI: 31554105 Silva, Hugo Ricardo
        Entrega: 1º reentrega
        Fecha:   06/05/2015
  #>
param([Parameter(Mandatory=$True)][string]$directorio)
echo $directorio
$tabla=@{}
Get-ChildItem $directorio -Recurse -Attributes Archive | Sort-Object -Property Name | ForEach-Object {$tabla[$_.Name]=$tabla[$_.Name]+1}
echo $tabla | Format-Table Name, Value