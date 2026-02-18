package com.example;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class FormalGreetingService implements GreetingService {

	@Override
    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            return "Formal Hello, World!";
        }
        return "Formal Hello, " + name + "!";
    }
}
