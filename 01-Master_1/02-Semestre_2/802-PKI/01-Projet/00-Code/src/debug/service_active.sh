#! /bin/bash
cd /var/lib/lxc/

tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)
nom=$1
paquet=$2

if [ $# -ne 2 ]
then
	echo Vous n avez pas donné les paramètres : nom du conteneur ou nom du paquet à activer.
	exit 1
else
    
    #On vérifie que Mosquitto soit activé
    if [ $(lxc-attach -n $nom -- systemctl status $paquet | grep -c $paquet ) -eq 0 ]
    then 
        echo " "
        echo Le paquet $paquet n est pas activé sur $nom. Activation ...
        echo " "
        lxc-attach -n $nom -- systemctl start $paquet
        if [ $(lxc-attach -n $nom -- systemctl status $paquet | grep -c $paquet ) -eq 0 ]
        then
            echo "Echec de l'activation de Mosquitto sur Serveur."
            exit 2
        fi  
    fi
    echo " "
    echo Le paquet $paquet est bien activé sur $nom.                

fi
exit 0