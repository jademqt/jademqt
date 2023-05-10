import subprocess
import sys

if len(sys.argv) != 3:
    print( "Attention aux arguments. Rappel 2 arguments nécessaires : [c1 : le client qui initie l'échange ] [c2 : le destinataire ]")
    exit()
else :
    c1=sys.argv[1]
    c2=sys.argv[2]
    
    #normalement les clients ne se connaissent pas 
    #c1 envoie son certificat à B en clair
    command = ["lxc-attach", "-n", c1, "--", "python3", "./Fichiers/src/editeur.py", c2, c1, "ns", "/Fichiers/secure/signed_csr.pem"]
    result = subprocess.run(command, capture_output=True, text=True)

    #c2 créé le dossier dans secure pour stocker le certificat du client c1 qui souhaite communiquer avec lui
    dir_path = "/Fichiers/secure/"+ c1
    command2 = ["lxc-attach", "-n", c2, "--", "mkdir", "-p", dir_path]
    result2 = subprocess.run(command2, capture_output=True, text=True)

    #On transfère le message reçu de c1 dans le dossier que l'on vient de créer
    #pour cela on récupère le nom du message
    chemin= "/Fichiers/msg/" + str(c1) 
    command3= ["lxc-attach", "-n", c2, "--", "ls", chemin ]
    result3 = subprocess.run(command3, capture_output=True, text=True)
    nom_message=result3.stdout
    print("nom message", nom_message) 

    #on le bouge dans le bon dossier
    chemin_or = "/Fichiers/msg/" + str(c1) + "/" + nom_message
    chemin_or = chemin_or.rstrip("\n")
    chemin_dest = "/Fichiers/secure/" + str(c1) + "/certificate.pem" 
    command4=["lxc-attach", "-n", c2, "--", "mv", chemin_or, chemin_dest ]
    result4 = subprocess.run(command4, capture_output=True, text=True)
    print(result4)         
