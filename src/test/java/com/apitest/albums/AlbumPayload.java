package com.apitest.albums;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumPayload {
    private Integer id;
    private Integer userId;
    private String title;

    public AlbumPayload() {}

    public AlbumPayload(Integer userId, String title) {
        this.userId = userId;
        this.title = title;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
