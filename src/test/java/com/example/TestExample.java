package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Before;

public class TestExample {
    private GreetingService greetingService;
    private TimeService timeService;
    private Example example;
    
    @Before
    public void setup() {
        greetingService = new SimpleGreetingService();
        timeService = new TimeService();
        example = new Example(greetingService, timeService);
    }

    // --- Existing controller tests (updated) ---

    @Test
    public void testIndexReturnsGreeting() {
        String result = example.index();
        assertEquals("Greetings from Spring Boot!", result);
    }

    // --- GreetingService unit tests ---

    @Test
    public void testGreetWithName() {
        String result = example.index("Alice");
        assertEquals("Simple Hello, Alice! 2026-01-01", result);
    }

    @Test
    public void testGreetingEndpointWithName() {
        String result = example.index("Bob");
        assertEquals("Simple Hello, Bob! 2026-01-01", result);
    }

    @Test
    public void testGreetWithEmptyName() {
        String result = example.index("");
        assertEquals("Simple Hello, World! 2026-01-01", result);
    }

    @Test
    public void testGreetWithNull() {
        String result = example.index(null);
        assertEquals("Simple Hello, World! 2026-01-01", result);
    }
}


