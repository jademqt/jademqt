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

    # Load the ciphertext
    with open(ciphertext_path, "rb") as f:
        ciphertext = f.read()

    # Load the secret
    with open(secret_path, "rb") as f:
        secret = f.read()

    # Create a Cipher object for AES decryption using the secret
    cipher = Cipher(algorithms.AES(secret), modes.ECB(), backend=default_backend())

    # Decrypt the ciphertext using the Cipher object
    decryptor = cipher.decryptor()
    padded_message = decryptor.update(ciphertext) + decryptor.finalize()

    # Remove the PKCS7 padding from the message
    unpadder = padding.PKCS7(128).unpadder()
    message = unpadder.update(padded_message) + unpadder.finalize()

    # Print the decrypted message
    print(message.decode('utf-8'))

    with open("/Fichiers/dernier_msg_recu.txt", "w") as f:
        f.write(message.decode('utf-8'))