package com.example;

import java.util.List;
import java.util.Optional;

public interface GreetingRepository {
    List<GreetingModel> findAll();
    GreetingModel findByName(String name);
    GreetingModel save(GreetingModel greetingModel);
    int update(GreetingModel greetingModel);
    int delete(String name);
}
