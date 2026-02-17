package com.example;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestExample {

    @Test
    public void testIndexReturnsGreeting() {
        Example example = new Example();
        String result = example.index();
        assertEquals("Greetings from Spring Boot!", result);
    }

    @Test
    public void testIndexReturnsConsistentResult() {
        Example example = new Example();
        String first = example.index();
        String second = example.index();
        assertEquals(first, second);
    }
}