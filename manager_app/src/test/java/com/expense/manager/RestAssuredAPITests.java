package com.expense.manager;
//REST assured Setup and First Tests

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("API Tests")
public class RestAssuredAPITests {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://127.0.0.1:8080"; // change this when URI changes!
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterAll
    static void teardown() {
        RestAssured.reset();
    }

    @Test
    @DisplayName("Health of API is OK")
    void get_health_isOkay() {
        given()
                .log().all()
                .when()
                .get("/health")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("All approvals GET path")
    void get_approvals_returnsSuccess() {
        given()
                .log().all()
                .when()
                .get("/approvals")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("All users GET path")
    void get_users_returnsSuccess() {
        given()
                .log().all()
                .when()
                .get("/users")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Alll expenses GET path")
    void get_expenses_returnsSuccess() {
        given()
                .log().all()
                .get("/expenses")
                .then()
                .log().all()
                .statusCode(200);
    }

    // @Test
    // @DisplayName("POST new approval")
    // void post_newApproval_returnValidResponse() {
    // String requestBody = """
    // {
    // "expense_id": 999,
    // "status": "approved",
    // "reviewer": 4,
    // "comment": "test",
    // "review_date": "2026-07-17"
    // }
    // """;
    // given()
    // .contentType(ContentType.JSON)
    // .body(requestBody)
    // .when()
    // .post("/approvals")
    // .then()
    // .statusCode(201)
    // .body("expense_id", notNullValue())
    // .body("comment", equalTo("test"));
    // }
}