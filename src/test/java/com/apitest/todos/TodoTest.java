package com.apitest.todos;

import com.apitest.config.ApiConfig;
import com.apitest.utils.RequestBuilder;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("Todos")
public class TodoTest {

    @Test(priority = 1)
    @Story("Get All Todos") @Severity(SeverityLevel.BLOCKER)
    public void testGetAllTodos() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(TodoEndpoint.TODOS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(200))
            .body("[0].userId", notNullValue())
            .body("[0].title", notNullValue())
            .body("[0].completed", notNullValue());
    }

    @Test(priority = 2)
    @Story("Get Todo By ID") @Severity(SeverityLevel.CRITICAL)
    public void testGetTodoById() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", TodoDataProvider.getValidTodoId())
        .when().get(TodoEndpoint.TODO_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", notNullValue())
            .body("completed", notNullValue());
    }

    @Test(priority = 3)
    @Story("Filter Completed Todos") @Severity(SeverityLevel.NORMAL)
    public void testFilterCompletedTodos() {
        given().spec(RequestBuilder.getRequestSpec())
            .queryParam("completed", TodoDataProvider.getCompletedStatus())
        .when().get(TodoEndpoint.TODOS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("completed", everyItem(equalTo(true)));
    }

    @Test(priority = 4)
    @Story("Filter Incomplete Todos") @Severity(SeverityLevel.NORMAL)
    public void testFilterIncompleteTodos() {
        given().spec(RequestBuilder.getRequestSpec())
            .queryParam("completed", TodoDataProvider.getIncompleteStatus())
        .when().get(TodoEndpoint.TODOS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("completed", everyItem(equalTo(false)));
    }

    @Test(priority = 5)
    @Story("Non Existent Todo") @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentTodo() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", TodoDataProvider.getInvalidTodoId())
        .when().get(TodoEndpoint.TODO_BY_ID)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 6)
    @Story("Response Time") @Severity(SeverityLevel.MINOR)
    public void testTodosResponseTime() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(TodoEndpoint.TODOS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .time(lessThan(5000L));
    }
}
