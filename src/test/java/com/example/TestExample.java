package com.example;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Optional;

public class TestExample {
    private GreetingService greetingService;
    private FakeGreetingRepository fakeRepository;

    @Before
    public void setUp() {
        fakeRepository = new FakeGreetingRepository();
        greetingService = new FormalGreetingService(fakeRepository);
    }

    @Test
    public void testGreetDBWithStoredMessage() {
        fakeRepository.setStoredGreeting("Alice",
            "Welcome back, Alice!");
        String result = greetingService.greet("Alice");
        assertEquals("Welcome back, Alice!", result);
    }

    @Test
    public void testGreetDBWithNoStoredMessage() {
        String result = greetingService.greet("Charlie");
        assertEquals("Hello, Charlie!", result);
    }

    @Test
    public void testGreetDBWithEmptyName() {
        fakeRepository.setStoredGreeting("World",
            "Hello, World!");
        String result = greetingService.greet("");
        assertEquals("Hello, World!", result);
    }

    // Simple fake for testing
    static class FakeGreetingRepository extends JdbcGreetingRepository {
        private String storedName;
        private String storedMessage;
        public FakeGreetingRepository() {
            super(null); // No real JdbcTemplate needed
        }

        public void setStoredGreeting(String name, String message) {
            this.storedName = name;
            this.storedMessage = message;
        }

        @Override
        public Optional<GreetingModel> findByName(String name) {
            if (name.equals(storedName)) {
                GreetingModel greetingModel = new GreetingModel();
                greetingModel.setName(storedName);
                greetingModel.setMessage(storedMessage);
                return Optional.of(greetingModel);
            }
            return Optional.empty();
        }
    }
}

