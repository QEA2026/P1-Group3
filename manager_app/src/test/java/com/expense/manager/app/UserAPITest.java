package com.expense.manager.app;

import com.expense.manager.models.Expense;
import com.expense.manager.models.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;



/*
Note: Because of the unique username constraint, this code assumes that some usernames don't exist.
Run this code with the base database from seed.py.
 */
public class UserAPITest {

    static List<User> dirtyUsers;

    public static void deleteDirtyUsers() {
        if (dirtyUsers != null && !dirtyUsers.isEmpty()) {
            for (User user: dirtyUsers) {
                given()
                        .when()
                        .delete("/" + user.getId())
                        .then()
                        .statusCode(200);
            }
        }
        dirtyUsers = new ArrayList<>();
    }

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://127.0.0.1:9090/users"; // change this when URI changes!
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        dirtyUsers = new ArrayList<>();
    }

    @AfterEach
    void cleanUp() {
        deleteDirtyUsers();
    }

    @AfterAll
    static void tearDown() {
        deleteDirtyUsers(); // I'm not sure if this does anything.
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
        // Add the user
        // presumes that the creation path works.
        User expectedUser = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().as(User.class);

        dirtyUsers.add(expectedUser);

        User actualUser = given()
                .when()
                .get("/" + expectedUser.getId())
                .then()
                .statusCode(200)
                .extract().as(User.class);

        assertNotNull(actualUser, "Retrieved null user");

        assertAll(
                () -> assertEquals(expectedUser.getUsername(), actualUser.getUsername(), "Usernames do not match"),
                () -> assertEquals(expectedUser.getPassword(), actualUser.getPassword(), "Passwords do not match"),
                () -> assertEquals(expectedUser.getRole(), actualUser.getRole(), "Roles do not match")
        );
    }

    @Test
    @DisplayName("GET /users/{id} fails when there is no user with a matching ID")
    void get_usersId_nonexistentUserFails() {
        // create a new user
        String requestBody = """
                {
                    "username": "John Delete",
                    "password": "admin456",
                    "role": "Manager"
                }
                """;
        // Add the user
        // presumes that the creation path works.
        User expectedUser = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().as(User.class);


        given()
                .when()
                .delete("/" + expectedUser.getId())
                .then()
                .statusCode(200);

        String responseString = given()
                .when()
                .get("/" + expectedUser.getId())
                .then()
                .statusCode(200)
                .extract().asString();

        assertTrue(responseString.isEmpty(), "Getting a nonexistent user should yield an empty response");
    }

    @Test
    @DisplayName("POST /users creates a new user and retrieves valid response")
    void post_users_createSuccessValidResponse() {
        String requestBody = """
				{
				    "username": "Oscar Test",
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

        assertNotNull(newUser, "New user not added");
        assertAll(
                () -> assertEquals("Oscar Test", newUser.getUsername(), "Created user with incorrect username"),
                () -> assertEquals("test_password", newUser.getPassword(), "Created user with incorrect password"),
                () -> assertEquals("Manager", newUser.getRole(), "Created user with incorrect role")
        );
    }

    @Test
    @DisplayName("POST /users throws appropriate error with invalid input")
    void post_users_invalidInputGetsError() {
        // body has invalid role
        String invalidRequestBody = """
                {
                    "username": "John Roleless",
                    "password": "SuperGoodPassword"
                    "role": "invalid role"
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("GET /users/username/{username} retrieves correct user")
    void get_usersUsername_retrieveCorrectUser() {
        String requestBody = """
                {
                    "username": "Jane Employee",
                    "password": "password",
                    "role": "Employee"
                }
                """;
        User expectedUser = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().as(User.class);
        dirtyUsers.add(expectedUser);
        User actualUser = given()
                .when()
                .get("/username/" + expectedUser.getUsername())
                .then()
                .statusCode(200)
                .extract().as(User.class);

        assertNotNull(actualUser);
        assertAll(
                () -> assertEquals(expectedUser.getId(), actualUser.getId(), "User IDs do not match"),
                () -> assertEquals(expectedUser.getPassword(), actualUser.getPassword(), "Passwords do not match"),
                () -> assertEquals(expectedUser.getRole(), actualUser.getRole(), "Roles do not match")
        );
    }



}

