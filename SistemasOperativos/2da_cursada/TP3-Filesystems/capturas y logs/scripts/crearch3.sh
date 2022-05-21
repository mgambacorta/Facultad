#!/bin/bash
# c) Crear 1 archivo secuencial de 1024 MB
DISP="/media/usb"
echo "Inicio c): "$(date +%T:%N)" - "$(date +"%s");
sudo dd if=/dev/zero of=/media/usb/1024mb.dat bs=1024 count=1048576
echo "Fin c): "$(date +%T:%N)" - "$(date +"%s");
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