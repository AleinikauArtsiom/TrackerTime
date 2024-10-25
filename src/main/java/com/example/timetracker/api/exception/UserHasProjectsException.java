package com.example.timetracker.api.exception;

public class UserHasProjectsException extends RuntimeException {
    public UserHasProjectsException(String message) {
        super(message);
    }
}
