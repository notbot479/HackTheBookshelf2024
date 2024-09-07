from dotenv import load_dotenv
import os

load_dotenv()


#BASE
HOSTING = "hackthebookshelf.loca.lt"
BASE_DIR = os.path.dirname(os.path.abspath(__file__))

#SMTP
SMTP_EMAIL_BACKEND = 'django.core.mail.backends.smtp.EmailBackend'
SMTP_EMAIL_HOST = os.getenv("SMTP_EMAIL_HOST", "")
SMTP_EMAIL_USE_TLS = os.getenv("SMTP_EMAIL_USE_TLS", "")
SMTP_EMAIL_PORT = os.getenv("SMTP_EMAIL_PORT", "")
SMTP_EMAIL_HOST_USER = os.getenv("SMTP_EMAIL_HOST_USER", "")
SMTP_EMAIL_HOST_PASSWORD = os.getenv("SMTP_EMAIL_HOST_PASSWORD", "")
SMTP_DEFAULT_FROM_EMAIL = os.getenv("SMTP_DEFAULT_FROM_EMAIL", "")

#FIREBASE
FIREBASE_CERTIFICATE_PATH = os.path.join(BASE_DIR, 'firebase-certificate.json')

#DATABASE
DB_NAME: str = os.getenv("DB_NAME", "")
DB_USER: str = os.getenv("DB_USER", "")
DB_PASSWORD: str = os.getenv("DB_PASSWORD","")
DB_HOST: str = os.getenv("DB_HOST","")
DB_PORT: str = os.getenv("DB_PORT","")
