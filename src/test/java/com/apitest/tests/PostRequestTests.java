package com.apitest.tests;

import com.apitest.config.ApiConfig;
import com.apitest.models.Post;
import com.apitest.utils.RequestBuilder;
import com.apitest.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

@Epic("JSONPlaceholder API Tests")
@Feature("POST Requests")
public class PostRequestTests {

    @Test(priority = 1)
    @Story("Create New Post") @Severity(SeverityLevel.BLOCKER)
    public void testCreatePost() {
        Post newPost = TestDataGenerator.createValidPost();
        given().spec(RequestBuilder.getRequestSpec()).body(newPost)
                .when().post(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.STATUS_CREATED)
                .body("id", notNullValue())
                .body("title", equalTo(newPost.getTitle()))
                .body("userId", equalTo(newPost.getUserId()));
    }

    @Test(priority = 2)
    @Story("POST Response Headers") @Severity(SeverityLevel.NORMAL)
    public void testCreatePostResponseHeaders() {
        given().spec(RequestBuilder.getRequestSpec())
                .body(TestDataGenerator.createValidPost())
                .when().post(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.STATUS_CREATED)
                .header("Content-Type", containsString("application/json"));
    }

    @Test(priority = 3)
    @Story("Create Post with Map Body") @Severity(SeverityLevel.NORMAL)
    public void testCreatePostWithMapBody() {
        given().spec(RequestBuilder.getRequestSpec())
                .body(TestDataGenerator.createPostAsMap())
                .when().post(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.STATUS_CREATED)
                .body("title", equalTo("Map-based Post Title"));
    }

    @Test(priority = 4)
    @Story("Extract Post ID") @Severity(SeverityLevel.CRITICAL)
    public void testCreatePostExtractId() {
        Response response = given().spec(RequestBuilder.getRequestSpec())
                .body(TestDataGenerator.createValidPost())
                .when().post(ApiConfig.POSTS_ENDPOINT)
                .then().statusCode(ApiConfig.STATUS_CREATED).extract().response();

        assertTrue(response.jsonPath().getInt("id") > 0);
    }

    @Test(priority = 5)
    @Story("POST Performance") @Severity(SeverityLevel.MINOR)
    public void testCreatePostResponseTime() {
        given().spec(RequestBuilder.getRequestSpec())
                .body(TestDataGenerator.createValidPost())
                .when().post(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.STATUS_CREATED)
                .time(lessThan(5000L));
    }
}