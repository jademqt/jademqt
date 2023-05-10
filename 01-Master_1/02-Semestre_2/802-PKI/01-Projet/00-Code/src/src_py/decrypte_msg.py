from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import padding
import sys
import subprocess

if len(sys.argv) != 3:
    print("Attention aux arguments. Rappel 2 arguments nécessaires : [fichier à décrypter ] [secret : fichier avec le chemin]")
    print("arg 1 : ", sys.argv[1])
    print("arg 2 : ", sys.argv[2])
    exit()
else:
    ciphertext_path = sys.argv[1]
    secret_path = sys.argv[2]

    # Charger le texte chiffré
    with open(ciphertext_path, "rb") as f:
        ciphertext = f.read()

    # Charge le secret
    with open(secret_path, "rb") as f:
        secret = f.read()

    # Créer un objet Cipher pour le décryptage AES en utilisant le secret
    #   La classe Cipher est utilisée pour chiffrer et déchiffrer des données à l'aide d'algorithmes de chiffrement symétrique.
    #   Pour créer un objet Cipher,il faut spécifier trois choses : l'algorithme de chiffrement à utiliser, le mode de chiffrement et la clé secrète.
    #   ECB est le mode de chiffrement le plus simple. Chaque bloque de données est chiffré de manière indépendante. CBC serait mieux.
    cipher = Cipher(algorithms.AES(secret), modes.ECB(), backend=default_backend())

    # Décryptage du texte chiffré à l'aide de l'objet Cipher
    decryptor = cipher.decryptor()
    padded_message = decryptor.update(ciphertext) + decryptor.finalize()

    # Supprimer le remplissage PKCS7 du message
    # Pour rappel le padding à pour but d'ajouter des bits supplémentaires à la fin des données avant de les chiffrer.
    # Il est utilisé pour garantir que les données ont une longueur fixe et sont donc plus difficiles à attaquer en utilisant des attaques basées sur la longueur
    unpadder = padding.PKCS7(128).unpadder() #supprime le padding de la donnée déchiffrée. Elle est importante pour garantir la sécurité et la cohérence des données déchiffrées.
    message = unpadder.update(padded_message) + unpadder.finalize() # update supprime le padding du bloc de données et finalize() est appelée pour supprimer tout padding restant et renvoyer la donnée déchiffrée originale, sans le padding

    # Affiche le message décrypté
    print(message.decode('utf-8'))

    with open("/Fichiers/dernier_msg_recu.txt", "w") as f:
        f.write(message.decode('utf-8'))