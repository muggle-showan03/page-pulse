package com.example.pagepulse.exception;

public class PageFetchException extends RuntimeException {

    public PageFetchException(String url, Throwable cause) {
        super("Failed to fetch page: " + url, cause);
    }
}
