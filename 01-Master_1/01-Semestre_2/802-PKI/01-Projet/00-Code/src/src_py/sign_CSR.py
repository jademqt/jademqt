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
    # Load the CSR from a file
    with open(nom_fichier, 'rb') as f:
        csr_data = f.read()

    # Load the CA's private key used for signing the CSR
    with open('/Fichiers/secure/Serveur/private_key.pem', 'rb') as f:
        key_data = f.read()
        password = b'mdp_pki'
        ca_private_key = serialization.load_pem_private_key(key_data, password=None)

    # Load the CA certificate used for signing the CSR
    with open('/Fichiers/secure/Serveur/certificate.pem', 'rb') as f:
        ca_cert_data = f.read()
        ca_cert = x509.load_pem_x509_certificate(ca_cert_data)

    # Parse the CSR data
    csr = x509.load_pem_x509_csr(csr_data)

    # Create a new certificate builder
    builder = x509.CertificateBuilder()

    # Set the subject of the certificate to the same as the CSR
    builder = builder.subject_name(csr.subject)

    # Set the issuer of the certificate to the CA's subject
    builder = builder.issuer_name(ca_cert.subject)

    # Set the public key from the CSR
    builder = builder.public_key(csr.public_key())

    # Set the serial number for the certificate
    # You may want to use a unique identifier for each certificate you sign
    builder = builder.serial_number(x509.random_serial_number())

    # Set the validity period for the certificate
    builder = builder.not_valid_before(ca_cert.not_valid_before)
    builder = builder.not_valid_after(ca_cert.not_valid_after)

    # Add the appropriate extensions, if any
    builder = builder.add_extension(x509.BasicConstraints(ca=False, path_length=None), critical=True)
    builder = builder.add_extension(x509.SubjectAlternativeName([x509.DNSName('example.com')]), critical=False)

    # Sign the certificate with the CA's private key
    signed_cert = builder.sign(private_key=ca_private_key, algorithm=hashes.SHA256(), backend=default_backend())

    # Get the signature bytes
    signature = signed_cert.signature
    
    # Write the signature bytes to a file
    nom_signature="/Fichiers/secure/"+ str(nom_client) +"/certificate.sig"
    with open(nom_signature, "wb") as f:
        f.write(signature)

    # Save the signed certificate to a file
    nom_CSR_signed="/Fichiers/secure/"+ str(nom_client) +"/signed_CSR.pem"
    with open(nom_CSR_signed, 'wb') as f:
        f.write(signed_cert.public_bytes(serialization.Encoding.PEM))
