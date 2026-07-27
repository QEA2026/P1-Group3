import os
import subprocess
import sys
import time

from selenium import webdriver

BASE_URL = "http://localhost:5174"
SLOW_MO = float(os.environ.get("SLOW_MO", "0"))

FEATURES_DIR = os.path.dirname(os.path.abspath(__file__))
EMPLOYEE_APP_DIR = os.path.dirname(os.path.dirname(FEATURES_DIR))
REPO_ROOT = os.path.dirname(EMPLOYEE_APP_DIR)
DB_DIR = os.path.join(EMPLOYEE_APP_DIR, "db")
DB_FILE = os.path.join(REPO_ROOT, "expenses_system_db.db")


def reset_database():
    if os.path.exists(DB_FILE):
        os.remove(DB_FILE)
    subprocess.run([sys.executable, "seed.py"], cwd=DB_DIR, check=True)


def before_scenario(context, scenario):
    reset_database()
    context.driver = webdriver.Chrome()
    context.driver.implicitly_wait(5)
    context.base_url = BASE_URL


def after_step(context, step):
    if SLOW_MO:
        time.sleep(SLOW_MO)


def after_scenario(context, scenario):
    if SLOW_MO:
        time.sleep(SLOW_MO * 2)
    context.driver.quit()
