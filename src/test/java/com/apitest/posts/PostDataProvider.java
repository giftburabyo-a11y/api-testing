package com.apitest.posts;

import org.testng.annotations.DataProvider;

public class PostDataProvider {

    @DataProvider(name = "validPostIds")
    public static Object[][] validPostIds() {
        return new Object[][] {
            {1, 1, "sunt aut facere"},
            {2, 1, "qui est esse"},
            {3, 1, "ea molestias quasi"}
        };
    }

    @DataProvider(name = "invalidPostIds")
    public static Object[][] invalidPostIds() {
        return new Object[][] {
            {999},
            {0},
            {-1}
        };
    }
}