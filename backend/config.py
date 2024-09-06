from dotenv import load_dotenv
import os

load_dotenv()


HOSTING = "hackthebookshelf.loca.lt"

#DATABASE
DB_NAME: str = os.getenv("DB_NAME", "")
DB_USER: str = os.getenv("DB_USER", "")
DB_PASSWORD: str = os.getenv("DB_PASSWORD","")
DB_HOST: str = os.getenv("DB_HOST","")
DB_PORT: str = os.getenv("DB_PORT","")
