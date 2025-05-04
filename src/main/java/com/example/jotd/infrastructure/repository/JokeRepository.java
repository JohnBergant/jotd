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
}

