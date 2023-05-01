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
    key = private_key.decrypt(
        encrypted_data[-private_key.key_size // 8:],
        padding.OAEP(
             mgf=padding.MGF1(algorithm=hashes.SHA256()),
            algorithm=hashes.SHA256(),
            label=None
        )
    )

    # 3. Déchiffrer en faisant l'inverse d'AES 128
    encrypted_secret = encrypted_data[:-private_key.key_size // 8]
    cipher = Cipher(algorithms.AES(key), modes.ECB(), backend=backend)
    decryptor = cipher.decryptor()
    secret = decryptor.update(encrypted_secret) + decryptor.finalize()

    # 4. Enregistrer le secret de 32 bits
    #print(" Secret sur c1 : ", secret)
 
    chemin2 = "/Fichiers/secure/" + c2 + "/secret.pem"
    with open(chemin2, "wb") as f:
        f.write(secret)

