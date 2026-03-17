package com.example.repository;

import com.example.model.GreetingModel;

import java.util.List;

public interface GreetingRepository {
    List<GreetingModel> findAll();
    GreetingModel findByName(String name);
    GreetingModel save(GreetingModel greetingModel);
    int update(GreetingModel greetingModel);
    int delete(String name);
}
