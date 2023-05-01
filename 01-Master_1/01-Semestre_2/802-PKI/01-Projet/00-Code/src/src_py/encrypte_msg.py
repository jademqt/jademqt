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

    # Load the secret
    with open(secret_path, "rb") as f:
        secret = f.read()

    # Create a Cipher object for AES encryption using the secret
    cipher = Cipher(algorithms.AES(secret), modes.ECB(), backend=default_backend())

    # Add PKCS7 padding to the message
    padder = padding.PKCS7(128).padder()
    padded_message = padder.update(message) + padder.finalize()

    # Encrypt the padded message using the Cipher object
    encryptor = cipher.encryptor()
    ciphertext = encryptor.update(padded_message) + encryptor.finalize()

    # Write the encrypted message to a file
    chemin = "/Fichiers/a_envoyer_secu.txt"
    with open(chemin, "wb") as outfile:
        outfile.write(ciphertext)
