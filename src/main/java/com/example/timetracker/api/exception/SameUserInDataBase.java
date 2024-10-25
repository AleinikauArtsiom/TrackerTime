package com.example.timetracker.api.exception;

public class SameUserInDataBase extends RuntimeException {
    private final String message;

    public SameUserInDataBase(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return " Registration problem! We already have user with login:  " + message;
    }
}
