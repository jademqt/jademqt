#! /bin/bash
nom=$1

if [ $# -ne 1 ]
then
	echo Vous n avez pas donné le paramètre : nom du conteneur.
	exit 1
else
    cp /var/lib/lxc/$nom/rootfs/etc/mosquitto/mosquitto.conf /var/lib/lxc/$nom/rootfs/etc/mosquitto/mosquitto.conf.copy
    rm /var/lib/lxc/$nom/rootfs/etc/mosquitto/mosquitto.conf

    echo "pid_file /run/mosquitto/mosquitto.pid" > /var/lib/lxc/$nom/rootfs/etc/mosquitto/mosquitto.conf
    echo "persistence true" >> /var/lib/lxc/$nom/rootfs/etc/mosquitto/mosquitto.conf
    echo "persistence_location /var/lib/mosquitto/" >> /var/lib/lxc/$nom/rootfs/etc/mosquitto/mosquitto.conf
    echo "log_dest file /var/log/mosquitto/mosquitto.log" >> /var/lib/lxc/$nom/rootfs/etc/mosquitto/mosquitto.conf
    echo "include_dir /etc/mosquitto/conf.d" >> /var/lib/lxc/$nom/rootfs/etc/mosquitto/mosquitto.conf
    echo "listener 1883 0.0.0.0" >> /var/lib/lxc/$nom/rootfs/etc/mosquitto/mosquitto.conf
    echo "allow_anonymous true" >> /var/lib/lxc/$nom/rootfs/etc/mosquitto/mosquitto.conf

    echo "Le fichier de configuration mosquitto.conf a été modifié. "
fi
echo " "
exit 0