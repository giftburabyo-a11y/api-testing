package com.apitest.todos;

public class TodoDataProvider {
    public static int getValidTodoId() { return 1; }
    public static int getInvalidTodoId() { return 99999; }
    public static boolean getCompletedStatus() { return true; }
    public static boolean getIncompleteStatus() { return false; }
}
