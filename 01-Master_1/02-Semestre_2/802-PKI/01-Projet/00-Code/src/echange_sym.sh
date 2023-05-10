#! /bin/bash

#on récupère les clients dans le fichier
readarray -t clients < /home/adm_jma/message_en_cours.txt

#on utilise le secret pour chiffrer le message que c1 veut envoyer à c2
lxc-attach -n ${clients[0]} -- python3 ./Fichiers/src/encrypte_msg.py "${clients[2]}" /Fichiers/secure/${clients[1]}/secret.pem

#c1 écrit son envoi dans son dossier historique.txt dans /Fichiers
lxc-attach -n ${clients[0]} -- python3 ./Fichiers/src/ajout_historique.py "${clients[0]}" "${clients[1]}" "${clients[2]}" "${clients[0]}"

#on envoie le message a_envoyer_secu.txt (message encrypté avec le secret) à c2 
lxc-attach -n ${clients[0]} -- python3 ./Fichiers/src/editeur.py ${clients[1]} ${clients[0]} b /Fichiers/a_envoyer_secu.txt
_nom_msg=$(ls /var/lib/lxc/${clients[1]}/rootfs/Fichiers/msg/${clients[0]}/)
lxc-attach -n ${clients[1]} -- python3 ./Fichiers/src/format_binaire.py /Fichiers/msg/${clients[0]}/$_nom_msg /Fichiers/msg/${clients[0]}/msg_x.txt


#on décrypte le message
lxc-attach -n ${clients[1]} -- python3 ./Fichiers/src/decrypte_msg.py /Fichiers/msg/${clients[0]}/msg_x.txt /Fichiers/secure/${clients[0]}/secret.pem
lxc-attach -n ${clients[1]} -- rm /Fichiers/msg/${clients[0]}/msg_x.txt

#c2 écrit la recption de ce message dans son dossier historique.txt dans /Fichiers
lxc-attach -n ${clients[1]} -- python3 ./Fichiers/src/ajout_historique.py "${clients[0]}" "${clients[1]}" "${clients[2]}" "${clients[1]}"