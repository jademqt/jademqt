#! /bin/bash
cd /var/lib/lxc/

tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)
nom=$1
fichier=$2

if [ $# -ne 2 ]
then
	echo Vous n avez pas donné les paramètres : nom du conteneur ou nom du fichier à copier.
	exit 1
else
    if [ -d "/var/lib/lxc/$nom/rootfs/Fichiers/src" ]
    then
        cp /home/adm_jma/src/src_py/$fichier /var/lib/lxc/$nom/rootfs/Fichiers/src/$fichier
    else
        mkdir -p /var/lib/lxc/$nom/rootfs/Fichiers/src
        cp /home/adm_jma/src/src_py/$fichier /var/lib/lxc/$nom/rootfs/Fichiers/src/$fichier
    fi
fi
exit 0
