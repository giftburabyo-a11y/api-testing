package com.apitest.tests;

import com.apitest.config.ApiConfig;
import com.apitest.utils.RequestBuilder;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("GET Requests")
public class GetRequestTests {

    @Test(priority = 1)
    @Story("Get All Posts") @Severity(SeverityLevel.BLOCKER)
    public void testGetAllPosts() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .contentType(containsString("application/json"))
                .body("$", hasSize(greaterThan(0)))
                .body("[0].id", notNullValue())
                .body("[0].title", notNullValue());
    }

    @Test(priority = 2)
    @Story("Get Post By ID") @Severity(SeverityLevel.CRITICAL)
    public void testGetPostById() {
        given().spec(RequestBuilder.getRequestSpec())
                .pathParam("id", 1)
                .when().get(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", notNullValue());
    }

    @Test(priority = 3)
    @Story("Validate Response Body") @Severity(SeverityLevel.NORMAL)
    public void testGetPostResponseBody() {
        Response response = given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT + "/1")
                .then().statusCode(ApiConfig.STATUS_OK).extract().response();

        assert response.jsonPath().getInt("id") == 1;
        assert !response.jsonPath().getString("title").isEmpty();
    }

    @Test(priority = 4)
    @Story("Validate Response Headers") @Severity(SeverityLevel.NORMAL)
    public void testGetPostsResponseHeaders() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .header("Content-Type", containsString("application/json"));
    }

    @Test(priority = 5)
    @Story("JSON Schema Validation") @Severity(SeverityLevel.CRITICAL)
    public void testGetPostJsonSchema() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT + "/1")
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test(priority = 6)
    @Story("Filter By Query Param") @Severity(SeverityLevel.NORMAL)
    public void testGetPostsByUserId() {
        given().spec(RequestBuilder.getRequestSpec())
                .queryParam("userId", 1)
                .when().get(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body("userId", everyItem(equalTo(1)));
    }

    @Test(priority = 7)
    @Story("Non-Existent Resource") @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentPost() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT + "/99999")
                .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 8)
    @Story("Get Post Comments") @Severity(SeverityLevel.MINOR)
    public void testGetPostComments() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT + "/1/comments")
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body("$", hasSize(greaterThan(0)))
                .body("[0].email", notNullValue());
    }

    @Test(priority = 9)
    @Story("Response Time") @Severity(SeverityLevel.MINOR)
    public void testGetPostsResponseTime() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .time(lessThan(5000L));
    }
}