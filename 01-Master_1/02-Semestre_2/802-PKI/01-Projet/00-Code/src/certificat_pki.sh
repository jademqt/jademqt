#! /bin/bash
cd /home/adm_jma/src/debug

#Update avant l installation
./update_conteneurs.sh Serveur

#Installation de cryptography sur le Serveur 
lxc-attach -n Serveur -- pip install cryptography

#création du dossier secure sur le serveur ainsi que du sous dossier Serveur
mkdir /var/lib/lxc/Serveur/rootfs/Fichiers/secure 
mkdir /var/lib/lxc/Serveur/rootfs/Fichiers/secure/Serveur 

#génération du certificat
lxc-attach -n Serveur -- python3 ./Fichiers/src/certif_auto_signe.py

nbr_clients=$(ls /var/lib/lxc/Serveur/rootfs/Fichiers/data | wc -l)
for (( i=1; i<$nbr_clients; i++ ))
do
    nom=client_$i

    #Installation de cryptography sur les clients
    #lxc-attach -n client_$i -- pip install cryptography

    #création du dossier secure sur les clients qui regroupera les certificats etc.
    mkdir /var/lib/lxc/client_$i/rootfs/Fichiers/secure
    mkdir /var/lib/lxc/client_$i/rootfs/Fichiers/secure/Serveur

    #copie du certificat et de la clef publique de la pki sur les clients
    cp /var/lib/lxc/Serveur/rootfs/Fichiers/secure/Serveur/certificate.pem /var/lib/lxc/client_$i/rootfs/Fichiers/secure/Serveur/certificate_pki.pem 
    cp /var/lib/lxc/Serveur/rootfs/Fichiers/secure/Serveur/public_key.pem /var/lib/lxc/client_$i/rootfs/Fichiers/secure/Serveur/public_key_pki.pem


    #génération des clefs et du CSR sur les clients
    lxc-attach -n client_$i -- python3 Fichiers/src/generer_pub_priv.py

    #envoie du CSR au serveur 
    #normalement les clients et le serveur sont déjà en écoute sur MQTT
    lxc-attach -n client_$i -- python3 Fichiers/src/editeur.py Serveur client_$i t /Fichiers/secure/csr.pem
    
    #le serveur  construit un dossier par client
    lxc-attach -n Serveur -- mkdir /Fichiers/secure/client_$i

    #le serveur traite les certificats 
    nom_msg=$(ls /var/lib/lxc/Serveur/rootfs/Fichiers/msg/client_$i/)
    lxc-attach -n Serveur -- python3 ./Fichiers/src/sign_CSR.py /Fichiers/msg/client_$i/$nom_msg client_$i
    lxc-attach -n Serveur -- rm /Fichiers/msg/client_$i/$nom_msg

    
    #Le serveur renvoie le certificat et la signature en lisible à chaque client
    dir="/var/lib/lxc/Serveur/rootfs/Fichiers/secure/client_$i"
    noms_msg_serveur=()
    for file in "$dir"/*; do
        if [[ -f "$file" ]]; then
            filename=$(basename "$file")
            noms_msg_serveur+=("$filename")
        fi
    done
    
    #on traite l'envoie de {noms_msg_serveur[1]} qui est le CSR signé
    lxc-attach -n Serveur -- python3 Fichiers/src/editeur.py client_$i Serveur. t /Fichiers/secure/client_$i/${noms_msg_serveur[1]}
    #les client stockent le certificat signé dans leur dossier "secure"
    nom_csr=$(ls /var/lib/lxc/client_$i/rootfs/Fichiers/msg/Serveur./)
    if [ -z "$nom_csr" ]
    then
        echo PB
        exit 1
    else
        mv /var/lib/lxc/client_$i/rootfs/Fichiers/msg/Serveur./$nom_csr /var/lib/lxc/client_$i/rootfs/Fichiers/secure/signed_csr.pem
    fi

    #on traite l'envoie de {noms_msg_serveur[0]} qui est la signature
    
    #cp /var/lib/lxc/Serveur/rootfs/Fichiers/secure/client_$i/${noms_msg_serveur[0]} /var/lib/lxc/client_$i/rootfs/Fichiers/secure/${noms_msg_serveur[0]}
    lxc-attach -n Serveur -- python3 ./Fichiers/src/editeur.py client_$i Serveur. b /Fichiers/secure/client_$i/${noms_msg_serveur[0]}
    nom_msg=$(ls /var/lib/lxc/client_$i/rootfs/Fichiers/msg/Serveur.)
    #echo DEBUG nom_msg :: $nom_msg
    lxc-attach -n client_$i -- python3 ./Fichiers/src/format_binaire.py /Fichiers/msg/Serveur./$nom_msg /Fichiers/secure/${noms_msg_serveur[0]}

    echo""
    echo""
    
done
exit 0
