package com.example.jotd.domain.service;

import com.example.jotd.api.model.Joke;

import java.util.Optional;

/**
 * Service interface for managing jokes.
 */
public interface JokeService {

    /**
     * Find a joke by a given id
     * @param jokeId the id of the joke to find
     * @return An optional of a Joke
     */
    Optional<Joke> findById(String jokeId);

    /**
     * Return the joke of the day
     * This returns the cached value that resets at midnight UTC
     */
    Joke getJokeOfTheDay();

    /**
     * Update a joke
     *
     * @param jokeId the id of the joke to update
     * @return the updated joke
     */
    Optional<Joke> updateJoke(String jokeId, Joke joke);

    /**
     * Save a new joke or update an existing one.
     *
     * @param joke the joke to save or update
     * @return the saved or updated joke
     */
    Joke saveJoke(Joke joke);
    
    /**
     * Delete a joke by its ID.
     *
     * @param id the ID of the joke to delete
     */
    void deleteJoke(String id);
}

