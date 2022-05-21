#!/bin/bash
#1) Insertar el FILESYSTEM a la particion!
USB="/dev/sdb1";
echo "";
echo "##################";
echo "Se desmonta $USB";
echo "##################";
sudo umount $USB;
echo "";
echo "###Formatea EN FAT32 -I EN 4KB=4096 bytes##";
echo "";
echo "##Tiempo Inicial##: ";
ti=$(date +%T:%N)" - "$(date +"%s");
echo $ti;
sudo mkdosfs $USB -v -s 8 -F 32
echo "##Tiempo Final:##";
tf=$(date +%T:%N)" - "$(date +"%s");
echo $tf;
echo "##################";
echo "Se monta $USB";
echo "##################";
sudo mount $USB /media/usb;
#sudo mount -t ext3 /dev/sdb1 /media/usb
