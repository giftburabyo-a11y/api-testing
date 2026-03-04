package com.apitest.posts;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Feature("Posts")
public class PostTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(PostTest.class);

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /posts returns all 100 posts")
    public void testGetAllPosts() {
        log.info("Fetching all posts from {}", PostEndpoint.BASE);
        given().spec(requestSpec)
        .when().get(PostEndpoint.BASE)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(100))
            .body("id", everyItem(notNullValue()));
        log.info("Verified 100 posts returned");
    }

    @Test(priority = 2, dataProvider = "validPostIds", dataProviderClass = PostDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /posts/{id} returns correct post data")
    public void testGetPostById(int postId, int expectedUserId, String expectedTitle) {
        log.info("Fetching post ID: {} | Expected userId: {} | Expected title: {}", postId, expectedUserId, expectedTitle);
        given().spec(requestSpec)
        .when().get(PostEndpoint.BASE + "/" + postId)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(postId))
            .body("userId", equalTo(expectedUserId))
            .body("title", equalTo(expectedTitle));
        log.info("Post ID {} verified successfully", postId);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify response structure matches post JSON schema")
    public void testPostSchemaValidation() {
        log.info("Validating post schema for post ID 1");
        given().spec(requestSpec)
        .when().get(PostEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK)
            .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
        log.info("Schema validation passed");
    }

    @Test(priority = 4, dataProvider = "invalidPostIds", dataProviderClass = PostDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /posts/{id} returns 404 for non-existent IDs")
    public void testGetPostNotFound(int invalidId) {
        log.info("Testing 404 for invalid post ID: {}", invalidId);
        given().spec(requestSpec)
        .when().get(PostEndpoint.BASE + "/" + invalidId)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
        log.warn("Post ID {} correctly returned 404", invalidId);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /posts creates a new post and returns 201")
    public void testCreatePost() {
        log.info("Creating new post with payload: {}", PostPayload.create());
        given().spec(requestSpec)
            .body(PostPayload.create())
        .when().post(PostEndpoint.BASE)
        .then().statusCode(ApiConfig.STATUS_CREATED)
            .body("title", equalTo("Test Post Title"))
            .body("userId", equalTo(1))
            .body("id", notNullValue());
        log.info("Post created successfully with status 201");
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify PUT /posts/1 fully updates the post")
    public void testUpdatePost() {
        log.info("Updating post ID 1 with full payload");
        given().spec(requestSpec)
            .body(PostPayload.update())
        .when().put(PostEndpoint.BASE + "/1")
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("title", equalTo("Updated Post Title"))
            .body("id", equalTo(1));
        log.info("Post ID 1 updated successfully");
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify PATCH /posts/1 partially updates the post")
    public void testPatchPost() {
        log.info("Patching post ID 1 with partial payload");
        given().spec(requestSpec)
            .body(PostPayload.patch())
        .when().patch(PostEndpoint.BASE + "/1")
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("title", equalTo("Patched Post Title"));
        log.info("Post ID 1 patched successfully");
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify DELETE /posts/1 returns 200")
    public void testDeletePost() {
        log.info("Deleting post ID 1");
        given().spec(requestSpec)
        .when().delete(PostEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK);
        log.info("Post ID 1 deleted successfully");
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header and response time")
    public void testPostHeaders() {
        log.info("Checking headers and response time for post ID 1");
        given().spec(requestSpec)
        .when().get(PostEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
        log.info("Headers and response time validated");
    }
}