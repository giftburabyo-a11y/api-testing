package com.apitest.users;

import com.apitest.config.ApiConfig;
import com.apitest.utils.RequestBuilder;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("Users")
public class UserTest {

    @Test(priority = 1)
    @Story("Get All Users") @Severity(SeverityLevel.BLOCKER)
    public void testGetAllUsers() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(UserEndpoint.USERS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(10))
            .body("[0].name", notNullValue())
            .body("[0].email", notNullValue())
            .body("[0].address", notNullValue())
            .body("[0].company", notNullValue());
    }

    @Test(priority = 2)
    @Story("Get User By ID") @Severity(SeverityLevel.CRITICAL)
    public void testGetUserById() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", UserDataProvider.getValidUserId())
        .when().get(UserEndpoint.USER_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(1))
            .body("name", notNullValue())
            .body("email", notNullValue())
            .body("address.city", notNullValue())
            .body("company.name", notNullValue());
    }

    @Test(priority = 3)
    @Story("User JSON Schema") @Severity(SeverityLevel.CRITICAL)
    public void testUserJsonSchema() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", UserDataProvider.getValidUserId())
        .when().get(UserEndpoint.USER_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test(priority = 4)
    @Story("User Email Format") @Severity(SeverityLevel.NORMAL)
    public void testUserEmailFormat() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(UserEndpoint.USERS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("email", everyItem(containsString("@")));
    }

    @Test(priority = 5)
    @Story("Get User Posts") @Severity(SeverityLevel.NORMAL)
    public void testGetUserPosts() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", UserDataProvider.getValidUserId())
        .when().get(UserEndpoint.USER_POSTS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
    }

    @Test(priority = 6)
    @Story("Get User Todos") @Severity(SeverityLevel.NORMAL)
    public void testGetUserTodos() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", UserDataProvider.getValidUserId())
        .when().get(UserEndpoint.USER_TODOS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
    }

    @Test(priority = 7)
    @Story("Get User Albums") @Severity(SeverityLevel.NORMAL)
    public void testGetUserAlbums() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", UserDataProvider.getValidUserId())
        .when().get(UserEndpoint.USER_ALBUMS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
    }

    @Test(priority = 8)
    @Story("Non Existent User") @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentUser() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", UserDataProvider.getInvalidUserId())
        .when().get(UserEndpoint.USER_BY_ID)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }
}
