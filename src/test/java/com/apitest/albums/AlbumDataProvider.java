package com.apitest.albums;

import org.testng.annotations.DataProvider;

public class AlbumDataProvider {

    @DataProvider(name = "validAlbumIds")
    public static Object[][] validAlbumIds() {
        return new Object[][] {
            {1, 1, "quidem molestiae enim"},
            {2, 1, "sunt qui excepturi placeat culpa"},
            {3, 1, "omnis laborum odio"}
        };
    }

    @DataProvider(name = "invalidAlbumIds")
    public static Object[][] invalidAlbumIds() {
        return new Object[][] {
            {999},
            {0},
            {-1}
        };
    }
}