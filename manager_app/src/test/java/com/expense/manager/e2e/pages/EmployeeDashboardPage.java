package com.expense.manager.e2e.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmployeeDashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public EmployeeDashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickSubmitNewExpense() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Submit New Expense')]")
        )).click();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Logout')]")
        )).click();
    }
}