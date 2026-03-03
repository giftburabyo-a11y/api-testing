package com.apitest.todos;

import java.util.HashMap;
import java.util.Map;

public class TodoPayload {

    public static Map<String, Object> create() {
        Map<String, Object> todo = new HashMap<>();
        todo.put("userId", 1);
        todo.put("title", "Test Todo Item");
        todo.put("completed", false);
        return todo;
    }

    public static Map<String, Object> update() {
        Map<String, Object> todo = new HashMap<>();
        todo.put("id", 1);
        todo.put("userId", 1);
        todo.put("title", "Updated Todo Item");
        todo.put("completed", true);
        return todo;
    }
}