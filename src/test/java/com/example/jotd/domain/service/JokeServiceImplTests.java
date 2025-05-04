package com.example.jotd.domain.service;

import com.example.jotd.api.errors.JokeNotFound;
import com.example.jotd.api.model.Joke;
import com.example.jotd.api.model.JokeResponse;
import com.example.jotd.domain.transformer.JokeTransformer;
import com.example.jotd.infrastructure.repository.JokeRepository;
import com.example.jotd.infrastructure.repository.model.JokeDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JokeServiceImplTests {

    @InjectMocks
    private JokeServiceImpl jokeServiceImpl;

    @Mock
    private JokeRepository jokeRepository;

    @Mock
    private JokeTransformer jokeTransformer;

    JokeDocument jokeDocument;
    JokeResponse jokeResponse;
    Joke joke;

    private final String TEST_JOKE_ID = "125-2025";

    @BeforeEach
    public void setup() {
        jokeDocument = new JokeDocument();
        jokeDocument.setId(TEST_JOKE_ID);
        jokeDocument.setContent("Why did the programmer quit his job? Because he didn't get arrays!");
        jokeDocument.setDescription("Programming joke");
        jokeDocument.setCreatedAt(LocalDateTime.now());

        jokeResponse = new JokeResponse();
        jokeResponse.setJokeId(TEST_JOKE_ID);
        jokeResponse.setJoke("Why did the programmer quit his job? Because he didn't get arrays!");
        jokeResponse.setDescription("Programming joke");

        joke = new Joke();
        joke.setJoke("Why did the programmer quit his job? Because he didn't get arrays!");
        joke.setDescription("Programming joke");
        joke.setDate(LocalDateTime.now());

        jokeServiceImpl = new JokeServiceImpl(jokeRepository, jokeTransformer);
    }

    @Test
    public void givenGetJokeInvocation_whenNoJokeFound_thenThrowJokeNotFound() {
        //given
        Optional<JokeDocument> jokeResponseOptional = Optional.empty();

        // when
        when(jokeRepository.findById(any())).thenReturn(jokeResponseOptional);

        // then
        assertThrows(JokeNotFound.class, () -> jokeServiceImpl.getJokeOfTheDay());
    }

    @Test
    public void givenGetJokeInvocation_whenTheJokeExists_thenReturnJokeResponse() {
        // Given
        when(jokeTransformer.getJokeKey(any(LocalDateTime.class))).thenReturn(TEST_JOKE_ID);
        when(jokeRepository.findById(TEST_JOKE_ID)).thenReturn(Optional.of(jokeDocument));
        when(jokeTransformer.of(jokeDocument)).thenReturn(jokeResponse);

        // When
        JokeResponse result = jokeServiceImpl.getJokeOfTheDay();

        // Then
        assertNotNull(result);
        assertEquals(TEST_JOKE_ID, result.getJokeId());
        assertEquals(jokeDocument.getDescription(), result.getDescription());
        assertEquals(jokeDocument.getContent(), result.getJoke());
        verify(jokeTransformer).getJokeKey(any(LocalDateTime.class));
        verify(jokeRepository).findById(TEST_JOKE_ID);
        verify(jokeTransformer).of(jokeDocument);
    }

    @Test
    void givenUpdateJokeInvocation_whenJokeExists_thenReturnProperlyUpdateAndReturnUpdatedJokeResponse() {
        // Given
        JokeDocument updatedJokeDocument = new JokeDocument();
        updatedJokeDocument.setId(TEST_JOKE_ID);
        updatedJokeDocument.setContent("Updated content");
        updatedJokeDocument.setDescription("Updated description");
        updatedJokeDocument.setCreatedAt(LocalDateTime.now());

        JokeResponse updatedJokeResponse = new JokeResponse();
        updatedJokeResponse.setJokeId(TEST_JOKE_ID);
        updatedJokeResponse.setJoke("Updated content");
        updatedJokeResponse.setDescription("Updated description");

        when(jokeRepository.findById(TEST_JOKE_ID)).thenReturn(Optional.of(jokeDocument));
        when(jokeTransformer.to(joke)).thenReturn(updatedJokeDocument);
        when(jokeRepository.save(any(JokeDocument.class))).thenReturn(updatedJokeDocument);
        when(jokeTransformer.of(updatedJokeDocument)).thenReturn(updatedJokeResponse);

        // When
        JokeResponse result = jokeServiceImpl.updateJoke(TEST_JOKE_ID, joke);

        // Then
        assertNotNull(result);
        assertEquals(updatedJokeResponse.getJokeId(), result.getJokeId());
        assertEquals(updatedJokeResponse.getDescription(), result.getDescription());
        assertEquals(updatedJokeResponse.getJoke(), result.getJoke());

        verify(jokeRepository).findById(TEST_JOKE_ID);
        verify(jokeTransformer).to(joke);
        verify(jokeRepository).save(any(JokeDocument.class));
        verify(jokeTransformer).of(updatedJokeDocument);
    }

    @Test
    void givenUpdateJokeInvocation_whenJokeDoesNotExist_thenThrowJokeNotFound() {
        // Given
        when(jokeRepository.findById(TEST_JOKE_ID)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(JokeNotFound.class, () -> jokeServiceImpl.updateJoke(TEST_JOKE_ID, joke));
    }

    @Test
    void givenAValidJoke_whenJokeIsSaved_thenJokeResponseIsReturned() {
        // Given
        when(jokeTransformer.to(joke)).thenReturn(jokeDocument);
        when(jokeRepository.save(jokeDocument)).thenReturn(jokeDocument);
        when(jokeTransformer.of(jokeDocument)).thenReturn(jokeResponse);

        // When
        JokeResponse result = jokeServiceImpl.saveJoke(joke);

        // Then
        assertNotNull(result);
        assertEquals(jokeResponse.getJokeId(), result.getJokeId ());
        assertEquals(jokeResponse.getJoke(), result.getJoke());
        assertEquals(jokeResponse.getDescription(), result.getDescription());

        verify(jokeTransformer).to(joke);
        verify(jokeRepository).save(jokeDocument);
        verify(jokeTransformer).of(jokeDocument);
    }

    @Test
    void givenAValidJokeId_whenDeleteJokeIsInvoked_thenJokeIsDeleted() {
        // Given
        when(jokeRepository.existsById(TEST_JOKE_ID)).thenReturn(true);
        doNothing().when(jokeRepository).deleteById(TEST_JOKE_ID);

        // When
        jokeServiceImpl.deleteJoke(TEST_JOKE_ID);

        // Then
        verify(jokeRepository).existsById(TEST_JOKE_ID);
        verify(jokeRepository).deleteById(TEST_JOKE_ID);
    }

    @Test
    void givenAnInvalidJokeId_whenDeleteJokeIsInvoked_thenJokeRepositoryIsNotCalled() {
        // Given
        when(jokeRepository.existsById(TEST_JOKE_ID)).thenReturn(false);

        // When
        jokeServiceImpl.deleteJoke(TEST_JOKE_ID);

        // Then
        verify(jokeRepository, never()).deleteById(TEST_JOKE_ID);
    }
}
