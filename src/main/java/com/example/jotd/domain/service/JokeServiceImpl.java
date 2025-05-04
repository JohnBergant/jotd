package com.example.jotd.domain.service;

import com.example.jotd.api.errors.JokeNotFound;
import com.example.jotd.api.model.Joke;
import com.example.jotd.api.model.JokeResponse;
import com.example.jotd.domain.transformer.JokeTransformer;
import com.example.jotd.infrastructure.repository.JokeRepository;
import com.example.jotd.infrastructure.repository.model.JokeDocument;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Implementation of the JokeService interface.
 */
@Service
@RequiredArgsConstructor
public class JokeServiceImpl implements JokeService {

    private static final Logger logger = LoggerFactory.getLogger(JokeServiceImpl.class);

    private final JokeRepository jokeRepository;

    private final JokeTransformer jokeTransformer;
    
    private static final String TIMEZONE_UTC = "UTC";

    @Override
    public JokeResponse getJokeOfTheDay() {
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of(TIMEZONE_UTC));
        LocalDateTime localDateTimeFromZoned = utcNow.toLocalDateTime();
        final String jokeKey = jokeTransformer.getJokeKey(localDateTimeFromZoned);

        Optional<JokeDocument> optionalJokeDocument = jokeRepository.findById(jokeKey);

        if (optionalJokeDocument.isEmpty()) {
            throw new JokeNotFound(jokeKey);
        }
        return jokeTransformer.of(optionalJokeDocument.get());
    }

    @Override
    public JokeResponse updateJoke(String jokeId, Joke joke) {
        Optional<JokeDocument> optionalJokeDocument = jokeRepository.findById(jokeId);

        if (optionalJokeDocument.isEmpty()) {
            throw new JokeNotFound(jokeId);
        }

        JokeDocument jokeDocument = optionalJokeDocument.get();
        JokeDocument updatedJokeDocument = jokeTransformer.to(joke);
        jokeDocument.setContent(updatedJokeDocument.getContent());
        jokeDocument.setDescription(updatedJokeDocument.getDescription());
        jokeDocument.setCreatedAt(updatedJokeDocument.getCreatedAt());
        jokeDocument = jokeRepository.save(jokeDocument);

        JokeResponse updatedJoke = jokeTransformer.of(jokeDocument);
        return updatedJoke;
    }

    @Override
    public JokeResponse saveJoke(Joke joke) {
        JokeDocument jokeDocument = jokeTransformer.to(joke);
        jokeDocument = jokeRepository.save(jokeDocument);

        return jokeTransformer.of(jokeDocument);
    }

    @Override
    public void deleteJoke(String id) {
        if (jokeRepository.existsById(id)) {
            jokeRepository.deleteById(id);
        }
    }
}

