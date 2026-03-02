package com.apitest.tests;

import com.apitest.config.ApiConfig;
import com.apitest.utils.RequestBuilder;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("Users Endpoint Tests")
public class UserTests {

    @Test(priority = 1)
    @Story("Get All Users") @Severity(SeverityLevel.BLOCKER)
    public void testGetAllUsers() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.USERS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body("$", hasSize(greaterThan(0)))
                .body("[0].email", notNullValue());
    }

    @Test(priority = 2)
    @Story("Get User By ID") @Severity(SeverityLevel.CRITICAL)
    public void testGetUserById() {
        given().spec(RequestBuilder.getRequestSpec()).pathParam("id", 1)
                .when().get(ApiConfig.USERS_ENDPOINT + "/{id}")
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body("id", equalTo(1))
                .body("name", notNullValue())
                .body("email", notNullValue());
    }

    @Test(priority = 3)
    @Story("User JSON Schema") @Severity(SeverityLevel.CRITICAL)
    public void testGetUserJsonSchema() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.USERS_ENDPOINT + "/1")
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test(priority = 4)
    @Story("User Email Format") @Severity(SeverityLevel.NORMAL)
    public void testUserEmailFormat() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.USERS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body("email", everyItem(containsString("@")));
    }

    @Test(priority = 5)
    @Story("Get User Todos") @Severity(SeverityLevel.NORMAL)
    public void testGetUserTodos() {
        given().spec(RequestBuilder.getRequestSpec()).pathParam("id", 1)
                .when().get(ApiConfig.USERS_ENDPOINT + "/{id}/todos")
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body("$", hasSize(greaterThan(0)))
                .body("[0].userId", equalTo(1));
    }
}