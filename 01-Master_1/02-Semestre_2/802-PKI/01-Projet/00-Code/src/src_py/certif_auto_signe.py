import datetime
from cryptography import x509
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.x509.oid import NameOID
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.serialization import Encoding, PrivateFormat, NoEncryption, PublicFormat

# Generate a private key
private_key = rsa.generate_private_key(
    public_exponent=65537, #recommandé car il petit, impair et peut être calculé rapidement
    key_size=2048,
    backend=default_backend() #Généralement OpenSSL
)

# Extrait la clef publique
public_key = private_key.public_key()

# Définit l'auteur
subject = x509.Name([
    x509.NameAttribute(NameOID.COUNTRY_NAME, "FR"),
    x509.NameAttribute(NameOID.STATE_OR_PROVINCE_NAME, "Marne"),
    x509.NameAttribute(NameOID.LOCALITY_NAME, "Reims"),
    x509.NameAttribute(NameOID.ORGANIZATION_NAME, "URCA"),
    x509.NameAttribute(NameOID.COMMON_NAME, "www.RT0802.com"),
])

# Donne la période de validité
not_valid_before = datetime.datetime.utcnow()
not_valid_after = not_valid_before + datetime.timedelta(days=365)

# Création d'un certificat builder auto signé  X.509
builder = x509.CertificateBuilder().subject_name(
    subject
).issuer_name(
    subject
).public_key(
    public_key
).serial_number(
    x509.random_serial_number()
).not_valid_before(
    not_valid_before
).not_valid_after(
    not_valid_after
).add_extension(
    x509.BasicConstraints(ca=False, path_length=None), critical=True,
)

# Signe le certificat X.509 avec le clef privée
certificate = builder.sign(
    private_key, hashes.SHA256(), 
)

# Sérialiser la clé privée, la clef publique et le certificat X.509 en chaînes encodées PEM 
private_pem = private_key.private_bytes(
    encoding=Encoding.PEM,   #Privacy-Enhanced Mail, format de codage de données largement utilisé pour les clefs dans le milieu de la cryptographie
    format=PrivateFormat.PKCS8, #La représentation de la clef doit être formatée selon la norme PKCS#8 (Public-Key Cryptography Standards #8)
    encryption_algorithm=NoEncryption(), #aucun algorithme de chiffrement ne doit être utilisé pour protéger la clef privée
)

public_pem = public_key.public_bytes(
    encoding=Encoding.PEM,
    format=PublicFormat.SubjectPublicKeyInfo,
)

cert_pem = certificate.public_bytes(Encoding.PEM)

# Enregistrer la clef privée codée en PEM dans un fichier
with open("/Fichiers/secure/Serveur/private_key.pem", "wb") as f:
    f.write(private_pem)

# Enregistrer la clef publique codée en PEM dans un fichier
with open("/Fichiers/secure/Serveur/public_key.pem", "wb") as f:
    f.write(public_pem)

# Enregistrer le certificat codé en PEM dans un fichier
with open("/Fichiers/secure/Serveur/certificate.pem", "wb") as f:
    f.write(cert_pem)
