import os
import time

from selenium import webdriver

BASE_URL = "http://localhost:5174"
SLOW_MO = float(os.environ.get("SLOW_MO", "0"))


def before_all(context):
    context.driver = webdriver.Chrome()
    context.driver.implicitly_wait(5)
    context.base_url = BASE_URL


def after_step(context, step):
    if SLOW_MO:
        time.sleep(SLOW_MO)


def after_all(context):
    if SLOW_MO:
        time.sleep(SLOW_MO * 3)
    context.driver.quit()
