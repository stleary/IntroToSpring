package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestExample {

    // --- Existing controller tests (updated) ---

    @Test
    public void testIndexReturnsGreeting() {
        GreetingService service = new GreetingService();
        Example example = new Example(service);
        String result = example.index();
        assertEquals("Greetings from Spring Boot!", result);
    }

    // --- GreetingService unit tests ---

    @Test
    public void testGreetWithName() {
        GreetingService service = new GreetingService();
        String result = service.greet("Alice");
        assertEquals("Hello, Alice!", result);
    }

    @Test
    public void testGreetWithEmptyName() {
        GreetingService service = new GreetingService();
        String result = service.greet("");
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testGreetWithNull() {
        GreetingService service = new GreetingService();
        String result = service.greet(null);
        assertEquals("Hello, World!", result);
    }

    // --- Controller greeting endpoint tests ---

    @Test
    public void testGreetingEndpointWithName() {
        GreetingService service = new GreetingService();
        Example example = new Example(service);
        String result = example.greeting("Bob");
        assertEquals("Hello, Bob!", result);
    }

    @Test
    public void testGreetingEndpointWithEmptyName() {
        GreetingService service = new GreetingService();
        Example example = new Example(service);
        String result = example.greeting("");
        assertEquals("Hello, World!", result);
    }
}

