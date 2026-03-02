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
@Feature("PUT Requests")
public class PutRequestTests {

    @Test(priority = 1)
    @Story("Full Update Post") @Severity(SeverityLevel.BLOCKER)
    public void testUpdatePost() {
        Post updatedPost = TestDataGenerator.createUpdatedPost();
        given().spec(RequestBuilder.getRequestSpec())
                .pathParam("id", 1).body(updatedPost)
                .when().put(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body("id", equalTo(1))
                .body("title", equalTo(updatedPost.getTitle()));
    }

    @Test(priority = 2)
    @Story("Validate Updated Title") @Severity(SeverityLevel.CRITICAL)
    public void testUpdatePostTitle() {
        Post updatedPost = new Post(1, "Specifically Updated Title", "Updated body");
        updatedPost.setId(1);
        Response response = given().spec(RequestBuilder.getRequestSpec())
                .pathParam("id", 1).body(updatedPost)
                .when().put(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then().statusCode(ApiConfig.STATUS_OK).extract().response();

        assertEquals(response.jsonPath().getString("title"), "Specifically Updated Title");
    }

    @Test(priority = 3)
    @Story("PUT Response Headers") @Severity(SeverityLevel.NORMAL)
    public void testUpdatePostResponseHeaders() {
        given().spec(RequestBuilder.getRequestSpec())
                .pathParam("id", 1).body(TestDataGenerator.createUpdatedPost())
                .when().put(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .header("Content-Type", containsString("application/json"));
    }

    @Test(priority = 4)
    @Story("Partial Update PATCH") @Severity(SeverityLevel.CRITICAL)
    public void testPatchPost() {
        given().spec(RequestBuilder.getRequestSpec())
                .pathParam("id", 1).body(TestDataGenerator.createPartialUpdateData())
                .when().patch(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .body("id", equalTo(1))
                .body("title", equalTo("Patched Title - REST Assured"));
    }

    @Test(priority = 5)
    @Story("PUT Performance") @Severity(SeverityLevel.MINOR)
    public void testUpdatePostResponseTime() {
        given().spec(RequestBuilder.getRequestSpec())
                .pathParam("id", 1).body(TestDataGenerator.createUpdatedPost())
                .when().put(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(ApiConfig.STATUS_OK)
                .time(lessThan(5000L));
    }
}