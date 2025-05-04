package com.example.jotd.infrastructure.repository;

import com.example.jotd.infrastructure.repository.model.JokeDocument;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for handling Joke entity operations in MongoDB.
 */
@Repository
public interface JokeRepository extends MongoRepository<JokeDocument, String> {

    /**
     * Find jokes by category.
     *
     * @param category the category to search for
     * @return a list of jokes matching the category
     */
    List<JokeDocument> findByCategory(String category);
    
    /**
     * Find a random joke using MongoDB's aggregation framework.
     *
     * @return an optional containing a random joke, or empty if no jokes exist
     */
    @Aggregation(pipeline = {
        "{ $sample: { size: 1 } }"
    })
    Optional<JokeDocument> findRandomJoke();
    
    /**
     * Find a random joke from a specific category.
     *
     * @param category the category to search within
     * @return an optional containing a random joke from the category, or empty if none exist
     */
    @Query(value = "{ 'category': ?0 }")
    @Aggregation(pipeline = {
        "{ $match: { 'category': ?0 } }",
        "{ $sample: { size: 1 } }"
    })
    Optional<JokeDocument> findRandomJokeByCategory(String category);
}

