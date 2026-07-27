import os
import sys
import time

from selenium import webdriver

BASE_URL = "http://localhost:5174"
SLOW_MO = float(os.environ.get("SLOW_MO", "0"))

FEATURES_DIR = os.path.dirname(os.path.abspath(__file__))
EMPLOYEE_APP_DIR = os.path.dirname(os.path.dirname(FEATURES_DIR))
DB_DIR = os.path.join(EMPLOYEE_APP_DIR, "db")

sys.path.insert(0, DB_DIR)
from db import get_connection, init_db
import seed as seed_module


def reset_database():
    init_db()

    conn = get_connection()
    conn.execute("DELETE FROM approvals")
    conn.execute("DELETE FROM expenses")
    conn.execute("DELETE FROM users")
    conn.commit()
    conn.close()

    seed_module.seed(get_connection())


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