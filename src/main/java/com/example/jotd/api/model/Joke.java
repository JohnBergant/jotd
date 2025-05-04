package com.example.jotd.api.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Joke request for the api
 */
@Data
public class Joke {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime date;

    public String description;

    public String joke;
}
