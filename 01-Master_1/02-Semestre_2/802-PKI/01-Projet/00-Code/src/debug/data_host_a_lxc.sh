#! /bin/bash
cd /var/lib/lxc/

tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)
nom=$1

if [ $# -ne 1 ]
then
	echo Vous n avez pas donné le paramètre : nom du conteneur.
	exit 1
else
    #echo LE NOM EST $nom
    mkdir -p /var/lib/lxc/$nom/rootfs/Fichiers/data
    cd /var/lib/lxc/$nom/rootfs/Fichiers/data
    rm *.txt
    for (( i=0; i<$nbr_fichiers; i++ ))
    do  
        if [ ${tab_fichiers[i]} != $nom ]
        then
            cp /home/adm_jma/data/${tab_fichiers[i]}.txt /var/lib/lxc/$nom/rootfs/Fichiers/data/${tab_fichiers[i]}.txt
        else
            cp /home/adm_jma/data/${tab_fichiers[i]}.txt /var/lib/lxc/$nom/rootfs/Fichiers/data/local.txt
        fi
    done
        
    #echo "Les fichiers présents dans /data ont été copié sur $nom depuis l hote. "
fi
exit 0