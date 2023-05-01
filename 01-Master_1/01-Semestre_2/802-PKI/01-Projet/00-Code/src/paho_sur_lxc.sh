#! /bin/bash

cd /var/lib/lxc/

tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)

cd /home/adm_jma/src/debug/

for (( i=0; i<$nbr_fichiers; i++ ))
do    
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} abonne.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} editeur.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} verifier_certif.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} extraire_pub.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} secret.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} recup_secret.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} generer_pub_priv.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} ack.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} encrypte_msg.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} decrypte_msg.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} ajout_historique.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} format_binaire.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} certif_auto_signe.py
    ./fichier_host_a_lxc.sh ${tab_fichiers[$i]} sign_CSR.py
    ./data_host_a_lxc.sh ${tab_fichiers[$i]}
    ./crea_dossier_msg.sh ${tab_fichiers[$i]}

done

./fichier_host_a_lxc.sh Serveur abonne_serveur.py
rm /var/lib/lxc/Serveur/rootfs/Fichiers/src/abonne.py
mv /var/lib/lxc/Serveur/rootfs/Fichiers/src/abonne_serveur.py /var/lib/lxc/Serveur/rootfs/Fichiers/src/abonne.py

./fichier_host_a_lxc.sh Serveur editeur_serveur.py
rm /var/lib/lxc/Serveur/rootfs/Fichiers/src/editeur.py
mv /var/lib/lxc/Serveur/rootfs/Fichiers/src/editeur_serveur.py /var/lib/lxc/Serveur/rootfs/Fichiers/src/editeur.py

exit 0