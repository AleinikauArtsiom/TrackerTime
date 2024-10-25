package com.example.timetracker.api.exception;

public class CustomValidException extends RuntimeException {
    private final String message;

    public CustomValidException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "problem with validation " + message;
    }
}
