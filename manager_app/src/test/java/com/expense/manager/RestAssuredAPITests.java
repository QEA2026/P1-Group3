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

	@BeforeAll
	static void setup() {
		RestAssured.baseURI = "http://127.0.0.1:8080"; // change this when URI changes!
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

		dirtyExpenses = new ArrayList<>();
		dirtyApprovals = new ArrayList<>();

	}

	@AfterAll
	static void teardown() {

		RestAssured.reset();
	}

	@BeforeEach
	void initialize() {
		initializeUsers();
	}

	@AfterEach
	void cleanup() {
		given()
				.when()
				.delete("/users/" + TEST_EMPLOYEE.getId())
				.then()
				.statusCode(201);

		System.out.println("Deleted test employee " + TEST_EMPLOYEE.getId());

		given()
				.when()
				.delete("/users/" + TEST_MANAGER.getId())
				.then()
				.statusCode(201);

		System.out.println("Deleted test manager " + TEST_MANAGER.getId());
		for (Expense expense : dirtyExpenses) {
			given()
					.when()
					.delete("/expenses/" + expense.getId())
					.then()
					.statusCode(201);

		}
		for (Approval approval : dirtyApprovals) {
			given()
					.when()
					.delete("/approvals/" + approval.getId())
					.then()
					.statusCode(201);
		}
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
	@DisplayName("POST new expense")
	void post_newExpense_returnValidResponse() {
		String requestBody = """
				{
				    "user_id": %d,
				    "amount": 999.99,
				    "description": "test expense",
				    "date": "2026-03-12"
				}
				""";
		requestBody = String.format(requestBody, TEST_EMPLOYEE.getId());
		System.out.println(requestBody);
	}

	@Test
	@DisplayName("POST new approval")
	void post_newApproval_returnValidResponse() {
		String requestBody = """
				{
				"expense_id": 999,
				"status": "approved",
				"reviewer": 4,
				"comment": "test",
				"review_date": "2026-07-17"
				}
				""";
		Expense newExpense = given()
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/approvals")
				.then()
				.statusCode(201)
				.body("expense_id", notNullValue())
				.extract().as(Expense.class);
		dirtyExpenses.add(newExpense);
		Approval newApproval = given()
				.when()
				.get("/approvals/expense/" + newExpense.getId())
				.then()
				.statusCode(200)
				.extract().as(Approval.class);
		dirtyApprovals.add(newApproval);

	}
}