#! /bin/bash


nom=$1

if [ $# -ne 1 ]
then
	echo Vous n avez pas donné le paramètre : nom du conteneur.
	exit 1
else
    cd /var/lib/lxc/$nom/rootfs/Fichiers/data
    tab_fichiers=($(ls))
    nbr_fichiers=$(ls | wc -l)

    if [ -d "/var/lib/lxc/$nom/rootfs/Fichiers/msg" ]
    then
        echo "Les dossiers msg existent déjà. "
    else

        mkdir /var/lib/lxc/$nom/rootfs/Fichiers/msg

        for (( i=0; i<$nbr_fichiers; i++ ))
        do          
            nom_fichier=$( echo ${tab_fichiers[i]} | cut -c 1-8)
            mkdir /var/lib/lxc/$nom/rootfs/Fichiers/msg/$nom_fichier
        done
        rmdir /var/lib/lxc/$nom/rootfs/Fichiers/msg/local.tx
    fi
fi
exit 0