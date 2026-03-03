package com.apitest.photos;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Feature("Photos")
public class PhotoTest extends BaseTest {

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /photos returns all 5000 photos with valid fields")
    public void testGetAllPhotos() {
        given()
            .spec(requestSpec)
        .when()
            .get(PhotoEndpoint.BASE)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(5000))
            .body("id", everyItem(notNullValue()))
            .body("url", everyItem(notNullValue()));
    }

    @Test(priority = 2, dataProvider = "validPhotoIds", dataProviderClass = PhotoDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /photos/{id} returns correct photo data for valid IDs")
    public void testGetPhotoById(int photoId, int expectedAlbumId, String expectedTitle) {
        given()
            .spec(requestSpec)
        .when()
            .get(PhotoEndpoint.BASE + "/" + photoId)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(photoId))
            .body("albumId", equalTo(expectedAlbumId))
            .body("title", equalTo(expectedTitle));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /photos?albumId=1 returns only photos for that album")
    public void testFilterPhotosByAlbumId() {
        given()
            .spec(requestSpec)
            .queryParam("albumId", 1)
        .when()
            .get(PhotoEndpoint.BASE)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("albumId", everyItem(equalTo(1)));
    }

    @Test(priority = 4, dataProvider = "invalidPhotoIds", dataProviderClass = PhotoDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /photos/{id} returns 404 for non-existent IDs")
    public void testGetPhotoNotFound(int invalidId) {
        given()
            .spec(requestSpec)
        .when()
            .get(PhotoEndpoint.BASE + "/" + invalidId)
        .then()
            .statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /photos creates a new photo and returns 201")
    public void testCreatePhoto() {
        given()
            .spec(requestSpec)
            .body(PhotoPayload.create())
        .when()
            .post(PhotoEndpoint.BASE)
        .then()
            .statusCode(ApiConfig.STATUS_CREATED)
            .body("albumId", equalTo(1))
            .body("title", equalTo("Test Photo Title"))
            .body("id", notNullValue());
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify photo URL fields contain valid https links")
    public void testPhotoUrlFormat() {
        given()
            .spec(requestSpec)
        .when()
            .get(PhotoEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("url", containsString("https://"))
            .body("thumbnailUrl", containsString("https://"));
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header and response time are within expected bounds")
    public void testPhotoHeaders() {
        given()
            .spec(requestSpec)
        .when()
            .get(PhotoEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
    }
}