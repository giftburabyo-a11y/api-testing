package com.apitest.users;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Feature("Users")
public class UserTest extends BaseTest {

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /users returns all 10 users with valid fields")
    public void testGetAllUsers() {
        given()
            .spec(requestSpec)
        .when()
            .get(UserEndpoint.BASE)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(10))
            .body("id", everyItem(notNullValue()))
            .body("email", everyItem(containsString("@")));
    }

    @Test(priority = 2, dataProvider = "validUserIds", dataProviderClass = UserDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /users/{id} returns correct user data for valid IDs")
    public void testGetUserById(int userId, String expectedName, String expectedUsername) {
        given()
            .spec(requestSpec)
        .when()
            .get(UserEndpoint.BASE + "/" + userId)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(userId))
            .body("name", equalTo(expectedName))
            .body("username", equalTo(expectedUsername));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify response structure matches user JSON schema contract")
    public void testUserSchemaValidation() {
        given()
            .spec(requestSpec)
        .when()
            .get(UserEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test(priority = 4, dataProvider = "invalidUserIds", dataProviderClass = UserDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /users/{id} returns 404 for non-existent IDs")
    public void testGetUserNotFound(int invalidId) {
        given()
            .spec(requestSpec)
        .when()
            .get(UserEndpoint.BASE + "/" + invalidId)
        .then()
            .statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /users/1/posts returns posts belonging to user 1")
    public void testGetUserPosts() {
        given()
            .spec(requestSpec)
        .when()
            .get(UserEndpoint.BASE + "/1/posts")
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /users/1/todos returns todos belonging to user 1")
    public void testGetUserTodos() {
        given()
            .spec(requestSpec)
        .when()
            .get(UserEndpoint.BASE + "/1/todos")
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /users/1/albums returns albums belonging to user 1")
    public void testGetUserAlbums() {
        given()
            .spec(requestSpec)
        .when()
            .get(UserEndpoint.BASE + "/1/albums")
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /users creates a new user and returns 201")
    public void testCreateUser() {
        given()
            .spec(requestSpec)
            .body(UserPayload.create())
        .when()
            .post(UserEndpoint.BASE)
        .then()
            .statusCode(ApiConfig.STATUS_CREATED)
            .body("name", equalTo("Test User"))
            .body("email", equalTo("testuser@automation.com"))
            .body("id", notNullValue());
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header and response time are within expected bounds")
    public void testUserHeaders() {
        given()
            .spec(requestSpec)
        .when()
            .get(UserEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
    }
}