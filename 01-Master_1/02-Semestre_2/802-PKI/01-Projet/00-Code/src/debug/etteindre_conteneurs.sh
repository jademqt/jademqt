#! /bin/bash
cd /var/lib/lxc

tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)

for (( i=0; i<$nbr_fichiers; i++ ))
do
	if [ ${tab_fichiers[$i]} = "Serveur" ]
	then
        if [ $(lxc-ls -f | grep Serveur | awk '{ print $2}') = "RUNNING" ]
        then 
            lxc-stop -n Serveur
            echo Le conteneur Serveur a été etteint. 
        fi
    else
        if [[ $(lxc-ls -f | grep client_$(($i + 1)) | awk '{ print $2}') = "RUNNING" ]]
        then 
            lxc-stop -n client_$(($i + 1))
            echo Le conteneur client_$(($i + 1)) a été etteint.
        fi
	fi
done
exit 0