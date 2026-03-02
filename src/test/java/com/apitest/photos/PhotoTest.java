package com.apitest.photos;

import com.apitest.config.ApiConfig;
import com.apitest.utils.RequestBuilder;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("Photos")
public class PhotoTest {

    @Test(priority = 1)
    @Story("Get All Photos") @Severity(SeverityLevel.BLOCKER)
    public void testGetAllPhotos() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(PhotoEndpoint.PHOTOS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(5000))
            .body("[0].albumId", notNullValue())
            .body("[0].title", notNullValue())
            .body("[0].url", notNullValue())
            .body("[0].thumbnailUrl", notNullValue());
    }

    @Test(priority = 2)
    @Story("Get Photo By ID") @Severity(SeverityLevel.CRITICAL)
    public void testGetPhotoById() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", PhotoDataProvider.getValidPhotoId())
        .when().get(PhotoEndpoint.PHOTO_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("id", equalTo(1))
            .body("albumId", equalTo(1))
            .body("title", notNullValue())
            .body("url", notNullValue())
            .body("thumbnailUrl", notNullValue());
    }

    @Test(priority = 3)
    @Story("Filter Photos By AlbumId") @Severity(SeverityLevel.NORMAL)
    public void testFilterPhotosByAlbumId() {
        given().spec(RequestBuilder.getRequestSpec())
            .queryParam("albumId", PhotoDataProvider.getValidAlbumId())
        .when().get(PhotoEndpoint.PHOTOS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("$", hasSize(greaterThan(0)))
            .body("albumId", everyItem(equalTo(1)));
    }

    @Test(priority = 4)
    @Story("Photo URL Format") @Severity(SeverityLevel.NORMAL)
    public void testPhotoUrlFormat() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", PhotoDataProvider.getValidPhotoId())
        .when().get(PhotoEndpoint.PHOTO_BY_ID)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .body("url", containsString("https://"))
            .body("thumbnailUrl", containsString("https://"));
    }

    @Test(priority = 5)
    @Story("Non Existent Photo") @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentPhoto() {
        given().spec(RequestBuilder.getRequestSpec())
            .pathParam("id", PhotoDataProvider.getInvalidPhotoId())
        .when().get(PhotoEndpoint.PHOTO_BY_ID)
        .then().statusCode(ApiConfig.STATUS_NOT_FOUND);
    }

    @Test(priority = 6)
    @Story("Response Time") @Severity(SeverityLevel.MINOR)
    public void testPhotosResponseTime() {
        given().spec(RequestBuilder.getRequestSpec())
        .when().get(PhotoEndpoint.PHOTOS)
        .then()
            .statusCode(ApiConfig.STATUS_OK)
            .time(lessThan(5000L));
    }
}
