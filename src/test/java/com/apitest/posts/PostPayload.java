package com.apitest.posts;

import java.util.HashMap;
import java.util.Map;

public class PostPayload {

    public static Map<String, Object> create() {
        Map<String, Object> post = new HashMap<>();
        post.put("userId", 1);
        post.put("title", "Automation Test Post");
        post.put("body", "This post was created by REST Assured automation.");
        return post;
    }

    public static Map<String, Object> update() {
        Map<String, Object> post = new HashMap<>();
        post.put("id", 1);
        post.put("userId", 1);
        post.put("title", "Updated Post Title");
        post.put("body", "Updated post body content.");
        return post;
    }

    public static Map<String, Object> patch() {
        Map<String, Object> post = new HashMap<>();
        post.put("title", "Patched Title - REST Assured");
        return post;
    }
}