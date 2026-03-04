package com.apitest.todos;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Feature("Todos")
public class TodoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(TodoTest.class);

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /todos returns all 200 todos")
    public void testGetAllTodos() {
        log.info("Fetching all todos from {}", TodoEndpoint.BASE);
        given().spec(requestSpec)
        .when().get(TodoEndpoint.BASE)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(200))
            .body("id", everyItem(notNullValue()));
        log.info("Verified 200 todos returned");
    }

    @Test(priority = 2, dataProvider = "validTodoIds", dataProviderClass = TodoDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /todos/{id} returns correct todo data")
    public void testGetTodoById(int todoId, int expectedUserId, String expectedTitle) {
        log.info("Fetching todo ID: {} | Expected userId: {} | Expected title: {}", todoId, expectedUserId, expectedTitle);
        given().spec(requestSpec)
        .when().get(TodoEndpoint.BASE + "/" + todoId)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(todoId))
            .body("userId", equalTo(expectedUserId))
            .body("title", equalTo(expectedTitle));
        log.info("Todo ID {} verified successfully", todoId);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /todos?completed=true returns only completed todos")
    public void testFilterCompletedTodos() {
        log.info("Filtering todos by completed=true");
        given().spec(requestSpec)
            .queryParam("completed", true)
        .when().get(TodoEndpoint.BASE)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("completed", everyItem(equalTo(true)));
        log.info("Completed todos filter verified");
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /todos?completed=false returns only incomplete todos")
    public void testFilterIncompleteTodos() {
        log.info("Filtering todos by completed=false");
        given().spec(requestSpec)
            .queryParam("completed", false)
        .when().get(TodoEndpoint.BASE)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("completed", everyItem(equalTo(false)));
        log.info("Incomplete todos filter verified");
    }

    @Test(priority = 5, dataProvider = "invalidTodoIds", dataProviderClass = TodoDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /todos/{id} returns 404 for invalid IDs")
    public void testGetTodoNotFound(int invalidId) {
        log.warn("Testing 404 for invalid todo ID: {}", invalidId);
        given().spec(requestSpec)
        .when().get(TodoEndpoint.BASE + "/" + invalidId)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /todos creates a todo and returns 201")
    public void testCreateTodo() {
        log.info("Creating new todo with payload: {}", TodoPayload.create());
        given().spec(requestSpec)
            .body(TodoPayload.create())
        .when().post(TodoEndpoint.BASE)
        .then().statusCode(ApiConfig.STATUS_CREATED)
            .body("title", equalTo("Test Todo Title"))
            .body("completed", equalTo(false))
            .body("id", notNullValue());
        log.info("Todo created successfully with status 201");
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify PUT /todos/1 fully updates the todo")
    public void testUpdateTodo() {
        log.info("Updating todo ID 1 with full payload");
        given().spec(requestSpec)
            .body(TodoPayload.update())
        .when().put(TodoEndpoint.BASE + "/1")
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("title", equalTo("Updated Todo Title"))
            .body("id", equalTo(1));
        log.info("Todo ID 1 updated successfully");
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header and response time for todos")
    public void testTodoHeaders() {
        log.info("Checking headers and response time for todo ID 1");
        given().spec(requestSpec)
        .when().get(TodoEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
        log.info("Todo headers validated");
    }
}