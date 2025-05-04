package com.example.jotd.api.errors;

public class JokeNotFound extends RuntimeException {

    public JokeNotFound(String jokeId) {
        super("Unable to find joke by id: " + jokeId);
    }
}
