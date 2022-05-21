<#
    .SYNOPSIS 
    Recibe dos archivos uno de origen y otro de destino. Comprime los archivos que se encuentran en el origen
    y lo envía al destino en el formato YYYYMMDD.zip. Si hay más de tres archivos borra el más viejo.
    .EXAMPLE
     .\TP2EJ1.ps1 E:\sisop\TP2\origen\ E:\sisop\TP2\destino\
    .NOTES
         Nombre:  .\TP1_P3.ps1
         Autores: DNI: 30577162 Alvarez, Patricio
                  DNI: 29279015 Gambacorta, Mariano
                  DNI: 30466883 Quintana, Carlos Carlos
                  DNI: 40459956 Salas, Miguel Angel
                  DNI: 31554105 Silva, Hugo Ricardo
        Entrega: 1º entrega
        Fecha:   11/05/2015
  #>
param(
    [Parameter(Mandatory=$True,HelpMessage='Directorio origen')][string]$dirOrigen, 
    [Parameter(Mandatory=$True,HelpMessage='Directorio destino')][string]$dirDestino)
$cantArchivos=3
if(!$dirOrigen.EndsWith("\")){
    $dirOrigen=$dirOrigen+"\" 
}
if(!$dirDestino.EndsWith("\")){
    $dirDestino=$dirDestino+"\"
}
$fullName = $dirDestino+(get-date -UFormat %Y%m%d)+".zip"
If (Test-Path $fullName){
    Remove-Item $fullName
}
$tempFile = [System.IO.Path]::GetTempFileName()
Remove-Item -Force $tempFile
try{
    Add-Type -AssemblyName "System.IO.Compression.Filesystem"
    [System.IO.Compression.ZipFile]::CreateFromDirectory($dirOrigen,$tempFile);

    Move-Item $tempFile $fullName

    $cant = 0;
    Get-ChildItem $dirDestino | ForEach-Object {if($_.FullName.Contains(".zip")){$cant=$cant+1}}
    if($cant -gt $cantArchivos){
        echo "Se encontraron más de tres archivos..."
        $items = Get-ChildItem $dirDestino
        $aBorrar
        $timeAux = Get-Date
        foreach($item in $items){
            if($item.CreationTime -le $timeAux){
                $aBorrar = $item
                $timeAux = $item.CreationTime
            }
        }
        Remove-Item -Force $aBorrar.FullName
        echo "Se borro el archivo: $aBorrar..."
    }
}catch{
    $errorMessage =  $_.Exception.Message
    echo $errorMessage
}