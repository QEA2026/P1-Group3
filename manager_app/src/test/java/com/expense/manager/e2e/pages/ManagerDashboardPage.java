package com.expense.manager.e2e.pages;

import static org.mockito.Mockito.description;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ManagerDashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ManagerDashboardPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickFilterByPending() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'PENDING')]")
        )).click();
    }

    public boolean findExpense(String expenseDescription){
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//table/tbody/tr/td[4]")
        ));

        List<WebElement> descriptions = driver.findElements(
            By.xpath("//table/tbody/tr/td[4]")
        );

        for (WebElement cell : descriptions) {
            System.out.println("Found expense: " + cell.getText());

            if (cell.getText().equals(expenseDescription)) {
                return true;
            }
        }

        return false;
    }      
}
