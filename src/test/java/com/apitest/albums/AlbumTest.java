package com.apitest.albums;

import com.apitest.config.ApiConfig;
import com.apitest.utils.RequestBuilder;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("Albums")
public class AlbumTest {

    @Test(priority = 1)
    @Story("Get All Albums") @Severity(SeverityLevel.BLOCKER)
    public void testGetAllAlbums() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(AlbumEndpoint.ALBUMS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(100))
            .body("[0].userId", notNullValue())
            .body("[0].title", notNullValue());
    }

    @Test(priority = 2)
    @Story("Get Album By ID") @Severity(SeverityLevel.CRITICAL)
    public void testGetAlbumById() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", AlbumDataProvider.getValidAlbumId())
        .when().get(AlbumEndpoint.ALBUM_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", notNullValue());
    }

    @Test(priority = 3)
    @Story("Get Album Photos") @Severity(SeverityLevel.NORMAL)
    public void testGetAlbumPhotos() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", AlbumDataProvider.getValidAlbumId())
        .when().get(AlbumEndpoint.ALBUM_PHOTOS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("[0].albumId", equalTo(1))
            .body("[0].url", notNullValue());
    }

    @Test(priority = 4)
    @Story("Create Album") @Severity(SeverityLevel.CRITICAL)
    public void testCreateAlbum() {
        AlbumPayload newAlbum = AlbumDataProvider.createAlbum();
        given().spec(RequestBuilder.getRequestSpec())
            .body(newAlbum)
        .when().post(AlbumEndpoint.ALBUMS)
        .then()
            .statusCode(ApiConfig.STATUS_CREATED)
            .body("id", notNullValue())
            .body("title", equalTo(newAlbum.getTitle()));
    }

    @Test(priority = 5)
    @Story("Filter Albums By UserId") @Severity(SeverityLevel.NORMAL)
    public void testFilterAlbumsByUserId() {
        given().spec(RequestBuilder.getRequestSpec())
            .queryParam("userId", 1)
        .when().get(AlbumEndpoint.ALBUMS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("userId", everyItem(equalTo(1)));
    }

    @Test(priority = 6)
    @Story("Non Existent Album") @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentAlbum() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", AlbumDataProvider.getInvalidAlbumId())
        .when().get(AlbumEndpoint.ALBUM_BY_ID)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }
}
