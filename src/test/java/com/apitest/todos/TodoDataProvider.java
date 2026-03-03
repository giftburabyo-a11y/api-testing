package com.apitest.todos;

import org.testng.annotations.DataProvider;

public class TodoDataProvider {

    @DataProvider(name = "validTodoIds")
    public static Object[][] validTodoIds() {
        return new Object[][] {
            {1, 1, "delectus aut autem", false},
            {2, 1, "quis ut nam facilis et officia qui", false},
            {4, 1, "et porro tempora", true}
        };
    }

    @DataProvider(name = "invalidTodoIds")
    public static Object[][] invalidTodoIds() {
        return new Object[][] {
            {999},
            {0},
            {-1}
        };
    }
}