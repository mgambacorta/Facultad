#!/bin/bash
# e) Crear 16 archivos de un tamaño de 5 MB cada uno
DISP="/media/usb"
echo "Inicio e): "$(date +%T:%N)" - "$(date +"%s");
for i in `seq 1 16`;
do
	sudo dd if=/dev/zero of=/media/usb/"5mb-"$i"-e.dat" bs=1024 count=5120
done
echo "Fin e): "$(date +%T:%N)" - "$(date +"%s");
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