#! /bin/bash
cd /home/adm_jma

nbr_clients=$1

#Verification de l'existance du parametre, sinon 3 clients par défaut
if [ "$#" != 1 ]
then
	echo Vous n avez pas rentre le nbr de clients en paramètre.
	echo Création de 3 clients.
	nbr_clients=$((3))
else
	if [ $nbr_clients -lt 2 ]||[ $nbr_clients -gt 8 ]
	then
		echo "Le nombre de clients doit être compris entre 2 et 8."
		exit 3
	fi
fi

#creation des conteneurs
echo "    "
echo "    "
echo " Attention le procédé peut prendre du temps ..."
echo "    "
echo "    "

for (( i=0; i<=$nbr_clients; i++ ))
do
	if [ $i -eq 0 ]
	then
		nom=Serveur
	else
		nom=client_$i
	fi

	echo " "
	echo "Creation du conteneur " $nom " ..."


	lxc-create -n $nom -t ubuntu

	#copie du fichier de configuration
	cp /var/lib/lxc/$nom/config /var/lib/lxc/$nom/config.copy
	rm /var/lib/lxc/$nom/config 

	
	echo "#configuration du reseau" > /var/lib/lxc/$nom/config
	echo "# Common configuration" >> /var/lib/lxc/$nom/config
	echo "lxc.include = /usr/share/lxc/config/ubuntu.common.conf" >> /var/lib/lxc/$nom/config
	echo " " >> /var/lib/lxc/$nom/config
	echo "# Container specific configuration" >> /var/lib/lxc/$nom/config
	echo "lxc.rootfs.path = dir:/var/lib/lxc/$nom/rootfs" >> /var/lib/lxc/$nom/config
	echo "lxc.uts.name = $nom" >> /var/lib/lxc/$nom/config
	echo "lxc.arch = amd64" >> /var/lib/lxc/$nom/config
	echo " " >> /var/lib/lxc/$nom/config
	echo "# Network configuration" >> /var/lib/lxc/$nom/config
	echo "lxc.net.0.type = veth" >> /var/lib/lxc/$nom/config
	echo "lxc.net.0.link = br0" >> /var/lib/lxc/$nom/config
	echo "lxc.net.0.flags = up" >> /var/lib/lxc/$nom/config
	echo "lxc.net.0.hwaddr = 00:16:3e:c6:13:0"$(($i + 1)) >> /var/lib/lxc/$nom/config
	service lxc stop
	service lxc start

	echo " "
	echo " "
	echo "Démarrage du conteneur " $nom " ..."

	lxc-start -n $nom

	VM_run=$(lxc-ls -f | grep $nom | grep -c "RUNNING")

	if [ $VM_run -eq 0 ]
	then
		echo "Probleme de creation de la VM " $nom
		exit 1
	fi

	echo "Le conteneur " $nom " a démarré correctement ..."
done

exit 0
