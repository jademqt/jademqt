from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.backends import default_backend
from cryptography import x509
from cryptography.x509.oid import NameOID
from cryptography.hazmat.primitives.serialization import Encoding, PrivateFormat, NoEncryption, PublicFormat

# Generate a private key
private_key = rsa.generate_private_key(
    public_exponent=65537,
    key_size=2048,
    backend=default_backend()
)

# Define the subject name for the CSR
subject = x509.Name([
    x509.NameAttribute(NameOID.COUNTRY_NAME, "FR"),
    x509.NameAttribute(NameOID.STATE_OR_PROVINCE_NAME, "Marne"),
    x509.NameAttribute(NameOID.LOCALITY_NAME, "Reims"),
    x509.NameAttribute(NameOID.ORGANIZATION_NAME, "URCA"),
    x509.NameAttribute(NameOID.COMMON_NAME, "www.RT0802.com"),
])

# Create the CSR builder
builder = x509.CertificateSigningRequestBuilder().subject_name(
    subject
).add_extension(
    x509.BasicConstraints(ca=False, path_length=None), critical=True,
).add_extension(
    x509.SubjectAlternativeName([x509.DNSName(u"www.example.com")]), critical=False,
)

# Sign the CSR with the private key
csr = builder.sign(
    private_key, hashes.SHA256(), 
)

# Serialize the private key, public key, and CSR to PEM-encoded strings
private_pem = private_key.private_bytes(
    encoding=Encoding.PEM,
    format=PrivateFormat.PKCS8,
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
