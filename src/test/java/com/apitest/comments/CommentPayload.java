package com.apitest.comments;

import java.util.HashMap;
import java.util.Map;

public class CommentPayload {

    public static Map<String, Object> create() {
        Map<String, Object> comment = new HashMap<>();
        comment.put("postId", 1);
        comment.put("name", "Test Comment");
        comment.put("email", "test@automation.com");
        comment.put("body", "This comment was created by REST Assured automation.");
        return comment;
    }
}