package com.example.jotd.domain.service.transformer;

import com.example.jotd.api.errors.JokeTimeInvalid;
import com.example.jotd.api.model.Joke;
import com.example.jotd.infrastructure.repository.model.JokeDocument;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class JokeTransformer {

    public Joke of(JokeDocument jokeDocument) {
        Joke joke = new Joke();
        joke.setDate(jokeDocument.getCreatedAt().toString());
        joke.setJoke(jokeDocument.getContent());
        joke.setDescription(jokeDocument.getDescription());

        return joke;
    }

    public JokeDocument to(Joke joke) {
        JokeDocument jokeDocument = new JokeDocument();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(joke.getDate(), formatter);

            final String documentKey = String.join("-",
                    Integer.toString(zonedDateTime.getDayOfYear()),
                    Integer.toString(zonedDateTime.getYear()));

            jokeDocument.setId(documentKey);
            jokeDocument.setContent(joke.getJoke());
            jokeDocument.setDescription(joke.getDescription());
        } catch (DateTimeParseException ex) {
            throw new JokeTimeInvalid(joke.getDate());
        }

        return jokeDocument;
    }
}
