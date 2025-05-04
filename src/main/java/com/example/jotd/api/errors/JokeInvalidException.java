package com.example.jotd.api.errors;

/**
 * Error when a joke request is invalid for some reason
 */
public class JokeInvalidException extends RuntimeException {
    public JokeInvalidException(String field) {
        super("Provided value in: " + field + " is not valid");
    }
}
