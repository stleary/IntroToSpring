package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Primary
public class FormalGreetingService implements GreetingService {
    private final GreetingRepository greetingRepository;


    @Autowired
    public FormalGreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            name = "World";
        }

        Optional<GreetingModel> stored = greetingRepository.findByName(name);

        if (stored.isPresent()) {
            return stored.get().getMessage();
        }
        return "Hello, " + name + "!";
    }
}
