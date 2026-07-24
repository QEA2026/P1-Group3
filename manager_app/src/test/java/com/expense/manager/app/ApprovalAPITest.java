package com.expense.manager.app;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class ApprovalAPITest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://127.0.0.1:9090/approvals";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
