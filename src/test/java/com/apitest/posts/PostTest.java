package com.apitest.posts;

import com.apitest.config.ApiConfig;
import com.apitest.utils.RequestBuilder;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

@Epic("JSONPlaceholder API Tests")
@Feature("Posts")
public class PostTest {

    @Test(priority = 1)
    @Story("Get All Posts") @Severity(SeverityLevel.BLOCKER)
    public void testGetAllPosts() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(PostEndpoint.POSTS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(100))
            .body("[0].userId", notNullValue())
            .body("[0].title", notNullValue())
            .body("[0].body", notNullValue());
    }

    @Test(priority = 2)
    @Story("Get Post By ID") @Severity(SeverityLevel.CRITICAL)
    public void testGetPostById() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", 1)
        .when().get(PostEndpoint.POST_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", notNullValue());
    }

    @Test(priority = 3)
    @Story("JSON Schema Validation") @Severity(SeverityLevel.CRITICAL)
    public void testPostJsonSchema() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", 1)
        .when().get(PostEndpoint.POST_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test(priority = 4)
    @Story("Filter Posts By UserId") @Severity(SeverityLevel.NORMAL)
    public void testFilterPostsByUserId() {
        given().spec(RequestBuilder.getRequestSpec())
            .queryParam("userId", 1)
        .when().get(PostEndpoint.POSTS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
    }

    @Test(priority = 5)
    @Story("Create Post") @Severity(SeverityLevel.BLOCKER)
    public void testCreatePost() {
        PostPayload newPost = PostDataProvider.createPost();
        given().spec(RequestBuilder.getRequestSpec())
            .body(newPost)
        .when().post(PostEndpoint.POSTS)
        .then()
            .statusCode(ApiConfig.STATUS_CREATED)
            .body("id", notNullValue())
            .body("title", equalTo(newPost.getTitle()))
            .body("userId", equalTo(newPost.getUserId()));
    }

    @Test(priority = 6)
    @Story("Update Post") @Severity(SeverityLevel.CRITICAL)
    public void testUpdatePost() {
        PostPayload updatedPost = PostDataProvider.updatePost();
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", 1)
            .body(updatedPost)
        .when().put(PostEndpoint.POST_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(1))
            .body("title", equalTo(updatedPost.getTitle()));
    }

    @Test(priority = 7)
    @Story("Patch Post") @Severity(SeverityLevel.NORMAL)
    public void testPatchPost() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", 1)
            .body(PostDataProvider.patchPost())
        .when().patch(PostEndpoint.POST_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(1))
            .body("title", equalTo("Patched Title - REST Assured"));
    }

    @Test(priority = 8)
    @Story("Delete Post") @Severity(SeverityLevel.BLOCKER)
    public void testDeletePost() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", 1)
        .when().delete(PostEndpoint.POST_BY_ID)
        .then().statusCode(ApiConfig.STATUS_OK);
    }

    @Test(priority = 9)
    @Story("Post Comments") @Severity(SeverityLevel.NORMAL)
    public void testGetPostComments() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", 1)
        .when().get(PostEndpoint.POST_COMMENTS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("[0].postId", equalTo(1))
            .body("[0].email", notNullValue());
    }

    @Test(priority = 10)
    @Story("Non Existent Post") @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentPost() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", 99999)
        .when().get(PostEndpoint.POST_BY_ID)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }
}
