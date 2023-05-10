#! /bin/bash
cd /var/lib/lxc/
tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)

#deux clients vont communiquer (choix au hasard). Le fichier message_en_cours.txt permet de savoir quel message est en cours (expéditeur destinataire contenu). 
cd /home/adm_jma/src/src_py
python3 choix_msg.py $nbr_fichiers

#on récupère les clients dans le fichier
readarray -t clients < /home/adm_jma/message_en_cours.txt

#on vérifie si les échanges symétriques sont déjà possibles (SYM_ACK.txt présent) :
if [ -e /var/lib/lxc/${clients[1]}/rootfs/Fichiers/secure/${clients[0]}/SYM_OK.txt ]; then
    echo "Echanges symétriques déjà possibles."
   
    cd /home/adm_jma/src/
    ./echange_sym.sh
else 
    echo "Mise en place des échanges symétriques nécessaire."
    
    #transfère du certificat signé de c1 à c2 dans le bon dossier (sur c2 stocké dans secure/c1)
    python3 ./start_echange.py ${clients[0]} ${clients[1]}

    #transfère de la signature de c1 sur c2
    lxc-attach -n ${clients[0]} -- python3 ./Fichiers/src/editeur.py ${clients[1]} ${clients[0]} b /Fichiers/secure/certificate.sig
    nom_msg_=$(ls /var/lib/lxc/${clients[1]}/rootfs/Fichiers/msg/${clients[0]}/)
    lxc-attach -n ${clients[1]} -- python3 ./Fichiers/src/format_binaire.py /Fichiers/msg/${clients[0]}/$nom_msg_ /Fichiers/secure/${clients[0]}/certificate.sig

    #on vérifie la validité du certificat de c1 sur c2
    lxc-attach -n ${clients[1]} -- python3 ./Fichiers/src/verifier_certif.py ${clients[0]} ${clients[1]}
    lxc-attach -n ${clients[1]} -- python3 ./Fichiers/src/extraire_pub.py ${clients[0]} 

    #génération d'un secret sur c2 
    lxc-attach -n ${clients[1]} -- python3 ./Fichiers/src/secret.py ${clients[0]}
    
    #Envoie de ce secret à c1. On remet dans le bon format le message, car message binaire, et on le place dans le bon dossier
    lxc-attach -n ${clients[0]} -- mkdir /Fichiers/secure/${clients[1]}/
    lxc-attach -n ${clients[1]} -- python3 ./Fichiers/src/editeur.py ${clients[0]} ${clients[1]} b /Fichiers/secure/${clients[0]}/encrypted.bin
    nom_msg=$(ls /var/lib/lxc/${clients[0]}/rootfs/Fichiers/msg/${clients[1]}/)
    lxc-attach -n ${clients[0]} -- python3 ./Fichiers/src/format_binaire.py /Fichiers/msg/${clients[1]}/$nom_msg /Fichiers/secure/${clients[1]}/encrypted.bin
    
   #c1 déchiffre ce secret
   lxc-attach -n ${clients[0]} -- python3 ./Fichiers/src/recup_secret.py ${clients[1]}
   
   #création d'un fichier indiquant que le secret est créé <=> possible de faire des échanges symétriques maintenant.
   #(normalement faire un echo SYM_OK > ../SYM_OK.txt mais en lxc avec les variables de bash compliqué donc ici en  brut..)
   lxc-attach -n ${clients[0]} -- touch  /Fichiers/secure/${clients[1]}/SYM_OK.txt
   echo "SYM_OK" >  /var/lib/lxc/${clients[0]}/rootfs/Fichiers/secure/${clients[1]}/SYM_OK.txt

   #enfin pour finir la mise en place des échanges on envoie un message pour prévenir que le secret est récupéré (ack)
   lxc-attach -n ${clients[0]} -- python3 ./Fichiers/src/editeur.py ${clients[1]} ${clients[0]} t /Fichiers/secure/${clients[1]}/SYM_OK.txt

   #c2 vérifie que c'est un message d'aquittement :
   msg=$(ls /var/lib/lxc/${clients[1]}/rootfs/Fichiers/msg/${clients[0]}/)
   lxc-attach -n ${clients[1]} -- python3 ./Fichiers/src/ack.py /Fichiers/msg/${clients[0]}/$msg ${clients[0]}
   lxc-attach -n ${clients[1]} -- rm /Fichiers/msg/${clients[0]}/$msg

   #on vérifie que SYM_ACK.txt est maintenant là :
   if [ -e /var/lib/lxc/${clients[1]}/rootfs/Fichiers/secure/${clients[0]}/SYM_OK.txt ]; then
       echo "Le secret a été partagé correctement. Début des échanges. "
       
       cd /home/adm_jma/src/
       ./echange_sym.sh
   else
       echo "PB REVOIR start_echange.sh"
   fi
fi


