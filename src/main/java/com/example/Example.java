package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Example {

    private final GreetingService greetingService;
    private final TimeService timeService;

    @Autowired
    public Example(GreetingService greetingService, TimeService timeService) {
        this.greetingService = greetingService;
        this.timeService = timeService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/greeting")
    public String index(
            @RequestParam(value = "name", defaultValue = "") String name) {
        return greetingService.greet(name) + timeService.getTime();
    }
}
