package com.expense.manager.e2e.steps;

import io.cucumber.java.After;
import org.openqa.selenium.WebDriver;

import com.expense.manager.e2e.context.TestContext;
import com.expense.manager.e2e.hooks.Hooks;
import com.expense.manager.e2e.pages.ManagerDashboardPage;

import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import org.openqa.selenium.chrome.ChromeDriver;

public class DenyExpenseSteps {
    private WebDriver driver;
    private ManagerDashboardPage managerDashboardPage;
    private TestContext context;

    public DenyExpenseSteps(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUpPages() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        managerDashboardPage = new ManagerDashboardPage(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @When("the manager denies the expense")
    public void the_manager_denies_the_expense() {
        managerDashboardPage.clickDeny();
    }
}
