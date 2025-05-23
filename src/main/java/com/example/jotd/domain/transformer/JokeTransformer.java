package com.example.jotd.domain.transformer;

import com.example.jotd.api.errors.JokeInvalidException;
import com.example.jotd.api.model.Joke;
import com.example.jotd.api.model.JokeResponse;
import com.example.jotd.infrastructure.repository.model.JokeDocument;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Transformer for the business logic layer
 */
@Service
public class JokeTransformer {

    public JokeResponse of(JokeDocument jokeDocument) {
        JokeResponse joke = new JokeResponse();
        joke.setJokeId(jokeDocument.getId());
        joke.setJoke(jokeDocument.getContent());
        joke.setDescription(jokeDocument.getDescription());

        return joke;
    }

    public JokeDocument to(Joke joke) {
        if (joke.getJoke() == null || joke.getJoke().isBlank() ) {
            throw new JokeInvalidException("joke");
        }

        if (joke.getJoke().length() > 1000 ) {
            throw new JokeInvalidException("joke");
        }

        if (joke.getDescription() != null && joke.getDescription().length() > 1000) {
            throw new JokeInvalidException("description");
        }

        JokeDocument jokeDocument = new JokeDocument();

        final String documentKey = getJokeKey(joke.getDate());

        jokeDocument.setId(documentKey);
        jokeDocument.setContent(joke.getJoke());
        jokeDocument.setDescription(joke.getDescription());

        return jokeDocument;
    }

    public String getJokeKey(LocalDateTime localDateTime) {
        String dayOfYear = Integer.toString(localDateTime.getDayOfYear());
        String year = Integer.toString(localDateTime.getYear());

        return String.join("-", dayOfYear, year);
    }
}
