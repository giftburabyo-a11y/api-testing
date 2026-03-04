package com.apitest.comments;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Feature("Comments")
public class CommentTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CommentTest.class);

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /comments returns all 500 comments")
    public void testGetAllComments() {
        log.info("Fetching all comments from {}", CommentEndpoint.BASE);
        given().spec(requestSpec)
        .when().get(CommentEndpoint.BASE)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(500))
            .body("id", everyItem(notNullValue()));
        log.info("Verified 500 comments returned");
    }

    @Test(priority = 2, dataProvider = "validCommentIds", dataProviderClass = CommentDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /comments/{id} returns correct comment data")
    public void testGetCommentById(int commentId, int expectedPostId, String expectedEmail) {
        log.info("Fetching comment ID: {} | Expected postId: {} | Expected email: {}", commentId, expectedPostId, expectedEmail);
        given().spec(requestSpec)
        .when().get(CommentEndpoint.BASE + "/" + commentId)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(commentId))
            .body("postId", equalTo(expectedPostId))
            .body("email", equalTo(expectedEmail));
        log.info("Comment ID {} verified successfully", commentId);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /comments?postId=1 filters comments by post")
    public void testFilterCommentsByPostId() {
        log.info("Filtering comments by postId=1");
        given().spec(requestSpec)
            .queryParam("postId", 1)
        .when().get(CommentEndpoint.BASE)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("postId", everyItem(equalTo(1)));
        log.info("Filter by postId=1 verified");
    }

    @Test(priority = 4, dataProvider = "invalidCommentIds", dataProviderClass = CommentDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /comments/{id} returns 404 for invalid IDs")
    public void testGetCommentNotFound(int invalidId) {
        log.warn("Testing 404 for invalid comment ID: {}", invalidId);
        given().spec(requestSpec)
        .when().get(CommentEndpoint.BASE + "/" + invalidId)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /comments creates a comment and returns 201")
    public void testCreateComment() {
        log.info("Creating new comment with payload: {}", CommentPayload.create());
        given().spec(requestSpec)
            .body(CommentPayload.create())
        .when().post(CommentEndpoint.BASE)
        .then().statusCode(ApiConfig.STATUS_CREATED)
            .body("email", equalTo("test@example.com"))
            .body("id", notNullValue());
        log.info("Comment created successfully with status 201");
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header and response time for comments")
    public void testCommentHeaders() {
        log.info("Checking headers and response time for comment ID 1");
        given().spec(requestSpec)
        .when().get(CommentEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
        log.info("Comment headers validated");
    }
}