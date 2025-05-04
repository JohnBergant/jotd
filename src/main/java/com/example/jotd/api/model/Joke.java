package com.example.jotd.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class Joke {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public String date;

    @Size(max = 1000)
    public String description;

    @NotBlank
    @Size(max = 10000)
    public String joke;
}
