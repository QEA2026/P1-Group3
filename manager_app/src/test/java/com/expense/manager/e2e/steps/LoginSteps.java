package com.expense.manager.e2e.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {
    private WebDriver driver;
    private static final String LOGIN_URL = "http://localhost:5173/";

    @Before
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        if(driver!= null){
            driver.quit();
        }
    }


    @Given("the user is on the manager login page")
    public void the_user_is_on_the_manager_login_page() {
        driver.get(LOGIN_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );

        assertTrue(usernameField.isDisplayed());
    }
    @When("the user enters username {string}")
    public void the_user_enters_username(String username) {
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(username);
    }
    @When("the user enters password {string}")
    public void the_user_enters_password(String password) {
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
    }
    @When("the user clicks the login button")
    public void the_user_clicks_the_login_button() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("the login result should be {string}")
    public void the_login_result_should_be(String result) {
        if (result.equals("success")) {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.textToBePresentInElementLocated(
                    By.tagName("h1"), "Manager Expense Portal"));

            assertTrue(driver.findElement(By.tagName("h1"))
                .getText()
                .contains("Manager Expense Portal"));
        } else {
            WebElement error = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("p.text-red-600")));

            assertEquals("Username or password not valid.", error.getText());
        }
    }

}
