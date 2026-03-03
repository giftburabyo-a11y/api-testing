package com.apitest.users;

import java.util.HashMap;
import java.util.Map;

public class UserPayload {

    public static Map<String, Object> create() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Test User");
        user.put("username", "testuser");
        user.put("email", "testuser@automation.com");
        user.put("phone", "1-770-736-8031");
        user.put("website", "automation.org");
        return user;
    }
}