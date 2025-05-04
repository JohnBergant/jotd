package com.example.jotd.api.model;

import lombok.Data;

/**
 * Joke response for the api
 */
@Data
public class JokeResponse {

    public String jokeId;

    public String description;

    public String joke;
}
