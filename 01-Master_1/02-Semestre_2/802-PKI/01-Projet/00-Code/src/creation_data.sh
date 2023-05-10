#! /bin/bash
cd /home/adm_jma/src/debug
./allumer_conteneurs.sh

cd /var/lib/lxc/

tab_fichiers=($(ls))
nbr_fichiers=$(ls | wc -l)
#ip=""
for (( i=0; i<$nbr_fichiers; i++ ))
do
    ip=$(lxc-ls -f | grep ${tab_fichiers[$i]} | awk '{print $5}')
    echo ip :  $ip
    if [ $ip = '-' ]
    then
        echo "Probleme dans l attribution de l IP de " $nom
        exit 1
    fi
    echo $ip > /home/adm_jma/data/${tab_fichiers[$i]}.txt
done

exit 0
