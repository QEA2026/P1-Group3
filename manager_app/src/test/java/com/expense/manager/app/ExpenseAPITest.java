package com.expense.manager.app;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ExpenseAPITest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://127.0.0.1:9090/expenses";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @DisplayName("GET all expenses returns all expenses")
    @Test
    void get_expenses_ReturnsEverything() {
        given()
                .get()
                .then()
                .statusCode(200);
    }

//    @DisplayName
}
