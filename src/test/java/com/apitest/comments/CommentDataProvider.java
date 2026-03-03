package com.apitest.comments;

import org.testng.annotations.DataProvider;

public class CommentDataProvider {

    @DataProvider(name = "validCommentIds")
    public static Object[][] validCommentIds() {
        return new Object[][] {
            {1, 1, "id labore ex et quam laborum"},
            {2, 1, "quo vero reiciendis velit similique earum"},
            {3, 1, "odio adipisci rerum aut animi"}
        };
    }

    @DataProvider(name = "invalidCommentIds")
    public static Object[][] invalidCommentIds() {
        return new Object[][] {
            {999},
            {0},
            {-1}
        };
    }
}