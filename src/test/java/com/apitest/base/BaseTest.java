package com.apitest.base;

import com.apitest.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public class BaseTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite
    public void setUp() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ApiConfig.BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(buildLoggingFilter())
                .addFilter(new AllureRestAssured())
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();

        log.info("===========================================");
        log.info("  REST Assured API Test Suite Starting");
        log.info("  Base URL: {}", ApiConfig.BASE_URL);
        log.info("===========================================");
    }

    @BeforeMethod
    public void beforeTest(Method method) {
        log.info("--------------------------------------------");
        log.info("TEST START: {}::{}", method.getDeclaringClass().getSimpleName(), method.getName());
    }

    @AfterMethod
    public void afterTest(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        String name = result.getMethod().getMethodName();
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                log.info("TEST PASS : {} ({}ms)", name, duration);
                break;
            case ITestResult.FAILURE:
                log.error("TEST FAIL : {} ({}ms)", name, duration);
                if (result.getThrowable() != null) {
                    log.error("REASON    : {}", result.getThrowable().getMessage());
                }
                break;
            case ITestResult.SKIP:
                log.warn("TEST SKIP : {}", name);
                break;
        }
    }

    @AfterSuite
    public void tearDown() {
        RestAssured.reset();
        log.info("===========================================");
        log.info("  REST Assured API Test Suite Completed");
        log.info("===========================================");
    }

    private Filter buildLoggingFilter() {
        return (requestSpec, responseSpec, ctx) -> {
            log.debug("REQUEST  --> {} {}", requestSpec.getMethod(), requestSpec.getURI());
            long start = System.currentTimeMillis();
            Response response = ctx.next(requestSpec, responseSpec);
            long duration = System.currentTimeMillis() - start;
            log.debug("RESPONSE <-- Status: {} | Time: {}ms", response.statusCode(), duration);
            return response;
        };
    }
}