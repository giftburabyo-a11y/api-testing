package com.apitest.photos;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Feature("Photos")
public class PhotoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(PhotoTest.class);

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /photos returns all 5000 photos")
    public void testGetAllPhotos() {
        log.info("Fetching all photos from {}", PhotoEndpoint.BASE);
        given().spec(requestSpec)
        .when().get(PhotoEndpoint.BASE)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(5000))
            .body("id", everyItem(notNullValue()));
        log.info("Verified 5000 photos returned");
    }

    @Test(priority = 2, dataProvider = "validPhotoIds", dataProviderClass = PhotoDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /photos/{id} returns correct photo data")
    public void testGetPhotoById(int photoId, int expectedAlbumId, String expectedTitle) {
        log.info("Fetching photo ID: {} | Expected albumId: {} | Expected title: {}", photoId, expectedAlbumId, expectedTitle);
        given().spec(requestSpec)
        .when().get(PhotoEndpoint.BASE + "/" + photoId)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(photoId))
            .body("albumId", equalTo(expectedAlbumId))
            .body("title", equalTo(expectedTitle));
        log.info("Photo ID {} verified successfully", photoId);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /photos?albumId=1 filters photos by album")
    public void testFilterPhotosByAlbumId() {
        log.info("Filtering photos by albumId=1");
        given().spec(requestSpec)
            .queryParam("albumId", 1)
        .when().get(PhotoEndpoint.BASE)
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("albumId", everyItem(equalTo(1)));
        log.info("Filter by albumId=1 verified");
    }

    @Test(priority = 4, dataProvider = "invalidPhotoIds", dataProviderClass = PhotoDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /photos/{id} returns 404 for invalid IDs")
    public void testGetPhotoNotFound(int invalidId) {
        log.warn("Testing 404 for invalid photo ID: {}", invalidId);
        given().spec(requestSpec)
        .when().get(PhotoEndpoint.BASE + "/" + invalidId)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /photos creates a photo and returns 201")
    public void testCreatePhoto() {
        log.info("Creating new photo with payload: {}", PhotoPayload.create());
        given().spec(requestSpec)
            .body(PhotoPayload.create())
        .when().post(PhotoEndpoint.BASE)
        .then().statusCode(ApiConfig.STATUS_CREATED)
            .body("title", equalTo("Test Photo Title"))
            .body("id", notNullValue());
        log.info("Photo created successfully with status 201");
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify photo URLs are non-null and non-empty")
    public void testPhotoUrlFormat() {
        log.info("Verifying URL fields for photo ID 1");
        given().spec(requestSpec)
        .when().get(PhotoEndpoint.BASE + "/1")
        .then().spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("url", notNullValue())
            .body("thumbnailUrl", notNullValue());
        log.info("Photo URL fields verified");
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header and response time for photos")
    public void testPhotoHeaders() {
        log.info("Checking headers and response time for photo ID 1");
        given().spec(requestSpec)
        .when().get(PhotoEndpoint.BASE + "/1")
        .then().statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
        log.info("Photo headers validated");
    }
}