package com.example.takemeds.controllers.exceptionHandlers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;

    private int status;

    private long timestamp;

    public ErrorResponse(String message, int status, long timestamp) {
        setMessage(message);
        setStatus(status);
        setTimestamp(timestamp);
    }

    public ErrorResponse() {
    }
}
