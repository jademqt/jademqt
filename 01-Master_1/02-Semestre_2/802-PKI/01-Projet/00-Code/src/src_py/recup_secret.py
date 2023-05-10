from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives import hashes
import sys


if len(sys.argv) != 2:
    print( "Attention aux arguments. Rappel 1 argument nécessaire : [1 : c2, expéditeur/celui qui a généré le secret]")
    exit()
else :
    c2=sys.argv[1]

    # 1. Ouvrir le fichier encrypted.bin
    chemin = "/Fichiers/secure/" + c2 + "/encrypted.bin"
    with open(chemin, 'rb') as f:
        encrypted_data = f.read()

    # 2. Déchiffrer avec la clé privée
    backend = default_backend()
    with open('/Fichiers/secure/private_key.pem', 'rb') as f:
        private_key = serialization.load_pem_private_key(
            f.read(),
            password=None,
            backend=backend
        )
    # Les données chiffrées sont passées en entrée de la méthode decrypt
    # Le schéma de padding OAEP est également passée en entrée pour spécifier comment les données sont chiffrées. 
    # Rappel OAEP : technique de padding utilisée pour sécuriser les opérations de chiffrement asymétrique (ex RSA).
    # Ce padding permet d'ajouter des données aléatoires à un message avant le chiffrement, pour rendre le chiffrement plus résistant aux attaques.
    key = private_key.decrypt(
        encrypted_data[-private_key.key_size // 8:], # extrait la partie des données chiffrées qui correspond à la taille de la clef privée. Cela permet de supprimer les octets de remplissage qui ont été ajoutés lors du chiffrement avec OAEP.
        padding.OAEP(
             mgf=padding.MGF1(algorithm=hashes.SHA256()), # MGF1 algorithme de masquage de génération de masque (MGF) pour produire des masques aléatoires.
            algorithm=hashes.SHA256(), # algo de hachage pour calculer les fonctions de hachage utilisées dans le schéma de padding
            label=None # permet d'ajouter des données supplémentaires aux fonctions de hachage
        )
    )

    # 3. Déchiffrer en faisant l'inverse d'AES 128
    encrypted_secret = encrypted_data[:-private_key.key_size // 8] # correspond à la valeur d'initialisation (IV) qui a été placée avant les données chiffrées.
    cipher = Cipher(algorithms.AES(key), modes.ECB(), backend=backend)
    decryptor = cipher.decryptor()
    secret = decryptor.update(encrypted_secret) + decryptor.finalize()

    # 4. Enregistrer le secret de 32 bits
    #print(" Secret sur c1 : ", secret)
 
    chemin2 = "/Fichiers/secure/" + c2 + "/secret.pem"
    with open(chemin2, "wb") as f:
        f.write(secret)

