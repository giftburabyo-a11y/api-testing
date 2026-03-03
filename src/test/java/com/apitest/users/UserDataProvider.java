package com.apitest.users;

import org.testng.annotations.DataProvider;

public class UserDataProvider {

    @DataProvider(name = "validUserIds")
    public static Object[][] validUserIds() {
        return new Object[][] {
            {1, "Leanne Graham", "Bret"},
            {2, "Ervin Howell", "Antonette"},
            {3, "Clementine Bauch", "Samantha"}
        };
    }

    @DataProvider(name = "invalidUserIds")
    public static Object[][] invalidUserIds() {
        return new Object[][] {
            {999},
            {0},
            {-1}
        };
    }
}