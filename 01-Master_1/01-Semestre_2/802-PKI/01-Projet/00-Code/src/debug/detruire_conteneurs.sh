#! /bin/bash
cd /home/adm_jma/src/debug
./etteindre_conteneurs.sh

cd /var/lib/lxc

tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)

for (( i=0; i<$nbr_fichiers; i++ ))
do
	if [ ${tab_fichiers[$i]} = "Serveur" ]
    then
        lxc-destroy -n Serveur
        echo Le conteneur Serveur a été détruit.
    else
        lxc-destroy -n client_$(($i + 1))
        echo Le conteneur client_$(($i + 1)) a été détruit.
	fi
done
exit 0
