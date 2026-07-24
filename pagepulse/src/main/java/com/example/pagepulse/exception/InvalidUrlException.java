package com.example.pagepulse.exception;

public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(String url) {
        super("Invalid or unsupported URL: " + url);
    }
}
