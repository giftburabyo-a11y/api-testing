package com.apitest.tests;

import com.apitest.config.ApiConfig;
import com.apitest.utils.RequestBuilder;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("Negative & Edge Case Tests")
public class NegativeTests {

    // ✅ This will PASS - correct expectation
    @Test(priority = 1)
    @Story("Invalid Post ID") @Severity(SeverityLevel.NORMAL)
    public void testGetInvalidPostReturns404() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT + "/99999")
                .then().statusCode(404);
    }

    // ❌ This will FAIL - we expect 200 but will get 404
    @Test(priority = 2)
    @Story("Wrong Status Code Expectation") @Severity(SeverityLevel.CRITICAL)
    public void testWrongStatusCodeExpectation() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT + "/99999")
                .then().statusCode(200); // INTENTIONAL FAIL — this returns 404
    }

    // ❌ This will FAIL - wrong field value expected
    @Test(priority = 3)
    @Story("Wrong Field Value") @Severity(SeverityLevel.NORMAL)
    public void testWrongFieldValueExpectation() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT + "/1")
                .then()
                .statusCode(200)
                .body("userId", equalTo(99)); // INTENTIONAL FAIL — actual userId is 1
    }

    // ❌ This will FAIL - we expect a field that doesn't exist
    @Test(priority = 4)
    @Story("Non-Existent Field") @Severity(SeverityLevel.MINOR)
    public void testNonExistentFieldExpectation() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT + "/1")
                .then()
                .statusCode(200)
                .body("author", equalTo("John")); // INTENTIONAL FAIL — field "author" doesn't exist
    }

    // ❌ This will FAIL - wrong response time threshold (1ms is impossible)
    @Test(priority = 5)
    @Story("Impossible Response Time") @Severity(SeverityLevel.MINOR)
    public void testImpossibleResponseTime() {
        given().spec(RequestBuilder.getRequestSpec())
                .when().get(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(200)
                .time(lessThan(1L)); // INTENTIONAL FAIL — no API responds in under 1ms
    }

    // ✅ This will PASS - empty object body on DELETE
    @Test(priority = 6)
    @Story("Delete Response Is Empty") @Severity(SeverityLevel.NORMAL)
    public void testDeleteResponseIsEmpty() {
        given().spec(RequestBuilder.getRequestSpec())
                .pathParam("id", 1)
                .when().delete(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(200)
                .body("id", not(greaterThan(0))); // PASSES — deleted response has no id
    }
}