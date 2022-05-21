#!/bin/bash
# b) Crear 1 archivo de 100MB
# agregando piezas de tamaño random entre 1 y 10 MB 
# hasta completar el tamaño del archivo
ARCH="/media/usb/100mb.dat"
#ARCH="100mb.dat"
DISP="/media/usb"
echo "Inicio b): "$(date +%T:%N)" - "$(date +"%s");
sumatoria=0
x=0
while [ $x -le 100 ]
do
  RAN=$(( $RANDOM % 10 + 1 ))
  tmp=$(( $sumatoria + $RAN ))
  x=$tmp
  if [ $tmp -lt 100 ]
  then
    sumatoria=$tmp
    sudo dd if=/dev/zero bs=$(($RAN*1024*1024)) count=1 >> $ARCH
  fi
  #echo "es ahora $RAN"
  #echo "va sumando $sumatoria"
done

resta=$((100 - $sumatoria))
#echo $resta
echo $(($sumatoria + $resta))
sudo dd if=/dev/zero bs=$(($resta*1024*1024)) count=1 >> $ARCH
echo "Fin b): "$(date +%T:%N)" - "$(date +"%s");
echo "Se verifica el espacio -h"
df -h $DISP
echo "Se verifica el espacio -i"
df -i $DISP
#Fragmentacion de Disco
echo "Fragmentacion"
#sudo umount $DISP
#sudo fsck.reiserfs -fn /dev/sdb1
#sudo sudo fsck.reiserfs --check /dev/sdb1
#sudo fsck.msdos -v /dev/sdb1 #es FAT32
#sudo mount "/dev/sdb1" $DISP