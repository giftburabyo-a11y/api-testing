package com.apitest.base;

import com.apitest.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;

    @BeforeSuite
    public void setUp() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ApiConfig.BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new AllureRestAssured())
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();

        System.out.println("===========================================");
        System.out.println("  REST Assured API Test Suite Starting");
        System.out.println("  Base URL: " + ApiConfig.BASE_URL);
        System.out.println("===========================================");
    }

    @AfterSuite
    public void tearDown() {
        RestAssured.reset();
        System.out.println("===========================================");
        System.out.println("  REST Assured API Test Suite Completed");
        System.out.println("===========================================");
    }
}