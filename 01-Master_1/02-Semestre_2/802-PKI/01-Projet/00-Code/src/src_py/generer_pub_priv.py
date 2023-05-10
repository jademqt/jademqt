from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.backends import default_backend
from cryptography import x509
from cryptography.x509.oid import NameOID
from cryptography.hazmat.primitives.serialization import Encoding, PrivateFormat, NoEncryption, PublicFormat

# Générer une clé privée
private_key = rsa.generate_private_key(
    public_exponent=65537,
    key_size=2048,
    backend=default_backend()
)

# Définir le nom du sujet pour le CSR
# Constructeur de la classe x509
# Pour rappel x509 est une norme pour la gestion des certificats numériques utilisés pour l'authentification et l'établissement de connexions sécurisées entre les entités
subject = x509.Name([
    x509.NameAttribute(NameOID.COUNTRY_NAME, "FR"),
    x509.NameAttribute(NameOID.STATE_OR_PROVINCE_NAME, "Marne"),
    x509.NameAttribute(NameOID.LOCALITY_NAME, "Reims"),
    x509.NameAttribute(NameOID.ORGANIZATION_NAME, "URCA"),
    x509.NameAttribute(NameOID.COMMON_NAME, "www.RT0802.com"),
])

# Créer le constructeur de CSR
# l.28 spécifie le nom du sujet de la CSR
builder = x509.CertificateSigningRequestBuilder().subject_name(
    subject
).add_extension(
    x509.BasicConstraints(ca=False, path_length=None), critical=True,
).add_extension(
    x509.SubjectAlternativeName([x509.DNSName(u"www.example.com")]), critical=False,
)

# Signer la CSR avec la clef privée : 
# lorsque toutes les extensions et les informations de sujet ont été ajoutées, la CSR peut être signée en utilisant la méthode sign()
csr = builder.sign(
    private_key, hashes.SHA256(), 
)

# Sérialiser la clé privée, la clé publique et la CSR en chaînes encodées PEM

private_pem = private_key.private_bytes(
    encoding=Encoding.PEM, #Privacy-Enhanced Mail, format de codage de données largement utilisé pour les clefs dans le milieu de la cryptographie
    format=PrivateFormat.PKCS8, #La représentation de la clef doit être formatée selon la norme PKCS#8 (Public-Key Cryptography Standards #8)
    encryption_algorithm=NoEncryption(),
)

public_pem = private_key.public_key().public_bytes(
    encoding=Encoding.PEM,
    format=PublicFormat.SubjectPublicKeyInfo,
)

csr_pem = csr.public_bytes(Encoding.PEM)

# Write the PEM-encoded private key to a file
with open("/Fichiers/secure/private_key.pem", "wb") as f:
    f.write(private_pem)

# Write the PEM-encoded public key to a file
with open("/Fichiers/secure/public_key.pem", "wb") as f:
    f.write(public_pem)

# Write the PEM-encoded CSR to a file
with open("/Fichiers/secure/csr.pem", "wb") as f:
    f.write(csr_pem)
