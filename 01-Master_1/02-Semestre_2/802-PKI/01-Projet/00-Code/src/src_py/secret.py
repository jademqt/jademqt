import secrets
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives import hashes
import sys

if len(sys.argv) != 2:
    print( "Attention aux arguments. Rappel 1 argument nécessaire : [1 : c1, expéditeur]")
    exit()
else :
    c1=sys.argv[1]
    
    
    # 1. Générer un secret de 32 bits et l'enregistrer sur c2
    secret = secrets.token_bytes(4)
    # pour être sûr que la taille est un multiple de 16
    # En effet, AES fonctionne avec des blocs de 16 octets. 
    # Si ce n'est pas le cas on complète avec des octets nuls. 
    secret += b'\0' * (16 - len(secret) % 16)
    chemin1 = "/Fichiers/secure/"+c1+"/secret.pem"
    with open(chemin1, 'wb') as f:
        f.write(secret)

    #print( "Secret sur c2 : ", secret)

    # 2. Chiffrer le secret avec AES-128
    backend = default_backend() #renvoie le meilleur backend disponible sur la plateforme d'exécution
    key = secrets.token_bytes(16)  # la clef AES-128
    cipher = Cipher(algorithms.AES(key), modes.ECB(), backend=backend)
    encryptor = cipher.encryptor()
    encrypted_secret = encryptor.update(secret) + encryptor.finalize()

    # 3. Chiffrer le secret chiffré avec la clef publique de c1
    # On charge la clef publique
    chemin2= "/Fichiers/secure/"+c1+"/public_key.pem"
    with open(chemin2, 'rb') as f:
        public_key = serialization.load_pem_public_key(
            f.read(),
            backend=backend
        )
    encrypted_key = public_key.encrypt(
        key,
        padding.OAEP( # Optimal Asymmetric Encryption Padding : permet d'améliorer la sécurité du chiffrement RSA en ajoutant un mécanisme de padding aléatoire et un mécanisme de hashage.
            mgf=padding.MGF1(algorithm=hashes.SHA256()),
            algorithm=hashes.SHA256(),
            label=None
        )
    )

    # 4. Écrire le résultat dans un fichier pour l'envoyer à c1
    chemin3= "/Fichiers/secure/"+c1+"/encrypted.bin"
    with open(chemin3, 'wb') as f:
        f.write(encrypted_secret + encrypted_key)


