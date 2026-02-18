package com.example;

import org.springframework.stereotype.Service;

@Service
public class SimpleGreetingService implements GreetingService {

	@Override
    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            return "Simple Hello, World!";
        }
        return "Simple Hello, " + name + "!";
    }
}


