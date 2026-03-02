package com.apitest.tests;

import com.apitest.config.ApiConfig;
import com.apitest.utils.RequestBuilder;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

@Epic("JSONPlaceholder API Tests")
@Feature("DELETE Requests")
public class DeleteRequestTests {

    @Test(priority = 1)
    @Story("Delete Post") @Severity(SeverityLevel.BLOCKER)
    public void testDeletePost() {
        given().spec(RequestBuilder.getRequestSpec()).pathParam("id", 1)
                .when().delete(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then().statusCode(ApiConfig.STATUS_OK);
    }

    @Test(priority = 2)
    @Story("Delete Returns Empty Body") @Severity(SeverityLevel.NORMAL)
    public void testDeletePostResponseBody() {
        Response response = given().spec(RequestBuilder.getRequestSpec())
                .pathParam("id", 1)
                .when().delete(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then().statusCode(ApiConfig.STATUS_OK).extract().response();

        String body = response.getBody().asString().trim();
        assertTrue(body.equals("{}") || body.isEmpty());
    }

    @Test(priority = 3)
    @Story("Delete Multiple Posts") @Severity(SeverityLevel.NORMAL)
    public void testDeleteMultiplePosts() {
        for (int postId : new int[]{1, 5, 10}) {
            given().spec(RequestBuilder.getRequestSpec()).pathParam("id", postId)
                    .when().delete(ApiConfig.POSTS_ENDPOINT + "/{id}")
                    .then().statusCode(ApiConfig.STATUS_OK);
        }
    }

    @Test(priority = 4)
    @Story("DELETE Performance") @Severity(SeverityLevel.MINOR)
    public void testDeletePostResponseTime() {
        given().spec(RequestBuilder.getRequestSpec()).pathParam("id", 1)
                .when().delete(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then().statusCode(ApiConfig.STATUS_OK).time(lessThan(5000L));
    }

    @Test(priority = 5)
    @Story("DELETE Response Headers") @Severity(SeverityLevel.MINOR)
    public void testDeletePostResponseHeaders() {
        given().spec(RequestBuilder.getRequestSpec()).pathParam("id", 1)
                .when().delete(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then().statusCode(ApiConfig.STATUS_OK).header("Content-Type", notNullValue());
    }
}