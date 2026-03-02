package com.apitest.posts;

import java.util.HashMap;
import java.util.Map;

public class PostDataProvider {

    public static PostPayload createPost() {
        return new PostPayload(1, "Automation Test Post", "This post was created by REST Assured automation.");
    }

    public static PostPayload updatePost() {
        PostPayload post = new PostPayload(1, "Updated Post Title", "Updated post body content.");
        post.setId(1);
        return post;
    }

    public static Map<String, Object> patchPost() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Patched Title - REST Assured");
        return data;
    }
}
