package com.example;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class GreetingModel {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Message is required")
    @Size(min = 1, max = 500, message = "Message must be between 1 and 500 characters")
    private String message;

    private LocalDateTime createdAt;

    public GreetingModel() {}

    public GreetingModel(String name, String message) {
        this.name = name;
        this.message = message;
    }
    public GreetingModel(Long id, String name, String message,
                    LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.message = message;
        this.createdAt = createdAt;
    }

    // Getters and setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
