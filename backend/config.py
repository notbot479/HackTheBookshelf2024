from dotenv import load_dotenv
import os

load_dotenv()


#BASE
HOSTING = "hackthebookshelf.loca.lt"
BASE_DIR = os.path.dirname(os.path.abspath(__file__))

#FIREBASE
FIREBASE_CERTIFICATE_PATH = os.path.join(BASE_DIR, 'firebase-certificate.json')

#DATABASE
DB_NAME: str = os.getenv("DB_NAME", "")
DB_USER: str = os.getenv("DB_USER", "")
DB_PASSWORD: str = os.getenv("DB_PASSWORD","")
DB_HOST: str = os.getenv("DB_HOST","")
DB_PORT: str = os.getenv("DB_PORT","")
