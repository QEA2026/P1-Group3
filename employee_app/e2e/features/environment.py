import os
import time

from selenium import webdriver

BASE_URL = "http://localhost:5173"
SLOW_MO = float(os.environ.get("SLOW_MO", "0"))


def before_all(context):
    context.driver = webdriver.Chrome()
    context.driver.implicitly_wait(5)
    context.base_url = BASE_URL

def before_scenario(context, scenario):
    # Clear browser state from previous scenario
    context.driver.delete_all_cookies()

    # Navigate to the app first so JavaScript can access its storage
    context.driver.get(context.base_url)

    context.driver.execute_script("window.localStorage.clear();")
    context.driver.execute_script("window.sessionStorage.clear();")

    # Reload with a clean application state
    context.driver.refresh()

def after_step(context, step):
    if SLOW_MO:
        time.sleep(SLOW_MO)


def after_all(context):
    if SLOW_MO:
        time.sleep(SLOW_MO * 3)
    context.driver.quit()
