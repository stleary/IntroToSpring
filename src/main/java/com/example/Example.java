package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@SpringBootApplication
@RestController
public class Example {

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/greeting")
    public String index(@RequestParam(defaultValue="Unknown") String name) {
        return "Hello " + name + ", Greetings from Spring Boot!";
    }
}
