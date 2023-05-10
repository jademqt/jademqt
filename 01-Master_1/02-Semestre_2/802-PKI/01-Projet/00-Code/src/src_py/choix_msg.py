import random
import sys


if len(sys.argv) != 2:
    print( "Attention aux arguments. Rappel 1 argument nécessaire : [1 : nombre de clients]")
    exit()
else :
    nbr_clients=sys.argv[1]
    
    #Génération d'une communication entre deux clients pris au hasard.
    num1 = random.randint(1, int(nbr_clients)-1)

    while True:
        num2 = random.randint(1, int(nbr_clients)-1)
        if num2 != num1:
            break

    
    #On génère un message aléatoire 
    import random
    debut = ['Bonjour !', 'Salut !', 'Hey !']
    etat = ['Je suis fatiguée', 'Je suis en pleine forme', 'Je suis content', 'Je suis beau', 'Je suis soucieuse']
    donc = ['donc ', 'par conséquence', ' alors ', 'mais bonne nouvelle', 'mais mauvaise nouvelle']
    fin = ['ma mamie arrive demain', 'je mange du sucre.. oups', 'je ne dors que le matin', 'ça va', 'ca va pas trop..']

    sentence = f"{random.choice(debut)} {random.choice(etat)} {random.choice(donc)} {random.choice(fin)}."

    c1 = "client_" + str(num1)
    c2 = "client_" + str(num2)
    #c1= "client_1"
    #c2= "client_2"

    with open('/home/adm_jma/message_en_cours.txt', 'w') as f:
        msg = c1+ "\n"+ c2+ "\n"+ sentence
        f.write(msg)
    f.close()
   
    print("Le ", c1, " veut envoyer le message ' ", sentence , "' au ", c2)