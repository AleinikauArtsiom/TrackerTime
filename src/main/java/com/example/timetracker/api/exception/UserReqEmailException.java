package com.example.timetracker.api.exception;

public class UserReqEmailException extends RuntimeException {

    private final String message;

    public UserReqEmailException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return " We already have user with email:  " + message;
    }
}

