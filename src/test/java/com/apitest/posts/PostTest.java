package com.apitest.posts;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Feature("Posts")
public class PostTest extends BaseTest {

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /posts returns all 100 posts with valid fields")
    public void testGetAllPosts() {
        given()
            .spec(requestSpec)
        .when()
            .get(PostEndpoint.BASE)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(100))
            .body("id", everyItem(notNullValue()))
            .body("title", everyItem(notNullValue()));
    }

    @Test(priority = 2, dataProvider = "validPostIds", dataProviderClass = PostDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /posts/{id} returns correct post data for valid IDs")
    public void testGetPostById(int postId, int expectedUserId, String expectedTitleStart) {
        given()
            .spec(requestSpec)
        .when()
            .get(PostEndpoint.BASE + "/" + postId)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(postId))
            .body("userId", equalTo(expectedUserId))
            .body("title", startsWith(expectedTitleStart));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify response structure matches post JSON schema contract")
    public void testPostSchemaValidation() {
        given()
            .spec(requestSpec)
        .when()
            .get(PostEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test(priority = 4, dataProvider = "invalidPostIds", dataProviderClass = PostDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /posts/{id} returns 404 for non-existent IDs")
    public void testGetPostNotFound(int invalidId) {
        given()
            .spec(requestSpec)
        .when()
            .get(PostEndpoint.BASE + "/" + invalidId)
        .then()
            .statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /posts creates a new post and returns 201 with correct data")
    public void testCreatePost() {
        given()
            .spec(requestSpec)
            .body(PostPayload.create())
        .when()
            .post(PostEndpoint.BASE)
        .then()
            .statusCode(ApiConfig.STATUS_CREATED)
            .body("title", equalTo("Automation Test Post"))
            .body("userId", equalTo(1))
            .body("id", notNullValue());
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify PUT /posts/1 updates the post and returns 200 with updated data")
    public void testUpdatePost() {
        given()
            .spec(requestSpec)
            .body(PostPayload.update())
        .when()
            .put(PostEndpoint.BASE + "/1")
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("title", equalTo("Updated Post Title"))
            .body("id", equalTo(1));
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify PATCH /posts/1 partially updates the post")
    public void testPatchPost() {
        given()
            .spec(requestSpec)
            .body(PostPayload.patch())
        .when()
            .patch(PostEndpoint.BASE + "/1")
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("title", equalTo("Patched Title - REST Assured"));
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify DELETE /posts/1 returns 200 confirming deletion")
    public void testDeletePost() {
        given()
            .spec(requestSpec)
        .when()
            .delete(PostEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK);
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header is JSON and response time is under threshold")
    public void testPostHeaders() {
        given()
            .spec(requestSpec)
        .when()
            .get(PostEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
    }
}