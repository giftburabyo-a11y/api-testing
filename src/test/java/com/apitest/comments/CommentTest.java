package com.apitest.comments;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Feature("Comments")
public class CommentTest extends BaseTest {

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /comments returns all 500 comments with valid fields")
    public void testGetAllComments() {
        given()
            .spec(requestSpec)
        .when()
            .get(CommentEndpoint.BASE)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(500))
            .body("id", everyItem(notNullValue()))
            .body("email", everyItem(containsString("@")));
    }

    @Test(priority = 2, dataProvider = "validCommentIds", dataProviderClass = CommentDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /comments/{id} returns correct comment for valid IDs")
    public void testGetCommentById(int commentId, int expectedPostId, String expectedName) {
        given()
            .spec(requestSpec)
        .when()
            .get(CommentEndpoint.BASE + "/" + commentId)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(commentId))
            .body("postId", equalTo(expectedPostId))
            .body("name", equalTo(expectedName));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /comments?postId=1 returns only comments for that post")
    public void testFilterCommentsByPostId() {
        given()
            .spec(requestSpec)
            .queryParam("postId", 1)
        .when()
            .get(CommentEndpoint.BASE)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("postId", everyItem(equalTo(1)));
    }

    @Test(priority = 4, dataProvider = "invalidCommentIds", dataProviderClass = CommentDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /comments/{id} returns 404 for non-existent IDs")
    public void testGetCommentNotFound(int invalidId) {
        given()
            .spec(requestSpec)
        .when()
            .get(CommentEndpoint.BASE + "/" + invalidId)
        .then()
            .statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /comments creates a new comment and returns 201")
    public void testCreateComment() {
        given()
            .spec(requestSpec)
            .body(CommentPayload.create())
        .when()
            .post(CommentEndpoint.BASE)
        .then()
            .statusCode(ApiConfig.STATUS_CREATED)
            .body("postId", equalTo(1))
            .body("email", equalTo("test@automation.com"))
            .body("id", notNullValue());
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header and response time are within expected bounds")
    public void testCommentHeaders() {
        given()
            .spec(requestSpec)
        .when()
            .get(CommentEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
    }
}