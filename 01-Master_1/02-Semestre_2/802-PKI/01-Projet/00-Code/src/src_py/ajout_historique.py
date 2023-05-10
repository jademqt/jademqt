import sys
import datetime

if len(sys.argv) != 5:
    print( "Attention aux arguments. Rappel 4 arguments nécessaires : [c1 : expéditeur ] [c2 : destinataire ] [msg : message envoyé ] [ moi : qui appelle ce script]")
    exit()
else :
    c1=sys.argv[1]
    c2=sys.argv[2]
    msg=sys.argv[3]
    moi=sys.argv[4]

    if c1 == moi :
        c1_d = "j'ai "
        c1_f = "moi "
        c2_d = c2 + " a "
        c2_f = c2 + " "
    else:
        c2_d = "j'ai "
        c2_f = "moi "
        c1_d = c1 + " a "
        c1_f = c1 + " "

    now = datetime.datetime.now()

    date = now.strftime("%Y-%m-%d %H:%M")

    ma_ligne= "Le " + date + " : " + c1_d + "envoyé à " + c2_f + "le message : " + msg + "\n"
    with open("/Fichiers/historique.txt", "a") as f:
        f.write(ma_ligne)