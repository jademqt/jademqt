import sys
from cryptography import x509
from cryptography.hazmat.primitives import serialization, hashes
from cryptography.hazmat.primitives.asymmetric import rsa, padding
from cryptography.hazmat.backends import default_backend
from cryptography.x509.oid import NameOID

if len(sys.argv) != 3:
    print( "Attention aux arguments. Rappel 2 arguments nécessaire : [1 : fichier CSR avec son chemin depuis 'Fichiers'] [2: nom du client] ")
    exit()

else:
    nom_fichier=sys.argv[1]
    nom_client=sys.argv[2]
    # Charge le CSR à partir d'un fichier
    with open(nom_fichier, 'rb') as f:
        csr_data = f.read()

    # Charge la clef privée de l'autorité de certification utilisée pour signer le CSR
    with open('/Fichiers/secure/Serveur/private_key.pem', 'rb') as f:
        key_data = f.read()
        ca_private_key = serialization.load_pem_private_key(key_data, password=None)

    # Charge le certificat de l'autorité de certification utilisé pour signer le CSR
    with open('/Fichiers/secure/Serveur/certificate.pem', 'rb') as f:
        ca_cert_data = f.read()
        ca_cert = x509.load_pem_x509_certificate(ca_cert_data)

    # Chargement de la CSR au format PEM  à partir de csr_data qui contient les données de celle ci.
    csr = x509.load_pem_x509_csr(csr_data)

    # Créé un object builder qui permet de créer un certificat x509
    builder = x509.CertificateBuilder()

    # Permet de spécifier le nom du sujet du certificat à construire (extrait du CSR donné)
    builder = builder.subject_name(csr.subject)

    # Permet de spécifier le nom de l'émetteur
    # Le nom de l'émetteur doit correspondre au nom de l'autorité de certification qui signe le certificat, afin d'assurer l'authenticité et la validité du certificat.
    builder = builder.issuer_name(ca_cert.subject)

    # le nouveau certificat aura la même clef publique que celle qui a été utilisée pour générer la CSR
    builder = builder.public_key(csr.public_key())

    # Crée un numéro de série aléatoire pour le certificat en cours de création
    # Le numéro de série est un identifiant unique attribué à chaque certificat et est utilisé pour le distinguer des autres certificats.
    builder = builder.serial_number(x509.random_serial_number())

    # Définit la période de validité du certificat
    builder = builder.not_valid_before(ca_cert.not_valid_before)
    builder = builder.not_valid_after(ca_cert.not_valid_after)

    # Pas utile à supp (extensions)
    builder = builder.add_extension(x509.BasicConstraints(ca=False, path_length=None), critical=True)
    builder = builder.add_extension(x509.SubjectAlternativeName([x509.DNSName('example.com')]), critical=False)

    # Signe le certificat avec la clef privée de la CA en utilisant l'algorithme de hachage SHA256
    signed_cert = builder.sign(private_key=ca_private_key, algorithm=hashes.SHA256(), backend=default_backend())

    # Extrait la signature numérique du certificat signé. 
    # La signature numérique est un hash du contenu du certificat qui est chiffré à l'aide de la clef privée de la CA.
    # Cette signature permet aux clients de vérifier que le certificat n'a pas été altéré depuis qu'il a été signé par la CA.
    signature = signed_cert.signature
    
    # Ecrit la signature dans un fichier
    nom_signature="/Fichiers/secure/"+ str(nom_client) +"/certificate.sig"
    with open(nom_signature, "wb") as f:
        f.write(signature)

    # Sauvegarde le CSR signé dans un fichier
    nom_CSR_signed="/Fichiers/secure/"+ str(nom_client) +"/signed_CSR.pem"
    with open(nom_CSR_signed, 'wb') as f:
        f.write(signed_cert.public_bytes(serialization.Encoding.PEM))
