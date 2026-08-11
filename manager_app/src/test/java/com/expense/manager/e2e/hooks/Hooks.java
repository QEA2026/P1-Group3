package com.expense.manager.e2e.hooks;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    public static WebDriver driver;

    @Before(order = 0)
    public void setup() throws MalformedURLException {
        String seleniumUrl = System.getProperty("selenium.url", System.getenv("SELENIUM_URL"));
        if (seleniumUrl == null || seleniumUrl.isBlank()) {
            seleniumUrl = "http://selenium:4444";
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new RemoteWebDriver(new URL(seleniumUrl), options);
    }

    @After(order = 0)
    public void teardown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}