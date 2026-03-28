package com.example;

import java.time.LocalDateTime;

public class GreetingModel {
    private Long id;
    private String name;
    private String message;
    private LocalDateTime createdAt;

    public GreetingModel() {}

    public GreetingModel(Long id, String name, String message,
                    LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.message = message;
        this.createdAt = createdAt;
    }

    public GreetingModel(String name, String message) {
        this.name = name;
        this.message = message;
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
