package com.apitest.albums;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Feature("Albums")
public class AlbumTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(AlbumTest.class);

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /albums returns all 100 albums")
    public void testGetAllAlbums() {
        log.info("Fetching all albums from {}", AlbumEndpoint.BASE);
        given().spec(requestSpec)
        .when().get(AlbumEndpoint.BASE)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(100))
            .body("id", everyItem(notNullValue()));
        log.info("Verified 100 albums returned");
    }

    @Test(priority = 2, dataProvider = "validAlbumIds", dataProviderClass = AlbumDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /albums/{id} returns correct album data")
    public void testGetAlbumById(int albumId, int expectedUserId, String expectedTitle) {
        log.info("Fetching album ID: {} | Expected userId: {} | Expected title: {}", albumId, expectedUserId, expectedTitle);
        given().spec(requestSpec)
        .when().get(AlbumEndpoint.BASE + "/" + albumId)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(albumId))
            .body("userId", equalTo(expectedUserId))
            .body("title", equalTo(expectedTitle));
        log.info("Album ID {} verified successfully", albumId);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify relationship: album has many photos linked by albumId")
    public void testGetAlbumPhotos() {
        log.info("Fetching photos for album ID 1");
        given().spec(requestSpec)
        .when().get(AlbumEndpoint.BASE + "/1/photos")
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("albumId", everyItem(equalTo(1)));
        log.info("Album photos relationship verified");
    }

    @Test(priority = 4, dataProvider = "invalidAlbumIds", dataProviderClass = AlbumDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /albums/{id} returns 404 for non-existent IDs")
    public void testGetAlbumNotFound(int invalidId) {
        log.warn("Testing 404 for invalid album ID: {}", invalidId);
        given().spec(requestSpec)
        .when().get(AlbumEndpoint.BASE + "/" + invalidId)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify response structure matches album JSON schema")
    public void testAlbumSchemaValidation() {
        log.info("Validating album schema for album ID 1");
        given().spec(requestSpec)
        .when().get(AlbumEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK)
            .body(matchesJsonSchemaInClasspath("schemas/album-schema.json"));
        log.info("Album schema validation passed");
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /albums creates a new album and returns 201")
    public void testCreateAlbum() {
        log.info("Creating new album with payload: {}", AlbumPayload.create());
        given().spec(requestSpec)
            .body(AlbumPayload.create())
        .when().post(AlbumEndpoint.BASE)
        .then().statusCode(ApiConfig.STATUS_CREATED)
            .body("title", equalTo("Test Album Title"))
            .body("userId", equalTo(1))
            .body("id", notNullValue());
        log.info("Album created successfully with status 201");
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify PUT /albums/1 updates the album and returns 200")
    public void testUpdateAlbum() {
        log.info("Updating album ID 1 with full payload");
        given().spec(requestSpec)
            .body(AlbumPayload.update())
        .when().put(AlbumEndpoint.BASE + "/1")
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("title", equalTo("Updated Album Title"))
            .body("id", equalTo(1));
        log.info("Album ID 1 updated successfully");
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify DELETE /albums/1 returns 200")
    public void testDeleteAlbum() {
        log.info("Deleting album ID 1");
        given().spec(requestSpec)
        .when().delete(AlbumEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK);
        log.info("Album ID 1 deleted successfully");
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header and response time")
    public void testAlbumHeaders() {
        log.info("Checking headers and response time for album ID 1");
        given().spec(requestSpec)
        .when().get(AlbumEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
        log.info("Album headers validated");
    }
}