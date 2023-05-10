#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys
import paho.mqtt.client as mqtt #import library
import time
import json
from datetime import datetime

if len(sys.argv) != 2:
    print( "Il manque le nom du client, doit etre celui qui execute ce code. ")
    exit()

else:
    mon_topic=sys.argv[1]
        
    #on récupère l'adresse du serveur
    with open("Fichiers/data/local.txt", "r") as var:
        for ligne in var:
            ligne = ligne.rstrip()
    ip_broker=ligne.encode('UTF-8')
    print("ip broker " , ip_broker)

    global messageReceived
    messageReceived=False

    #créé un fichier par message reçu
    def creer_recep_msg(msg):
        msg_decode=json.loads(msg.payload)
        date=datetime.now()
        nom_fichier="msg_"+str(date.minute)+str(date.second)
        chemin="Fichiers/msg/"+str(msg_decode['expediteur'])+"/"+str(nom_fichier)
        fichier=open(chemin, 'w')
        fichier.write(str(msg_decode["message"]))

        
    #abonnement au topic (son topic)
    def on_connect(client, userdata, flags, rc):
        print("Connected with result code "+str(rc))
        client.subscribe(str(mon_topic))

    #traitement à la reception d un message
    def on_message(client, userdata, msg):
        creer_recep_msg(msg)
        print(msg.topic +" "+str(msg.payload.decode("utf-8")))

    #appel aux différentes fonctions / connexions
    
    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message
    client.connect(ip_broker, 1883, 60)

    client.loop_forever()



