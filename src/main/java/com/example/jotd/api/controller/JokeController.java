package com.example.jotd.api.controller;

import com.example.jotd.api.errors.JokeNotFound;
import com.example.jotd.api.errors.JokeTimeInvalid;
import com.example.jotd.api.model.Joke;
import com.example.jotd.api.model.JokeResponse;
import com.example.jotd.domain.service.JokeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for handling joke-related requests.
 */
@RestController
@RequestMapping("/v1/jokes")
@Tag(name = "Jokes", description = "Endpoints for performing CRUD operations on jokes")
@Validated
@RequiredArgsConstructor
@Slf4j
public class JokeController {

    private final JokeService jokeService;

    @GetMapping("/daily")
    @Operation(summary = "Get joke of the day", description = "Retrieves the current joke of the day")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the joke of the day")
    })
    public ResponseEntity<JokeResponse> getJokeOfTheDay() {
        return ResponseEntity.ok(jokeService.getJokeOfTheDay());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new joke", description = "Creates a new joke in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Joke successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid joke data", 
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<JokeResponse> createJoke(
            @Parameter(description = "Joke object to be created", required = true)
            @Valid @RequestBody Joke joke) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jokeService.saveJoke(joke));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a joke", description = "Updates an existing joke")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Joke successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid joke data", 
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Joke not found", 
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<JokeResponse> updateJoke(
            @Parameter(description = "ID of the joke to update", required = true)
            @PathVariable @NotBlank String id,
            @Parameter(description = "Updated joke data", required = true)
            @Valid @RequestBody Joke joke) {
        JokeResponse updatedJoke = jokeService.updateJoke(id, joke);

        return ResponseEntity.ok(updatedJoke);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a joke", description = "Deletes a joke from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Joke successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Joke not found", 
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> deleteJoke(
            @Parameter(description = "ID of the joke to delete", required = true)
            @PathVariable @NotBlank String id) {
        jokeService.deleteJoke(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Exception handler for JokeNotFoundException.
     */
    @ExceptionHandler(JokeTimeInvalid.class)
    public ResponseEntity<ApiError> handleInvalidJokeTime(JokeTimeInvalid ex, HttpServletRequest request) {
        ApiError error = ApiError.fromException(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Exception handler for DateTimeParseException.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleDateTimeParseException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ApiError error = ApiError.fromException(HttpStatus.BAD_REQUEST, "Unable to parse request", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Exception handler for JokeNotFoundException.
     */
    @ExceptionHandler(JokeNotFound.class)
    public ResponseEntity<ApiError> handleJokeNotFound(JokeNotFound ex, HttpServletRequest request) {
        ApiError error = ApiError.fromException(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Exception handler for other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception", ex);
        ApiError error = ApiError.fromException(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Unable to handle request",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

