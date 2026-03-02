package com.apitest.comments;

import com.apitest.config.ApiConfig;
import com.apitest.utils.RequestBuilder;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("Comments")
public class CommentTest {

    @Test(priority = 1)
    @Story("Get All Comments") @Severity(SeverityLevel.BLOCKER)
    public void testGetAllComments() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(CommentEndpoint.COMMENTS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(500))
            .body("[0].postId", notNullValue())
            .body("[0].email", notNullValue())
            .body("[0].body", notNullValue());
    }

    @Test(priority = 2)
    @Story("Get Comment By ID") @Severity(SeverityLevel.CRITICAL)
    public void testGetCommentById() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", CommentDataProvider.getValidCommentId())
        .when().get(CommentEndpoint.COMMENT_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(1))
            .body("postId", notNullValue())
            .body("email", containsString("@"));
    }

    @Test(priority = 3)
    @Story("Filter Comments By PostId") @Severity(SeverityLevel.NORMAL)
    public void testFilterCommentsByPostId() {
        given().spec(RequestBuilder.getRequestSpec())
            .queryParam("postId", CommentDataProvider.getValidPostId())
        .when().get(CommentEndpoint.COMMENTS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("postId", everyItem(equalTo(1)));
    }

    @Test(priority = 4)
    @Story("Comment Email Format") @Severity(SeverityLevel.NORMAL)
    public void testCommentEmailFormat() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(CommentEndpoint.COMMENTS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("email", everyItem(containsString("@")));
    }

    @Test(priority = 5)
    @Story("Non Existent Comment") @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentComment() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", CommentDataProvider.getInvalidCommentId())
        .when().get(CommentEndpoint.COMMENT_BY_ID)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 6)
    @Story("Response Time") @Severity(SeverityLevel.MINOR)
    public void testCommentsResponseTime() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(CommentEndpoint.COMMENTS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .time(lessThan(5000L));
    }
}
