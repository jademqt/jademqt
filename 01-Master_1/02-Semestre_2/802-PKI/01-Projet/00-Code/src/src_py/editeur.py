#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys
import json
import paho.mqtt.client as mqtt #import library
import time
import base64

if len(sys.argv) != 5:
    print( "Attention aux arguments. Rappel 3 arguments nécessaires : [1 : nom destinataire] [2 : nom local/expéditeur] [3 : b= message binaire, t= message texte] [4 : message qui est un fichier]")
    exit()

else:
    dest_topic=sys.argv[1]
    nom_local=sys.argv[2]
    type_message=sys.argv[3]
    fichier=sys.argv[4]
    
    #on récupère l'adresse du serveur
    with open("Fichiers/data/Serveur.txt", "r") as var:
        for ligne in var:
            ligne = ligne.rstrip()
    ip_broker=ligne.encode('UTF-8')

    if type_message == "b":
        with open(fichier, "rb") as f:
            encrypted_date = f.read()
        #print("binaire")
        msg_corps = encrypted_date.hex()
    else :
        with open(fichier, "r") as text_file:
            msg_corps = text_file.read()
        #print("texte")
    
    
    #message que l'on souhaite envoyer
    msg={'expediteur': nom_local, 'message' : str(msg_corps)}
    msg_json=json.dumps(msg)
    
    def on_publish(client,userdata,result):             
        #print("data published \n")
        pass

    client = mqtt.Client()                        
    client.on_publish = on_publish                          
    client.connect(ip_broker,1883)
    
    client.publish(dest_topic, msg_json) 
    print("Le message non sécurisé-", msg_json, "- a été envoyé à ", dest_topic)
    
    client.disconnect()    