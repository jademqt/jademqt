import sys
from cryptography import x509
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import rsa, padding
from cryptography.hazmat.backends import default_backend


if len(sys.argv) != 3:
    print( "Attention aux arguments. Rappel 2 arguments nécessaires : [1 : c1, expéditeur] [2 : c2, destinataire]")
    exit()
else :
    c1=sys.argv[1]
    c2=sys.argv[2]

    #on charge le certificat qu'on souhaite vérifier
    chemin_cert= "/Fichiers/secure/"+c1+"/certificate.pem"
    chemin_pub_pki= "/Fichiers/secure/Serveur/public_key_pki.pem"
    chemin_sign_cert="/Fichiers/secure/"+c1+"/certificate.sig" 
    
    # Load the certificate to be verified
    with open(chemin_cert, "rb") as f:
        cert_bytes = f.read()
    cert = x509.load_pem_x509_certificate(cert_bytes, default_backend())

    # Load the public key of the certificate's signer
    with open(chemin_pub_pki, "rb") as f:
        signer_public_key_bytes = f.read()
    signer_public_key = serialization.load_pem_public_key(signer_public_key_bytes, default_backend())

    # Load the signature of the certificate
    with open(chemin_sign_cert, "rb") as f:
        signature_bytes = f.read()

    # Verify the signature using the signer's public key
    try:
        signer_public_key.verify(
            signature_bytes,
            cert.tbs_certificate_bytes,
            padding.PKCS1v15(),
            cert.signature_hash_algorithm,
        )
        print("Certificate signature is valid.")
    except:
        print("Certificate signature is invalid.")