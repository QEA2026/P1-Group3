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
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
	private static List<User> dirtyUsers;

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
			System.out.println("Deleted user " + TEST_EMPLOYEE.getId());
			TEST_EMPLOYEE = null;
		}
		if (TEST_MANAGER != null) {
			given()
					.delete("/users/" + TEST_MANAGER.getId())
					.then()
					.statusCode(201);
			System.out.println("Deleted user " + TEST_MANAGER.getId());
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

	/**
	 * Posts a test expense that is used for multiple tests.
	 * This expense is added to dirtyExpenses.
	 */
	static Expense postTestExpense() {

		String expenseRequestBody = """
				{
					"user_id": %d,
					"amount": 999.99,
					"description": "test expense",
					"date": "2026-06-01"
				}
				""";
		expenseRequestBody = String.format(expenseRequestBody, TEST_EMPLOYEE.getId());

		Expense retExpense = given()
				.contentType(ContentType.JSON)
				.body(expenseRequestBody)
				.post("/expenses")
				.then()
				.statusCode(201)
				.extract().as(Expense.class);
		dirtyExpenses.add(retExpense);
		System.out.println("Added test expense " + retExpense.getId());
		return retExpense;

	}

	static Approval postTestApproval(int expenseId) {
		return postTestApproval(expenseId, "pending");
	}

	/**
	 * Posts a test approval with a given expense id that is used for multiple
	 * tests.
	 * This approval is added to dirtyApprovals.
	 */
	static Approval postTestApproval(int expenseId, String status) {
		String approvalRequestBody = """
				{
					"expense_id": %d,
					"status": "%s",
					"reviewer": %d,
					"comment": "test approval comment",
					"review_date": "2026-06-02"
				}
				""";
		approvalRequestBody = String.format(approvalRequestBody, expenseId, status, TEST_MANAGER.getId());
		Approval newApproval = given()
				.contentType(ContentType.JSON)
				.body(approvalRequestBody)
				.post("/approvals")
				.then()
				.extract().as(Approval.class);
		dirtyApprovals.add(newApproval);
		System.out.println("Added test approval " + newApproval.getId());
		return newApproval;
	}

	static void deleteApprovals() {
		if (dirtyApprovals == null || dirtyApprovals.size() == 0) {
			return;
		}
		for (Approval approval : dirtyApprovals) {
			given()
					.delete("/approvals/" + approval.getId())
					.then();
			System.out.println("Deleted approval " + approval.getId());
		}
		dirtyApprovals = new ArrayList<>();
	}

	static void deleteDirtyUsers() {
		if (dirtyUsers == null || dirtyUsers.size() == 0) {
			return;
		}
		for (User user : dirtyUsers) {
			given()
					.delete("/users/" + user.getId())
					.then();
			System.out.println("Deleted user " + user.getId());
		}
		dirtyUsers = new ArrayList<>();
	}

	@BeforeAll
	static void setup() {
		RestAssured.baseURI = "http://127.0.0.1:8080"; // change this when URI changes!
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

		dirtyExpenses = new ArrayList<>();
		dirtyApprovals = new ArrayList<>();
		dirtyUsers = new ArrayList<>();
		initializeUsers();

	}

	@AfterAll
	static void teardown() {
		deleteUsers();
		RestAssured.reset();
	}

	@AfterEach
	void cleanup() {
		deleteApprovals();
		deleteExpenses();
		deleteDirtyUsers();
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
		assertNotNull(newExpense.getId());
		assertEquals("test expense", newExpense.getDescription());
		assertEquals(TEST_EMPLOYEE.getId(), newExpense.getUser_id());
		System.out.println("Added test expense " + newExpense.getId());
	}

	@Test
	@DisplayName("POST approval")
	void post_approval_getValidResponse() {

		Expense newExpense = postTestExpense();

		String approvalRequestBody = """
				{
					"expense_id": %d,
					"status": "pending",
					"reviewer": %d,
					"comment": "test approval comment",
					"review_date": "2026-06-02"
				}
				""";
		approvalRequestBody = String.format(approvalRequestBody, newExpense.getId(), TEST_MANAGER.getId());
		Approval newApproval = given()
				.contentType(ContentType.JSON)
				.body(approvalRequestBody)
				.post("/approvals")
				.then()
				.statusCode(201)
				.extract().as(Approval.class);
		assertNotNull(newApproval);
		assertNotNull(newApproval.getId());
		System.out.println("Added test approval " + newApproval.getId());
		dirtyApprovals.add(newApproval);
	}

	@Test
	@DisplayName("Test login with valid credentials")
	void post_userslogin_validCredentialsGetsLogin() {
		List<User> usersToTest = new ArrayList<>();
		usersToTest.add(TEST_EMPLOYEE);
		usersToTest.add(TEST_MANAGER);
		for (User user : usersToTest) {
			String loginRequestBody = """
					{
						"username": "%s",
						"password": "%s"
					}
					""";
			loginRequestBody = String.format(loginRequestBody, user.getUsername(), user.getPassword());
			User responseUser = given()
					.contentType(ContentType.JSON)
					.body(loginRequestBody)
					.post("/users/login")
					.then()
					.statusCode(200)
					.extract().as(User.class);
			assertEquals(responseUser.getId(), user.getId());
		}
	}

	@Test
	@DisplayName("Test login with invalid password")
	void post_usersLogin_invalidCredentialsNoLogin() {
		List<User> invalidCredentials = new ArrayList<>();
		invalidCredentials.add(new User(TEST_EMPLOYEE.getId(), TEST_EMPLOYEE.getUsername(), "invalid password",
				TEST_EMPLOYEE.getRole()));
		invalidCredentials.add(new User(TEST_EMPLOYEE.getId(), "invalid name", TEST_EMPLOYEE.getPassword(),
				TEST_EMPLOYEE.getRole()));
		invalidCredentials.add(new User(TEST_MANAGER.getId(), TEST_MANAGER.getUsername(), "invalid password",
				TEST_MANAGER.getRole()));
		invalidCredentials.add(new User(TEST_MANAGER.getId(), "invalid name", TEST_MANAGER.getPassword(),
				TEST_MANAGER.getRole()));
		for (User user : invalidCredentials) {
			String loginRequestBody = """
					{
						"username": "%s",
						"password": "%s"
					}
					""";
			loginRequestBody = String.format(loginRequestBody, user.getUsername(), user.getPassword());
			given()
					.contentType(ContentType.JSON)
					.body(loginRequestBody)
					.post("/users/login")
					.then()
					.statusCode(404);
		}
	}

	@Test
	@DisplayName("GET specific expense")
	void get_expenses_specificExpense() {
		Expense newExpense = postTestExpense();

		Expense responseExpense = given()
				.get("/expenses/" + newExpense.getId())
				.then()
				.statusCode(200)
				.extract().as(Expense.class);
		assertEquals(responseExpense.getId(), newExpense.getId());
	}

	@Test
	@DisplayName("GET specific approval")
	void get_approvals_specificApproval() {
		Expense newExpense = postTestExpense();
		Approval newApproval = postTestApproval(newExpense.getId());
		Approval responseApproval = given()
				.get("/approvals/" + newApproval.getId())
				.then()
				.statusCode(200)
				.extract().as(Approval.class);
		assertEquals(newApproval.getId(), responseApproval.getId());
		assertEquals(newApproval.getComment(), responseApproval.getComment());
	}

	@Test
	@DisplayName("Getting a non-existent expense gets 404")
	void get_expenses_invalidGets404() {
		Expense newExpense = postTestExpense();
		deleteExpenses();
		given()
				.get("expenses/" + newExpense.getId())
				.then()
				.statusCode(404);
	}

	@Test
	@DisplayName("Getting a non-existent approval gets 404")
	void get_approvals_invalidGets404() {
		Expense newExpense = postTestExpense();
		Approval newApproval = postTestApproval(newExpense.getId());
		deleteApprovals();
		deleteExpenses();
		given()
				.get("approvals/" + newApproval.getId())
				.then()
				.statusCode(404);
	}

	@Test
	@DisplayName("PUT correctly updates expense")
	void put_expenses_correctlyUpdatesExpense() {
		Expense newExpense = postTestExpense();
		String expenseUpdateRequestBody = """
				{
					"user_id": %d,
					"amount": 500.00,
					"description": "new description",
					"date": "2026-07-01"
				}
				""";
		expenseUpdateRequestBody = String.format(expenseUpdateRequestBody, TEST_EMPLOYEE.getId());
		Expense updatedExpense = given()
				.contentType(ContentType.JSON)
				.body(expenseUpdateRequestBody)
				.put("/expenses/" + newExpense.getId())
				.then()
				.statusCode(201)
				.extract().as(Expense.class);
		assertNotNull(updatedExpense);
		assertEquals("new description", updatedExpense.getDescription());
		assertEquals("2026-07-01", updatedExpense.getDate());
		assertEquals(500.00, updatedExpense.getAmount());
	}

	// this test is failing currently
	@Test
	@DisplayName("PUT gets 404 when updating non-existent expense")
	void put_expenses_nonexistentExpenseGets404() {
		Expense newExpense = postTestExpense();
		deleteExpenses();
		String expenseUpdateRequestBody = """
				{
					"user_id": %d,
					"amount": 500.00,
					"description": "new description",
					"date": "2026-07-01"
				}
				""";
		given()
				.contentType(ContentType.JSON)
				.body(expenseUpdateRequestBody)
				.put("/expenses/" + newExpense.getId())
				.then()
				.statusCode(404);
	}

	@Test
	@DisplayName("GET all expenses for a user")
	void get_expensesuser_getAllUserExpenses() {
		Expense expense1 = postTestExpense();
		Expense expense2 = postTestExpense();
		Expense expense3 = postTestExpense();
		List<Expense> expectedExpenses = List.of(expense1, expense2, expense3);
		List<Expense> actualExpenses = given()
				.get("/expenses/user/" + TEST_EMPLOYEE.getId())
				.then()
				.statusCode(200)
				.extract().response().jsonPath().getList("$", Expense.class);
		assertEquals(expectedExpenses.size(), actualExpenses.size());
		for (int i = 0; i < actualExpenses.size(); i++) {
			Expense expectedExpense = expectedExpenses.get(i);
			Expense actualExpense = actualExpenses.get(i);
			assertEquals(expectedExpense.getId(), actualExpense.getId());
			assertEquals(expectedExpense.getDescription(), actualExpense.getDescription());
			assertEquals(expectedExpense.getDate(), actualExpense.getDate());
		}
	}

	@Test
	@DisplayName("GET matching approval for expense")
	void get_approvalsexpense_getMatchingApproval() {
		Expense originalExpense = postTestExpense();
		Approval originalApproval = postTestApproval(originalExpense.getId());
		Approval responseApproval = given()
				.get("/approvals/expense/" + originalExpense.getId())
				.then()
				.statusCode(200)
				.extract().as(Approval.class);
		assertEquals(originalExpense.getId(), responseApproval.getExpense_id());
		assertEquals(originalApproval.getId(), responseApproval.getId());
	}

	@Test
	@DisplayName("GET specific user")
	void get_user_getSpecificUser() {
		List<User> expectedUsers = List.of(TEST_EMPLOYEE, TEST_MANAGER);
		for (User expectedUser : expectedUsers) {
			User actualUser = given()
					.get("/users/" + expectedUser.getId())
					.then()
					.extract().as(User.class);
			assertEquals(expectedUser.getId(), actualUser.getId());
			assertEquals(expectedUser.getRole(), actualUser.getRole());
		}
	}

	@Test
	@DisplayName("Trying to GET specific user gets 404 when user doesn't exist")
	void get_user_nonExistent404() {
		String tempUserBody = """
				{
					"username": "test temp user",
					"password": "12345678",
					"role": "Employee"
				}
				""";
		User tempUser = given()
				.contentType(ContentType.JSON)
				.body(tempUserBody)
				.post("/users")
				.then()
				.extract().as(User.class);
		given()
				.delete("/users/" + tempUser.getId())
				.then();
		given()
				.get("/users/" + tempUser.getId())
				.then()
				.statusCode(404);
	}

	@Test
	@DisplayName("POST new user")
	void post_user_getNewUser() {
		String newUserBody = """
				{
					"username": "temp test user",
					"password": "soelifhaks",
					"role": "Employee"
				}
				""";
		User newUser = given()
				.contentType(ContentType.JSON)
				.body(newUserBody)
				.post("/users")
				.then()
				.statusCode(201)
				.extract().as(User.class);
		assertNotNull(newUser.getId());
		assertEquals("temp test user", newUser.getUsername());
		dirtyUsers.add(newUser);
	}

	@Test
	@DisplayName("GET history for user")
	void get_expensesuserhistory_nonPendingExpenses() {
		Expense pendingExpense = postTestExpense();
		Expense approvedExpense = postTestExpense();
		Expense deniedExpense = postTestExpense();
		postTestApproval(pendingExpense.getId(), "pending");
		postTestApproval(approvedExpense.getId(), "approved");
		postTestApproval(deniedExpense.getId(), "denied");
		List<Expense> history = given()
				.get("/expenses/user/" + TEST_EMPLOYEE.getId() + "/history")
				.then()
				.statusCode(200)
				.extract().response().jsonPath().getList("$", Expense.class);
		assertEquals(2, history.size());
	}

}