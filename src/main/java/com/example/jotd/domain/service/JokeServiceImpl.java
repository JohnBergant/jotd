package com.example.jotd.domain.service;

import com.example.jotd.api.errors.JokeNotFound;
import com.example.jotd.api.model.Joke;
import com.example.jotd.domain.service.transformer.JokeTransformer;
import com.example.jotd.infrastructure.repository.JokeRepository;
import com.example.jotd.infrastructure.repository.model.JokeDocument;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Implementation of the JokeService interface.
 */
@Service
@RequiredArgsConstructor
public class JokeServiceImpl implements JokeService {

    private static final Logger logger = LoggerFactory.getLogger(JokeServiceImpl.class);

    private final JokeRepository jokeRepository;

    private final JokeTransformer jokeTransformer;
    
    // Cache key for the joke of the day
    private static final String JOKE_OF_THE_DAY_CACHE = "jokeOfTheDay";
    
    // For determining the "joke of the day" if we don't have access to persistence
    private static final Random random = new Random();

    @Override
    public List<Joke> findAll() {
        List<JokeDocument> jokeDocuments = jokeRepository.findAll();
        List<Joke> jokes = new ArrayList<>();

        for (JokeDocument jokeDocument : jokeDocuments) {
            jokes.add(jokeTransformer.of(jokeDocument));
        }

        return jokes;
    }

    @Override
    public Optional<Joke> findById(String jokeId) {
        Optional<JokeDocument> jokeDocument = jokeRepository.findById(jokeId);

        return jokeDocument.map(jokeTransformer::of);
    }


    @Override
    @Cacheable(value = JOKE_OF_THE_DAY_CACHE)
    public Joke getJokeOfTheDay(String jokeId) {
        return null;
    }

    @Override
    public Optional<Joke> updateJoke(String jokeId, Joke joke) {
        Optional<JokeDocument> optionalJokeDocument = jokeRepository.findById(jokeId);

        if (optionalJokeDocument.isEmpty()) {
            return Optional.empty();
        }

        JokeDocument jokeDocument = optionalJokeDocument.get();
        JokeDocument updatedJokeDocument = jokeTransformer.to(joke);
        jokeDocument.setContent(updatedJokeDocument.getContent());
        jokeDocument.setDescription(updatedJokeDocument.getDescription());
        jokeDocument.setCreatedAt(updatedJokeDocument.getCreatedAt());
        jokeDocument = jokeRepository.save(jokeDocument);

        Joke updatedJoke = jokeTransformer.of(jokeDocument);
        return Optional.of(updatedJoke);
    }

    @Override
    public Joke saveJoke(Joke joke) {
        logger.debug("Saving joke: {}", joke);
        // validate joke
        JokeDocument jokeDocument = jokeTransformer.to(joke);
        jokeDocument = jokeRepository.save(jokeDocument);

        return jokeTransformer.of(jokeDocument);
    }

    @Override
    public void deleteJoke(String id) {
        logger.debug("Deleting joke with id: {}", id);
        if (!jokeRepository.existsById(id)) {
            logger.error("Failed to delete joke, id not found: {}", id);
            throw new JokeNotFound(id);
        }
        jokeRepository.deleteById(id);
        jokeRepository.deleteById(id);
    }

    /**
     * Clears the joke of the day cache at midnight every day
     * to ensure a new joke is selected for the next day.
     */
    @Scheduled(cron = "0 0 0 * * ?")  // Run at midnight every day
    @CacheEvict(value = JOKE_OF_THE_DAY_CACHE, allEntries = true)
    public void clearJokeOfTheDayCache() {
        logger.info("Cleared joke of the day cache at {}", LocalDate.now());
    }
}

