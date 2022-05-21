#!/bin/bash
DISP="/media/usb"
echo "Inicio a): "$(date +%T:%N)" - " $(date +"%s")
# a) Crear 512 archivos de 1 MB cada uno
for i in `seq 1 512`;
do
  sudo dd if=/dev/zero of=$DISP/1mb-$i.dat bs=1024 count=1024    
done
echo "Fin a): "$(date +%T:%N)" - " $(date +"%s")
echo "Se verifica el espacio -h"
df -h $DISP
echo "Se verifica el espacio -i"
df -i $DISP
#Fragmentacion de Disco
echo "Fragmentacion"		
#sudo umount "/dev/sdb1"
#sudo fsck.msdos -fn /dev/sdb1
#sudo fsck.msdos -v /dev/sdb1 #es FAT32
#sudo mount "/dev/sdb1" $DISP