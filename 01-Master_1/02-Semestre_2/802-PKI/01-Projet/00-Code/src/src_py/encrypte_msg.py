from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import padding
import sys

if len(sys.argv) != 3:
    print("Attention aux arguments. Rappel 2 arguments nécessaires : [contenu du message ] [secret : fichier avec le chemin]")
    print("arg 1 : ", sys.argv[1])
    print("arg 2 : ", sys.argv[2])
    exit()
else:
    message = sys.argv[1].encode('utf-8')
    secret_path = sys.argv[2]

    # Charger le secret
    with open(secret_path, "rb") as f:
        secret = f.read()

    # Créer un objet Cipher pour le chiffrement AES en utilisant le secret
    # AES est un algorithme de chiffrement de blocs qui peut être utilisé avec différents modes de chiffrement, comme ici ECB.
    # ECB est un mode de chiffrement qui spécifie comment les données doivent être divisées en blocs et comment les blocs doivent être chiffrés
    cipher = Cipher(algorithms.AES(secret), modes.ECB(), backend=default_backend())

    # Ajoute des données PKCS7 au message (padding)
    padder = padding.PKCS7(128).padder()
    padded_message = padder.update(message) + padder.finalize()

    # Cryptage du message (avec ECB)"complété" à l'aide de l'objet Cipher
    encryptor = cipher.encryptor()
    ciphertext = encryptor.update(padded_message) + encryptor.finalize()

    # Écrire le message crypté dans un fichier
    chemin = "/Fichiers/a_envoyer_secu.txt"
    with open(chemin, "wb") as outfile:
        outfile.write(ciphertext)
