#! /bin/bash

#les clients doivent écouter s'ils recoivent des messages
cd /var/lib/lxc/

tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)

cd /home/adm_jma/src/src_py/
for (( i=0; i<$nbr_fichiers; i++ ))
do
    lxc-attach -n ${tab_fichiers[i]} -- python3 ./Fichiers/src/abonne.py ${tab_fichiers[i]} &
    echo ${tab_fichiers[i]} s est abonné pour recevoir ses messages.
done

exit 0