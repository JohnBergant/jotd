package com.example.jotd.domain.service;

import com.example.jotd.api.model.Joke;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing jokes.
 */
public interface JokeService {
    
    /**
     * Find all jokes.
     *
     * @return list of all jokes
     */
    List<Joke> findAll();

    /**
     * Find a joke by a given id
     * @param jokeId the id of the joke to find
     * @return An optional of a Joke
     */
    Optional<Joke> findById(String jokeId);

    /**
     * Get the joke of the day.
     * This could be determined by various algorithms, 
     * but for simplicity, it returns a random joke each day.
     * @param jokeId the id of the joke to return
     * @return the joke of the day
     */
    Joke getJokeOfTheDay(String jokeId);

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

