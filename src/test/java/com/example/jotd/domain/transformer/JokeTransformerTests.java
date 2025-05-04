package com.example.jotd.domain.transformer;

import com.example.jotd.api.model.Joke;
import com.example.jotd.api.model.JokeResponse;
import com.example.jotd.infrastructure.repository.model.JokeDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JokeTransformerTest {

    @InjectMocks
    private JokeTransformer jokeTransformer;

    @Test
    void givenAValidJokeDocument_whenOfIsInvoked_thenReturnJokeResponse() {
        // Given
        JokeDocument jokeDocument = new JokeDocument();
        jokeDocument.setId("joke-123");
        jokeDocument.setContent("Why did the developer go broke? Because he used up all his cache!");
        jokeDocument.setDescription("Developer joke");
        LocalDateTime createdAt = LocalDateTime.of(2025, 5, 4, 10, 30);
        jokeDocument.setCreatedAt(createdAt);

        // When
        JokeResponse jokeResponse = jokeTransformer.of(jokeDocument);

        // Then
        assertNotNull(jokeResponse);
        assertEquals("joke-123", jokeResponse.getJokeId());
        assertEquals("Why did the developer go broke? Because he used up all his cache!", jokeResponse.getJoke());
        assertEquals("Developer joke", jokeResponse.getDescription());
    }

    @Test
    void givenAValidJokeDocument_whenToIsInvoked_thenReturnAValidJokeDocument() {
        // Given
        Joke joke = new Joke();
        joke.setJoke("How many programmers does it take to change a light bulb? None, it's a hardware problem.");
        joke.setDescription("Programmer joke");
        LocalDateTime jokeDate = LocalDateTime.of(2025, 5, 4, 10, 30);
        joke.setDate(jokeDate);

        // When
        JokeDocument jokeDocument = jokeTransformer.to(joke);

        // Then
        assertNotNull(jokeDocument);
        assertEquals("124-2025", jokeDocument.getId());
        assertEquals("How many programmers does it take to change a light bulb? None, it's a hardware problem.", jokeDocument.getContent());
        assertEquals("Programmer joke", jokeDocument.getDescription());
    }

    @Test
    void givenValidTimes_whenGetJokeKeyIsInvoked_thenReturnValidKeys() {
        // Given
        LocalDateTime dateTime1 = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 5, 4, 10, 30);
        LocalDateTime dateTime3 = LocalDateTime.of(2025, 12, 31, 23, 59);

        // When
        String key1 = jokeTransformer.getJokeKey(dateTime1);
        String key2 = jokeTransformer.getJokeKey(dateTime2);
        String key3 = jokeTransformer.getJokeKey(dateTime3);

        // Then
        assertEquals("1-2025", key1);
        assertEquals("124-2025", key2);
        assertEquals("365-2025", key3);
    }

    @Test
    void givenALeapYear_whenGetJokeKeyIsInvoked_thenReturnValidKey() {
        // Given
        LocalDateTime leapYearDate1 = LocalDateTime.of(2024, 12, 31, 23, 59);
        LocalDateTime leapYearDate2 = LocalDateTime.of(2024, 2, 29, 23, 59);

        // When
        String key1 = jokeTransformer.getJokeKey(leapYearDate1);
        String key2 = jokeTransformer.getJokeKey(leapYearDate2);

        // Then
        assertEquals("366-2024", key1);
        assertEquals("60-2024", key2);
    }
}