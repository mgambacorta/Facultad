#get-help custom
Function Get-seleccion{
	<#

	.SYNOPSIS
		Copia los archivos de un origen hacia un destino mediante parametros indicados por el usuario. 
	.DESCRIPTION
		El objetivo es copiar los 5 archivos mas nuevos en otro directorio pasados ambos por parametros
	
	.EXAMPLE
		Get-seleccion -directorioA c:\Users -directorioB c:\Users\Folder 
	
	.EXAMPLE
		Get-seleccion -directorioA c:\Users -directorioB .
	.NOTES
		Author: Hugo Silva
		Date:   Sabado 19/04/2015
	#>
}
#Copiar archivos
Function f-seleccion{
	[CmdletBinding()]
	Param
          (
            [String[]]
            $directorioA,
            [String[]]
            $directorioB
          ) 
	BEGIN{

		write-host '########################'
		write-host '######## INICIO ########'
		write-host ''
		write-host "Archivo copiado: "
	}
	PROCESS{
		
		$arch_origen=get-childitem -Path $directorioA -recurse | Where {$_.PsIsContainer -eq $false} |foreach-object{$_.FullName;} | sort-object ; #$_.LastWriteTime.ToString("yyyyMMdd HH:mm:ss")+" "+ 		
		$cantidad=$arch_origen.count;
		
		if(!$cantidad){
			echo "El directorio Origen no contiene archivos para copiar";
		}else{
			
			if($cantidad -gt 5){
				$cantidad=5;
			}
			
			for($i=0;$i -lt $cantidad;$i++){
				$origen=$arch_origen[$i];
				$destino=$directorioB[0];
				echo $origen;
				cpi $origen $destino;
			}
			
		}
			
	}
	END{
		write-host ''
		write-host '######## END ########'
		write-host '#####################'
	}
}
#Verificar la cantidad de parametros, si no es 2 no continuamos y arrojamos mje de Get-Help
if($args.count -ne 2){
	echo "Para obtener Ayuda ejecute: Get-Help Get-seleccion";
}else{

	$directorioA_=Test-Path $args[0] -pathType container
	$directorioB_=Test-Path $args[1] -pathType container
	
	if( !$directorioA_ -or !$directorioB_ ){
		echo "Verifique que los directorios existan";
	}else{
		f-seleccion  $args[0] $args[1];
	}
}
#ejecutar:  C\Users\Hugo\Desktop .. nombre.ps1 para generar el get-help
#ejecutar:  get-help Get-seleccion -examples