#! /bin/bash

cd /var/lib/lxc/

tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)


for (( i=0; i<$nbr_fichiers; i++ ))
do 
    rm -r /var/lib/lxc/${tab_fichiers[$i]}/rootfs/Fichiers/secure/
    for (( j=0; j<$nbr_fichiers; j++ ))
    do
        rm -r /var/lib/lxc/${tab_fichiers[$i]}/rootfs/Fichiers/msg/${tab_fichiers[$j]}/
        mkdir /var/lib/lxc/${tab_fichiers[$i]}/rootfs/Fichiers/msg/${tab_fichiers[$j]}
    done
    rm -r /var/lib/lxc/${tab_fichiers[$i]}/rootfs/Fichiers/msg/Serveur
    mkdir /var/lib/lxc/${tab_fichiers[$i]}/rootfs/Fichiers/msg/Serveur.
done



