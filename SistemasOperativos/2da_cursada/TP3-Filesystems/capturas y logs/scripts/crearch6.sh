#!/bin/bash
# f) Agregar 100 MB secuenciales al archivo del punto b)i
DISP="/media/usb"
echo "Inicio del script y de f): "$(date +%T:%N)" - "$(date +"%s");
sudo dd if=/dev/zero bs=1024 count=102400 >> /media/usb/100mb.dat
echo "Fin del script y de f): "$(date +%T:%N)" - "$(date +"%s");
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