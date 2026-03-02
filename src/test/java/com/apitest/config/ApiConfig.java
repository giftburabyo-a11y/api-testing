package com.apitest.config;

public class ApiConfig {
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final String POSTS_ENDPOINT = "/posts";
    public static final String USERS_ENDPOINT = "/users";
    public static final String COMMENTS_ENDPOINT = "/comments";
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_NOT_FOUND = 404;
    private ApiConfig() {}
}