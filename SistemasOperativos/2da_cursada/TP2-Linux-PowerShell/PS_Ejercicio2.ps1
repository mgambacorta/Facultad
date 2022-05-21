<#
.SYNOPSIS
    Script que muestra los procesos que están corriendo en el sistema.
.DESCRIPTION
    Muestra todos los procesos que están corriendo en el sistema, el % de uso de CPU y uso de memoria de cada uno.
	En caso de recibir por parametro un PID se muestran unicamente los datos del proceso (si el ID no existe no muestra nada).
.PARAMETER 1
    PID del proceso a mostrar.
.EXAMPLE
    > .\PS_Ejercicio2.ps1 
	
	Name     IdProcess  PercentProcessorTime  PageFileBytes
    ----     ---------  --------------------  -------------
    Idle             0                   100              0
    System           4                     0         233472
    smss           364                     0         589824
    csrss          516                     0        2547712
    wininit        596                     0        1736704
    csrss#1        620                     0        3284992
	...            ...                   ...            ...
	...            ...                   ...            ...
	...            ...                   ...            ...
	_Total           0                   100     2743222272
	
.EXAMPLE
	> .\PS_Ejercicio2.ps1  516
	
	Name     IdProcess  PercentProcessorTime  PageFileBytes
    ----     ---------  --------------------  -------------
    csrss          516                     0        2547712
	
.NOTES
    Author: Alvarez, Patricio
	        Gambacorta, Mariano
			Quintana, Carlos
			Salas, Miguel Angel
	        Silva, Hugo
    Date:   11/05/2015
#>

param([Parameter(Mandatory=$False)] [Int] $proceso)

if ( $proceso -eq "" )
{
	Get-WmiObject "Win32_PerfFormattedData_PerfProc_Process" | Format-Table Name, IdProcess, PercentProcessorTime, PageFileBytes
}
else
{
	Get-WmiObject "Win32_PerfFormattedData_PerfProc_Process" -Filter "IdProcess = $proceso" | Format-Table Name, IdProcess, PercentProcessorTime, PageFileBytes
}
