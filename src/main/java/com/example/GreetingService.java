package com.example;

import java.util.List;

public interface GreetingService {
    List<GreetingModel> findAll();
    GreetingModel findByName(String name);
    GreetingModel create(GreetingModel greetingModel);
    GreetingModel update(GreetingModel greetingModel);
    int delete(String name);
}

