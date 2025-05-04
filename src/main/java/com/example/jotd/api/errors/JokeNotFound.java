package com.example.jotd.api.errors;

/**
 * Error when a joke is not found
 */
public class JokeNotFound extends RuntimeException {

    public JokeNotFound(String jokeId) {
        super("Unable to find joke by id: " + jokeId);
    }
}
