import sys

if len(sys.argv) != 3:
    print( "Attention aux arguments. Rappel 1 argument nécessaire : [1 : fichier / message avec chemin ] [1 : nom du client expéditeur] ")
    exit()
else :
    message=sys.argv[1]
    nom_expediteur=sys.argv[2]

with open(message, 'r') as file:
    contenu = file.read()

if contenu.strip() != "SYM_OK":
    print("Ce message n'était pas un ack. Echanges symétriques non disponibles.")
else :
    print("Ack receptionné. Les échanges symétriques sont maintenant disponibles.")
    chemin = "/Fichiers/secure/"+nom_expediteur+"/SYM_OK.txt"
    with open(chemin, 'w') as f:
        pass