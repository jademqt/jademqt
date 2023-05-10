#! /bin/bash
if [ $# -ne 2 ]
then
	echo Vous n avez pas donné les paramètres : nom de la machine puis nom paquet.
	exit 1
else
	nom=$1
	paquet=$2
	#On vérifie si le paquet est déjà installé
	if [ $paquet = "python3-pip" ]
	then
		if [ $(lxc-attach -n $nom -- pip3 --version | grep -c "22.0.2") -ne 0 ]
		then
			echo Le paquet $paquet est déjà installé sur $nom.
			echo " "
		else
			echo Installation du paquet $paquet sur $nom...
			echo "..."
			echo "..."
			#sinon on l'installale
			lxc-attach -n $nom -- apt install $paquet -y
			echo " "
			echo Installation du paquet $paquet sur $nom terminée.
			echo " "
		fi
	else
		if [ $(dpkg-query -W $paquet| grep -c $paquet) -ne 0 ]
		then
			echo Le paquet $paquet est déjà installé sur $nom.
			echo " "
		else
			echo Installation du paquet $paquet sur $nom...
			echo "..."
			echo "..."
			#sinon on l'installale
			lxc-attach -n $nom -- apt install $paquet -y
			echo " "
			echo Installation du paquet $paquet sur $nom terminée.
			echo " "
		fi
	fi
fi
exit 0