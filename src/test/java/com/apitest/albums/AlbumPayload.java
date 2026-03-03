package com.apitest.albums;

import java.util.HashMap;
import java.util.Map;

public class AlbumPayload {

    public static Map<String, Object> create() {
        Map<String, Object> album = new HashMap<>();
        album.put("userId", 1);
        album.put("title", "Test Album Title");
        return album;
    }

    public static Map<String, Object> update() {
        Map<String, Object> album = new HashMap<>();
        album.put("userId", 1);
        album.put("id", 1);
        album.put("title", "Updated Album Title");
        return album;
    }
}