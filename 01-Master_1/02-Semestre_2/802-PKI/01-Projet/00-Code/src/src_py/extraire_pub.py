from cryptography import x509
from cryptography.hazmat.primitives.serialization import load_pem_private_key
from cryptography.hazmat.primitives import serialization
import sys

if len(sys.argv) != 2:
    print( "Attention aux arguments. Rappel 1 argument nécessaire : [1 : c1, expéditeur] ")
    exit()
else :
    c1=sys.argv[1]
    
    # Load the signed CSR certificate file
    path_signed_csr="/Fichiers/secure/"+c1+"/certificate.pem"
    with open(path_signed_csr, 'rb') as f:
        cert = x509.load_pem_x509_certificate(f.read())

    # Extract the public key from the certificate
    public_key = cert.public_key()

    # Serialize the public key to PEM format
    public_key_pem = public_key.public_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PublicFormat.SubjectPublicKeyInfo
    )

    # Write the public key to a file
    path_public="/Fichiers/secure/"+c1+"/public_key.pem"
    with open(path_public, 'wb') as f:
        f.write(public_key_pem)