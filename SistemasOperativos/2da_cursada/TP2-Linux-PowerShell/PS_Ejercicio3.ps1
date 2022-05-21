<#
.SYNOPSIS
    Muestra los datos de IP, MACAddress y Subred de una maquina dentro de la red.
.DESCRIPTION
    Dado un hostname de la misma red pasado por parametro, muestra la información de red de ese hostname: IP, 
	subred a la que pertenece y dirección MAC.
.PARAMETER 1
    Hostname.
.EXAMPLE
    > .\PS_Ejercicio3.ps1 localhost
	
	description                                 ipaddress                                  macaddress         DefaultIPGateway
    -----------                                 ---------                                  ----------         ----------------
    Intel(R) 82579V Gigabit Network Connection  {10.244.49.41, fe80::43b:5a0b:f1c9:d999}   00:90:F5:D9:2A:DB  {10.244.48.1}
    VirtualBox Host-Only Ethernet Adapter       {192.168.56.1, fe80::704d:32c9:8cc0:4ccb}  08:00:27:00:98:79                                              
	
.NOTES
    Author: Alvarez, Patricio
	        Gambacorta, Mariano
			Quintana, Carlos
			Salas, Miguel Angel
	        Silva, Hugo
    Date:   11/05/2015
#>

param([Parameter(Mandatory=$True, HelpMessage='Ingese un HostName dentro de su red: ')] [String] $nombre)

## Para usar credenciales necesito capturar errores
## Limpio la variable de errores
$Error.Clear()
## Establezco que los errores interrumpan, asi puedo capturarlos
$ErrorActionPreference = "Stop"

Try
{
	Get-WmiObject "win32_networkadapterconfiguration" -ComputerName $nombre | Where{$_.IPAddress -ne $null} | Format-Table DNSHostName, ipaddress, macaddress, IPSubNet
}
Catch
{
	## Si no me pude conectar con Get-WmiObject obtengo al menos la ip
	$ping = New-Object System.Net.NetworkInformation.Ping

	$obj =New-Object PSObject
	
	$obj | Add-Member DNSHostName($nombre)
	$obj | Add-Member ipaddress($($ping.Send($nombre).Address).IPAddressToString)
	## Estos datos no los puedo conseguir, pero los agrego para mantener el formato
	$obj | Add-Member macaddress("")
	$obj | Add-Member IPSubNet("")
	
	$obj | Format-Table
}