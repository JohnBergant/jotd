package com.example.jotd.domain.service;

import com.example.jotd.api.model.Joke;
import com.example.jotd.api.model.JokeResponse;

import java.util.Optional;

/**
 * Service interface for managing jokes.
 */
public interface JokeService {
    /**
     * Return the joke of the day
     * This returns the cached value that resets at midnight UTC
     */
    JokeResponse getJokeOfTheDay();

    /**
     * Update a joke
     *
     * @param jokeId the id of the joke to update
     * @return the updated joke
     */
    JokeResponse updateJoke(String jokeId, Joke joke);

    /**
     * Save a new joke or update an existing one.
     *
     * @param joke the joke to save or update
     * @return the saved or updated joke
     */
    JokeResponse saveJoke(Joke joke);
    
    /**
     * Delete a joke by its ID.
     *
     * @param id the ID of the joke to delete
     */
    void deleteJoke(String id);
}

