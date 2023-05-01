import sys
import os

if len(sys.argv) != 3:
    print( "Attention aux arguments. Rappel 2 arguments nécessaires : [chemin 1 : chemin du fichier encrypted.bin (dans msg)] [chemin 2 : chemin du fichier encrypted.bin dans le bon format (dans secure)]")
    exit()
else :
    chemin=sys.argv[1]
    chemin2=sys.argv[2]

    # Lire la chaîne hexadécimale depuis un fichier
    with open(chemin, "r") as f:
        hex_data = f.read().strip()

    # Convertir la chaîne hexadécimale en données binaires
    encrypted_data = bytes.fromhex(hex_data)

    # Écrire les données binaires dans un fichier
    with open(chemin2, "wb") as f:
        f.write(encrypted_data)

    os.remove(chemin)