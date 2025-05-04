package com.example.jotd.infrastructure.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entity representing a joke in the system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jokes")
public class JokeDocument {

    @Id
    private String id;

    private String content;
    
    private String description;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

