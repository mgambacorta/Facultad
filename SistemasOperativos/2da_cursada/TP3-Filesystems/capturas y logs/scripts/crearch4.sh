#!/bin/bash
# d) Crear 16 archivos de un tamaño de 5 MB cada uno, 
# pero se deben crear en piezas de a 512KB, 
# agregando piezas a cada archivo hasta 
# llegar al tamaño total.
DISP="/media/usb"
echo "Inicio d): "$(date +%T:%N)" - "$(date +"%s");
for i in `seq 1 16`;
do
sumatoria=0
PIEZA=512*1024
ARCH="5mb_"$i.dat
while [ $sumatoria -lt 5242880 ]
do  
  sudo dd if=/dev/zero bs=$(($PIEZA)) count=1 >> /media/usb/$ARCH
  sumatoria=$(( $sumatoria + $PIEZA ))
  #echo $sumatoria
done
done
echo "Fin d): "$(date +%T:%N)" - "$(date +"%s");
echo "Se verifica el espacio -h"
df -h $DISP
echo "Se verifica el espacio -i"
df -i $DISP
#Fragmentacion de Disco
echo "Fragmentacion"
#sudo umount $DISP
#sudo fsck.msdos -v /dev/sdb1 #es FAT32
#sudo sudo fsck.reiserfs --check /dev/sdb1
#sudo fsck.reiserfs -fn /dev/sdb1
#sudo mount "/dev/sdb1" $DISP