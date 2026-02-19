package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
// @Primary
public class SimpleGreetingService implements GreetingService {

    // 'Hello' is the default, gets overridden by the key-value pair
    @Value("${greeting.prefix:Hello}") 
    String prefix;

    @Override
    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            return "Simple " + prefix + ", World!";
        }
        return "Simple " + prefix + ", " + name + "!";
    }
}


