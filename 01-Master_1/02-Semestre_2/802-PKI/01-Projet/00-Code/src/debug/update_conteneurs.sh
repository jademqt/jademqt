#! /bin/bash
if [ "$#" != 1 ]
then
	echo Vous n avez pas donné les paramètres : nom de la machine.
	exit 1
else
	nom=$1
    echo Mise a jour de $nom...
    echo "..."
    echo "..."
	
	lxc-attach -n $nom -- apt update -y
	
    echo Mise a jour de $nom terminées.
    echo " "
fi
exit 0
