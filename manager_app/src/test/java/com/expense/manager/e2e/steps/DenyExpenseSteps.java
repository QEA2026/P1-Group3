package com.expense.manager.e2e.steps;

import org.openqa.selenium.WebDriver;

import com.expense.manager.e2e.context.TestContext;
import com.expense.manager.e2e.hooks.Hooks;
import com.expense.manager.e2e.pages.ManagerDashboardPage;

import io.cucumber.java.Before;
import io.cucumber.java.en.When;

public class DenyExpenseSteps {
    private WebDriver driver;
    private ManagerDashboardPage managerDashboardPage;
    private TestContext context;

    public DenyExpenseSteps(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUpPages() {
        driver = Hooks.driver;
        managerDashboardPage = new ManagerDashboardPage(driver);
    }


    @When("the manager denies the expense")
    public void the_manager_denies_the_expense() {
        managerDashboardPage.clickDeny();
    }
}
