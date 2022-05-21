#!/bin/bash
#1) Insertar el FILESYSTEM a la particion!
USB="/dev/sdb1";
MOUNT="/media/usb";
echo "";
echo "##################";
echo "Se desmonta";
echo "##################";
sudo umount $MOUNT;
echo "";
echo "###Formatea EN EXT3 EN 32KB=32768 bytes##";
echo "";
echo "##Tiempo Inicial##: ";
ti=$(date +%T:%N)" - "$(date +"%s");
echo $ti;
sudo mkfs.ext3 -F $USB -b 32768
echo "##Tiempo Final:##";
tf=$(date +%T:%N)" - "$(date +"%s");
echo $tf;
echo "##################";
echo "Se monta";
echo "##################";
sudo mount -t ext3 $USB $MOUNT;
#sudo mount -t ext3 /dev/sdb1 /media/usb
