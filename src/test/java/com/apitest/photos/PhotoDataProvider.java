package com.apitest.photos;

import org.testng.annotations.DataProvider;

public class PhotoDataProvider {

    @DataProvider(name = "validPhotoIds")
    public static Object[][] validPhotoIds() {
        return new Object[][] {
            {1, 1, "accusamus beatae ad facilis cum similique qui sunt"},
            {2, 1, "reprehenderit est deserunt velit ipsam"},
            {3, 1, "officia porro iure quia iusto qui ipsa ut modi"}
        };
    }

    @DataProvider(name = "invalidPhotoIds")
    public static Object[][] invalidPhotoIds() {
        return new Object[][] {
            {999999},
            {0},
            {-1}
        };
    }
}