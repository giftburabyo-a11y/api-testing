package com.apitest.todos;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Feature("Todos")
public class TodoTest extends BaseTest {

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /todos returns all 200 todos with valid fields")
    public void testGetAllTodos() {
        given()
            .spec(requestSpec)
        .when()
            .get(TodoEndpoint.BASE)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(200))
            .body("id", everyItem(notNullValue()))
            .body("completed", everyItem(notNullValue()));
    }

    @Test(priority = 2, dataProvider = "validTodoIds", dataProviderClass = TodoDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /todos/{id} returns correct todo data for valid IDs")
    public void testGetTodoById(int todoId, int expectedUserId, String expectedTitle, boolean expectedCompleted) {
        given()
            .spec(requestSpec)
        .when()
            .get(TodoEndpoint.BASE + "/" + todoId)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(todoId))
            .body("userId", equalTo(expectedUserId))
            .body("title", equalTo(expectedTitle))
            .body("completed", equalTo(expectedCompleted));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /todos?completed=true returns only completed todos")
    public void testFilterCompletedTodos() {
        given()
            .spec(requestSpec)
            .queryParam("completed", true)
        .when()
            .get(TodoEndpoint.BASE)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("completed", everyItem(equalTo(true)));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /todos?completed=false returns only incomplete todos")
    public void testFilterIncompleteTodos() {
        given()
            .spec(requestSpec)
            .queryParam("completed", false)
        .when()
            .get(TodoEndpoint.BASE)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("completed", everyItem(equalTo(false)));
    }

    @Test(priority = 5, dataProvider = "invalidTodoIds", dataProviderClass = TodoDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /todos/{id} returns 404 for non-existent IDs")
    public void testGetTodoNotFound(int invalidId) {
        given()
            .spec(requestSpec)
        .when()
            .get(TodoEndpoint.BASE + "/" + invalidId)
        .then()
            .statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /todos creates a new todo and returns 201")
    public void testCreateTodo() {
        given()
            .spec(requestSpec)
            .body(TodoPayload.create())
        .when()
            .post(TodoEndpoint.BASE)
        .then()
            .statusCode(ApiConfig.STATUS_CREATED)
            .body("userId", equalTo(1))
            .body("title", equalTo("Test Todo Item"))
            .body("completed", equalTo(false))
            .body("id", notNullValue());
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify PUT /todos/1 updates the todo and returns 200")
    public void testUpdateTodo() {
        given()
            .spec(requestSpec)
            .body(TodoPayload.update())
        .when()
            .put(TodoEndpoint.BASE + "/1")
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("title", equalTo("Updated Todo Item"))
            .body("completed", equalTo(true));
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header and response time are within expected bounds")
    public void testTodoHeaders() {
        given()
            .spec(requestSpec)
        .when()
            .get(TodoEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
    }
}