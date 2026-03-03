package com.apitest.albums;

import com.apitest.base.BaseTest;
import com.apitest.config.ApiConfig;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Feature("Albums")
public class AlbumTest extends BaseTest {

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /albums returns all 100 albums with valid ids and titles")
    public void testGetAllAlbums() {
        given()
            .spec(requestSpec)
        .when()
            .get(AlbumEndpoint.BASE)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(100))
            .body("id", everyItem(notNullValue()));
    }

    @Test(priority = 2, dataProvider = "validAlbumIds", dataProviderClass = AlbumDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /albums/{id} returns correct album data for valid IDs")
    public void testGetAlbumById(int albumId, int expectedUserId, String expectedTitle) {
        given()
            .spec(requestSpec)
        .when()
            .get(AlbumEndpoint.BASE + "/" + albumId)
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(albumId))
            .body("userId", equalTo(expectedUserId))
            .body("title", equalTo(expectedTitle));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify relationship: albums have many photos, all linked by albumId")
    public void testGetAlbumPhotos() {
        given()
            .spec(requestSpec)
        .when()
            .get(AlbumEndpoint.BASE + "/1/photos")
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("albumId", everyItem(equalTo(1)));
    }

    @Test(priority = 4, dataProvider = "invalidAlbumIds", dataProviderClass = AlbumDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /albums/{id} returns 404 for non-existent IDs")
    public void testGetAlbumNotFound(int invalidId) {
        given()
            .spec(requestSpec)
        .when()
            .get(AlbumEndpoint.BASE + "/" + invalidId)
        .then()
            .statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify response structure matches album JSON schema contract")
    public void testAlbumSchemaValidation() {
        given()
            .spec(requestSpec)
        .when()
            .get(AlbumEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body(matchesJsonSchemaInClasspath("schemas/album-schema.json"));
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /albums creates a new album and returns 201 with correct data")
    public void testCreateAlbum() {
        given()
            .spec(requestSpec)
            .body(AlbumPayload.create())
        .when()
            .post(AlbumEndpoint.BASE)
        .then()
            .statusCode(ApiConfig.STATUS_CREATED)
            .body("title", equalTo("Test Album Title"))
            .body("userId", equalTo(1))
            .body("id", notNullValue());
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify PUT /albums/1 updates the album and returns 200 with updated data")
    public void testUpdateAlbum() {
        given()
            .spec(requestSpec)
            .body(AlbumPayload.update())
        .when()
            .put(AlbumEndpoint.BASE + "/1")
        .then()
            .spec(responseSpec)
            .statusCode(ApiConfig.STATUS_OK)
            .body("title", equalTo("Updated Album Title"))
            .body("id", equalTo(1));
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify DELETE /albums/1 returns 200 confirming deletion")
    public void testDeleteAlbum() {
        given()
            .spec(requestSpec)
        .when()
            .delete(AlbumEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK);
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Content-Type header is JSON and response time is under threshold")
    public void testAlbumHeaders() {
        given()
            .spec(requestSpec)
        .when()
            .get(AlbumEndpoint.BASE + "/1")
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .header("Content-Type", containsString("application/json"))
            .time(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS));
    }
}