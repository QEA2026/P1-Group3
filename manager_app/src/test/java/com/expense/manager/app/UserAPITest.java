package com.expense.manager.app;

import com.expense.manager.models.Expense;
import com.expense.manager.models.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAPITest {

    static List<User> dirtyUsers;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://127.0.0.1:9090/users"; // change this when URI changes!
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        dirtyUsers = new ArrayList<>();
    }

    @AfterEach
    void cleanUp() {
        if (dirtyUsers != null && !dirtyUsers.isEmpty()) {
            for (User user: dirtyUsers) {
                given()
                        .when()
                        .delete("/" + user.getId())
                        .then()
                        .statusCode(200);
            }
        }
    }


    @Test
    @DisplayName("GET /users retrieves a list of all users")
    void get_users_RetrieveAllUsers() {
         List<User> resultUsers = given()
                .when()
                .get()
                .then()
                .statusCode(200)
                 .extract().response().jsonPath().getList("$", User.class);
         assertNotNull(resultUsers);
    }

    @Test
    @DisplayName("GET /users/{id} retrieves the specified user")
    void get_usersId_returnSpecificUser() {
        String requestBody = """
                {
                    "username": "John Manager",
                    "password": "admin456",
                    "role": "Manager"
                }
                """;

    }

    @Test
    @DisplayName("POST /users creates a new user and retrieves valid response")
    void post_users_createSuccessValidResponse() {
        String requestBody = """
				{
				    "username": "Oscar",
				    "password": "test_password",
				    "role": "Manager"
				}
				""";

        User newUser = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().as(User.class);

        dirtyUsers.add(newUser);

        assertNotNull(newUser);
        assertAll(
                () -> assertEquals("Oscar", newUser.getUsername()),
                () -> assertEquals("test_password", newUser.getPassword()),
                () -> assertEquals("Manager", newUser.getRole())
        );
    }

}

