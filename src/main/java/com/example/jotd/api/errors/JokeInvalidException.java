package com.example.jotd.api.errors;

public class JokeInvalidException extends RuntimeException {
    public JokeInvalidException(String parameter) {
        super("Provided parameter: " + parameter + " is not valid");
    }
}
