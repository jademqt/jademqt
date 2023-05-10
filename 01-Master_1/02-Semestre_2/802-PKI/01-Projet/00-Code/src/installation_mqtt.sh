#! /bin/bash
cd /home/adm_jma/data

#on récupère les fichiers créés dans le dossier data
tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)

cd /home/adm_jma/src/debug

for (( i=0; i<$nbr_fichiers; i++ ))
do
	if [ ${tab_fichiers[$i]} = "Serveur.txt" ]
	then
        #Update avant les installations
        ./update_conteneurs.sh Serveur
        #Broker MQTT
        #Installation de python3 
        ./instal_paquet.sh Serveur python3
        #Installation de mosquitto
        ./instal_paquet.sh Serveur mosquitto
        ./instal_paquet.sh Serveur mosquitto-clients
        #on vérifie que mosquitto soit activé
        ./service_active.sh Serveur mosquitto
        #Installation de Paho
        ./instal_paquet.sh Serveur python3-pip
        echo " "
        echo "Installation de Paho, librairie Python, sur Serveur..."
        lxc-attach -n Serveur -- pip install paho-mqtt
        #configuration de mosquitto
        ./mosquitto_conf.sh Serveur
    else
        #Update avant les installations
        ./update_conteneurs.sh client_$(($i + 1))
        #Client MQTT
        #Installation de python3 
        ./instal_paquet.sh client_$(($i + 1)) python3
        #Installation de mosquitto
        ./instal_paquet.sh client_$(($i + 1)) mosquitto
        ./instal_paquet.sh client_$(($i + 1)) mosquitto-clients
        #on vérifie que mosquitto soit activé
        ./service_active.sh client_$(($i + 1)) mosquitto
        #Installation de Paho
        ./instal_paquet.sh client_$(($i + 1)) python3-pip
        echo " "
        echo "Installation de Paho, librairie Python, sur client_$(($i + 1))..."
        lxc-attach -n client_$(($i + 1)) -- pip install paho-mqtt
        #configuration de mosquitto
        ./mosquitto_conf.sh client_$(($i + 1))
	fi
done

echo " "
echo "########################"
echo "INSTALLATIONS TERMINEES"
echo "########################"
exit 0
