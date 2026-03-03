package com.apitest.config;

public class ApiConfig {
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_NOT_FOUND = 404;
    public static final long MAX_RESPONSE_TIME_MS = 5000L;
    private ApiConfig() {}
}