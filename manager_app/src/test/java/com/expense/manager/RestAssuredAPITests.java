package com.expense.manager;
//REST assured Setup and First Tests

/**
 * HOW TO RUN
 * 
 * Start with an EMPTY database - run db.py instead of seed.py in employee_app/db
 * Database is assumed empty and is cleared completely between and after tests.
 */

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import com.expense.manager.models.Approval;
import com.expense.manager.models.Expense;
import com.expense.manager.models.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@DisplayName("API Tests")
public class RestAssuredAPITests {
	private static User TEST_EMPLOYEE;
	private static User TEST_MANAGER;
	private static List<Expense> dirtyExpenses;
	private static List<Approval> dirtyApprovals;

	static void initializeUsers() {
		// create a new test employee and test manager
		String testEmployeeBody = """
				{
				    "username": "test employee",
				    "password": "password123",
				    "role": "Employee"
				}
				""";
		String testManagerBody = """
				{
				    "username": "test manager",
				    "password": "password456",
				    "role": "Manager"
				}
				""";

		TEST_EMPLOYEE = given()
				.contentType(ContentType.JSON)
				.body(testEmployeeBody)
				.when()
				.post("/users")
				.then()
				.statusCode(201)
				.extract()
				.as(User.class);
		System.out.println("Test employee ID: " + TEST_EMPLOYEE.getId());

		TEST_MANAGER = given()
				.contentType(ContentType.JSON)
				.body(testManagerBody)
				.when()
				.post("/users")
				.then()
				.statusCode(201)
				.extract()
				.as(User.class);
		System.out.println("Test manager ID: " + TEST_MANAGER.getId());
	}

	static void deleteUsers() {
		if (TEST_EMPLOYEE != null) {
			given()
					.delete("/users/" + TEST_EMPLOYEE.getId())
					.then()
					.statusCode(201);
			TEST_EMPLOYEE = null;
		}
		if (TEST_MANAGER != null) {
			given()
					.delete("/users/" + TEST_MANAGER.getId())
					.then()
					.statusCode(201);
			TEST_MANAGER = null;
		}
	}

	static void deleteExpenses() {
		if (dirtyExpenses == null || dirtyExpenses.size() == 0) {
			return;
		}
		for (Expense expense : dirtyExpenses) {
			given()
					.delete("/expenses/" + expense.getId())
					.then()
					.statusCode(201);
			System.out.println("Deleted expense " + expense.getId());
		}
		dirtyExpenses = new ArrayList<>();
		System.out.println("dirtyExpenses cleared.");
	}

	static void deleteApprovals() {
		if (dirtyApprovals == null || dirtyApprovals.size() == 0) {
			return;
		}
		for (Approval approval : dirtyApprovals) {
			given()
					.delete("/approvals/" + approval.getId())
					.then()
					.statusCode(201);
			System.out.println("Deleted approval " + approval.getId());
		}
		dirtyApprovals = new ArrayList<>();
	}

	@BeforeAll
	static void setup() {
		RestAssured.baseURI = "http://127.0.0.1:8080"; // change this when URI changes!
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

		dirtyExpenses = new ArrayList<>();
		dirtyApprovals = new ArrayList<>();
		initializeUsers();

	}

	@AfterAll
	static void teardown() {
		deleteUsers();
		RestAssured.reset();
	}

	@AfterEach
	void cleanup() {
		deleteExpenses();
		deleteApprovals();
	}

	@Test
	@DisplayName("Health of API is OK")
	void get_health_isOkay() {
		given()
				.when()
				.get("/health")
				.then()
				.statusCode(200);
	}

	@Test
	@DisplayName("All approvals GET path")
	void get_approvals_returnsSuccess() {
		given()
				.when()
				.get("/approvals")
				.then()
				.statusCode(200);
	}

	@Test
	@DisplayName("All users GET path")
	void get_users_returnsSuccess() {
		given()
				.when()
				.get("/users")
				.then()
				.statusCode(200);
	}

	@Test
	@DisplayName("All expenses GET path")
	void get_expenses_returnsSuccess() {
		given()
				.get("/expenses")
				.then()
				.statusCode(200);
	}

	@Test
	@DisplayName("POST expense")
	void post_expense_getValidResponse() {

		// new_expense = Expense(
		// id=None,
		// user_id=data['user_id'],
		// amount=data['amount'],
		// description=data['description'],
		// date=data['date']
		// )
		String expenseRequestBody = """
				{
					"user_id": %d,
					"amount": 999.99,
					"description": "test expense",
					"date": "2026-06-01"
				}
				""";
		expenseRequestBody = String.format(expenseRequestBody, TEST_EMPLOYEE.getId());

		Expense newExpense = given()
				.contentType(ContentType.JSON)
				.body(expenseRequestBody)
				.post("/expenses")
				.then()
				.statusCode(201)
				.extract().as(Expense.class);
		dirtyExpenses.add(newExpense);
		System.out.println("Added test expense " + newExpense.getId());
	}

}