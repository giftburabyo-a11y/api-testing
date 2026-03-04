package com.apitest.users;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Feature("Users")
public class UserTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(UserTest.class);

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /users returns all 10 users")
    public void testGetAllUsers() {
        log.info("Fetching all users from {}", UserEndpoint.BASE);
        given().spec(requestSpec)
        .when().get(UserEndpoint.BASE)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(10))
            .body("id", everyItem(notNullValue()));
        log.info("Verified 10 users returned");
    }

    @Test(priority = 2, dataProvider = "validUserIds", dataProviderClass = UserDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /users/{id} returns correct user data")
    public void testGetUserById(int userId, String expectedName, String expectedEmail) {
        log.info("Fetching user ID: {} | Expected name: {} | Expected email: {}", userId, expectedName, expectedEmail);
        given().spec(requestSpec)
        .when().get(UserEndpoint.BASE + "/" + userId)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(userId))
            .body("name", equalTo(expectedName))
            .body("email", equalTo(expectedEmail));
        log.info("User ID {} verified successfully", userId);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify response structure matches user JSON schema")
    public void testUserSchemaValidation() {
        log.info("Validating user schema for user ID 1");
        given().spec(requestSpec)
        .when().get(UserEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK)
            .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
        log.info("User schema validation passed");
    }

    @Test(priority = 4, dataProvider = "invalidUserIds", dataProviderClass = UserDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /users/{id} returns 404 for invalid IDs")
    public void testGetUserNotFound(int invalidId) {
        log.warn("Testing 404 for invalid user ID: {}", invalidId);
        given().spec(requestSpec)
        .when().get(UserEndpoint.BASE + "/" + invalidId)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify relationship: user has many posts")
    public void testGetUserPosts() {
        log.info("Fetching posts for user ID 1");
        given().spec(requestSpec)
        .when().get(UserEndpoint.BASE + "/1/posts")
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
        log.info("User posts relationship verified");
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify relationship: user has many todos")
    public void testGetUserTodos() {
        log.info("Fetching todos for user ID 1");
        given().spec(requestSpec)
        .when().get(UserEndpoint.BASE + "/1/todos")
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
        log.info("User todos relationship verified");
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify relationship: user has many albums")
    public void testGetUserAlbums() {
        log.info("Fetching albums for user ID 1");
        given().spec(requestSpec)
        .when().get(UserEndpoint.BASE + "/1/albums")
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
        log.info("User albums relationship verified");
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /users creates a user and returns 201")
    public void testCreateUser() {
        log.info("Creating new user with payload: {}", UserPayload.create());
        given().spec(requestSpec)
            .body(UserPayload.create())
        .when().post(UserEndpoint.BASE)
        .then().statusCode(ApiConfig.STATUS_CREATED)
            .body("name", equalTo("Test User"))
            .body("id", notNullValue());
        log.info("User created successfully with status 201");
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header and response time for users")
    public void testUserHeaders() {
        log.info("Checking headers and response time for user ID 1");
        given().spec(requestSpec)
        .when().get(UserEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
        log.info("User headers validated");
    }
}