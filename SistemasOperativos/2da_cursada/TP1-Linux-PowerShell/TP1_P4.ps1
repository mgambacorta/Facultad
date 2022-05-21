<#
.SYNOPSIS
    El script realiza la suma, resta, multiplicación y el promedio de dos números enteros guardando en el archivo de salida cada operación realizada.
.DESCRIPTION
    Script que recibe por parámetro un número entero a, un número entero b y el path del archivo de salida en el que se guardarán los resultados.
.PARAMETER a
    Primer número de las expresiones.
.PARAMETER b
    Segundo número de las expresiones.
.PARAMETER Path
    Archivo donde se guarsa las expresiones y sus resultados.
.EXAMPLE
    > .\script.ps1 2 100 salida
    > Get-Content salida
    2 + 100 = 102
    2 - 100 = -98
    2 * 100 = 200
    (2+100)/2 = 51
.NOTES
    Author: Miguel Angel Salas
    Date:   20 de Abril de 2015
#>

param([Parameter(Mandatory=$True,
       ValueFromPipeline=$True,
       ValueFromPipelineByPropertyName=$True,
       HelpMessage='Ingese el primer número de las expresiones')]
      [Int] $a,
      [Parameter(Mandatory=$True,
       ValueFromPipeline=$True,
       ValueFromPipelineByPropertyName=$True,
       HelpMessage='Ingese el segundo número de las expresiones')]
      [Int] $b,
      [Parameter(Mandatory=$True,
       ValueFromPipeline=$True,
       ValueFromPipelineByPropertyName=$True,
       HelpMessage='Ingrese donde se guarsa las expresiones y sus resultados')]
      [String] $Path) <# [ValidateScript({-NOT (Test-Path $_ -PathType 'Leaf')})]  #>


Write-Debug "Inicia el script"

if (Test-Path "$Path" -PathType 'Leaf')
{
    Throw "El archivo ya existe: $($Path)"
}

Write-Debug "Parametros validados"

"$a + $b = " + ($a + $b) | Out-File -FilePath $Path -Append

"$a - $b = " + ($a - $b) | Out-File -FilePath $Path -Append

"$a * $b = " + ($a * $b) | Out-File -FilePath $Path -Append

"($a+$b)/2 = " + ($a + $b)/2 | Out-File -FilePath $Path -Append