package com.expense.manager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import io.restassured.RestAssured;

@DisplayName("API Tests")
public class RestAssuredAPITests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "127.0.0.1";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterAll
    static void teardown() {
        RestAssured.reset();
    }
}
