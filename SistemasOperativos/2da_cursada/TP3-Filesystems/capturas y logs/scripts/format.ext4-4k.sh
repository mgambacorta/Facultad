#!/bin/bash
#1) Insertar el FILESYSTEM a la particion!
USB="/dev/sdb1";
echo "";
echo "##################";
echo "Se desmonta /dev/sdb1";
echo "##################";
sudo umount $USB;
echo "";
echo "###Formatea EN EXT4 EN 4KB=4096 bytes##";
echo "";
echo "##Tiempo Inicial##: ";
ti=$(date +%T:%N)" - "$(date +"%s");
echo $ti;
sudo mkfs.ext4 -F $USB -b 4096
echo "##Tiempo Final:##";
tf=$(date +%T:%N)" - "$(date +"%s");
echo $tf;
echo "##################";
echo "Se monta /dev/usb1";
echo "##################";
sudo mount -t ext4 $USB /media/usb;
#sudo mount -t ext3 /dev/sdb1 /media/usb
