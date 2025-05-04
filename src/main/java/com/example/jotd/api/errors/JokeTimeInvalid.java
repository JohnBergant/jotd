package com.example.jotd.api.errors;

public class JokeTimeInvalid extends RuntimeException {

    public JokeTimeInvalid(String rawDate) {
        super("Unable to parse provided date time: " + rawDate);
    }
}
