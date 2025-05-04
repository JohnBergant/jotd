package com.example.jotd.api.errors;

/**
 * Error when the time provided for a joke is not valid
 */
public class JokeTimeInvalid extends RuntimeException {

    public JokeTimeInvalid(String rawDate) {
        super("Unable to parse provided date time: " + rawDate);
    }
}
