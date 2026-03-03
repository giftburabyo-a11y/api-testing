package com.apitest.photos;

import java.util.HashMap;
import java.util.Map;

public class PhotoPayload {

    public static Map<String, Object> create() {
        Map<String, Object> photo = new HashMap<>();
        photo.put("albumId", 1);
        photo.put("title", "Test Photo Title");
        photo.put("url", "https://via.placeholder.com/600/automation");
        photo.put("thumbnailUrl", "https://via.placeholder.com/150/automation");
        return photo;
    }
}