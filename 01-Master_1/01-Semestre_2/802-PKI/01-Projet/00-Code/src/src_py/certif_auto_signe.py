import datetime
from cryptography import x509
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.x509.oid import NameOID
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.serialization import Encoding, PrivateFormat, NoEncryption, PublicFormat

# Generate a private key
private_key = rsa.generate_private_key(
    public_exponent=65537,
    key_size=2048,
    backend=default_backend()
)

# Extract the public key
public_key = private_key.public_key()

# Define the subject name
subject = x509.Name([
    x509.NameAttribute(NameOID.COUNTRY_NAME, "FR"),
    x509.NameAttribute(NameOID.STATE_OR_PROVINCE_NAME, "Marne"),
    x509.NameAttribute(NameOID.LOCALITY_NAME, "Reims"),
    x509.NameAttribute(NameOID.ORGANIZATION_NAME, "URCA"),
    x509.NameAttribute(NameOID.COMMON_NAME, "www.RT0802.com"),
])

# Define the validity period
not_valid_before = datetime.datetime.utcnow()
not_valid_after = not_valid_before + datetime.timedelta(days=365)

# Create a self-signed X.509 certificate builder
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

# Sign the X.509 certificate with the private key
certificate = builder.sign(
    private_key, hashes.SHA256(), 
)

# Serialize the private key, public key, and X.509 certificate to PEM-encoded strings
private_pem = private_key.private_bytes(
    encoding=Encoding.PEM,
    format=PrivateFormat.PKCS8,
    encryption_algorithm=NoEncryption(),
)

public_pem = public_key.public_bytes(
    encoding=Encoding.PEM,
    format=PublicFormat.SubjectPublicKeyInfo,
)

cert_pem = certificate.public_bytes(Encoding.PEM)

# Write the PEM-encoded private key to a file
with open("/Fichiers/secure/Serveur/private_key.pem", "wb") as f:
    f.write(private_pem)

# Write the PEM-encoded public key to a file
with open("/Fichiers/secure/Serveur/public_key.pem", "wb") as f:
    f.write(public_pem)

# Write the PEM-encoded certificate to a file
with open("/Fichiers/secure/Serveur/certificate.pem", "wb") as f:
    f.write(cert_pem)
