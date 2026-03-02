package com.apitest.utils;

import com.apitest.models.Post;
import java.util.HashMap;
import java.util.Map;

public class TestDataGenerator {
    public static Post createValidPost() {
        return new Post(1, "Test Post Title - Automation", "This is a test post body created by REST Assured.");
    }

    public static Post createUpdatedPost() {
        Post post = new Post(1, "Updated Post Title - Automation", "This is an updated post body.");
        post.setId(1);
        return post;
    }

    public static Map<String, Object> createPartialUpdateData() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Patched Title - REST Assured");
        return data;
    }

    public static Map<String, Object> createPostAsMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1);
        data.put("title", "Map-based Post Title");
        data.put("body", "Map-based post body content");
        return data;
    }
}