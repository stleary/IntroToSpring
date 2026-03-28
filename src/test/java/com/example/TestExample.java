package com.example;

import com.example.model.GreetingModel;
import com.example.repository.GreetingRepository;
import com.example.service.FormalGreetingService;
import com.example.service.GreetingService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TestExample {
    private GreetingService greetingService;
    private FakeGreetingRepository fakeRepository;

    @Before
    public void setUp() {
        fakeRepository = new FakeGreetingRepository();
        greetingService = new FormalGreetingService(fakeRepository);
    }
    
    // --- Service Tests: findAll() ---

    @Test
    public void testFindAllReturnsAllGreetings() {
        fakeRepository.addGreeting(1L, "Alice", "Hello Alice");
        fakeRepository.addGreeting(2L, "Bob", "Hello Bob");
        List<GreetingModel> results = greetingService.findAll();
        assertEquals(2, results.size());
    }

    // --- Service Tests: findByName() ---

    @Test
    public void testFindByIdReturnsGreeting() {
        fakeRepository.addGreeting(1L, "Alice", "Hello Alice");
        GreetingModel result = greetingService.findByName("Alice");
        assertEquals("Alice", result.getName());
    }


    // --- Service Tests: create() ---

    @Test

    public void testCreateAssignsId() {
        GreetingModel greetingModel = new GreetingModel();
        greetingModel.setName("Charlie");
        greetingModel.setMessage("Hello Charlie");
        GreetingModel saved = greetingService.create(greetingModel);
        assertNotNull(saved.getId());
        assertEquals("Charlie", saved.getName());
    }

    // --- Service Tests: update() ---

    @Test
    public void testUpdateModifiesGreeting() {
        fakeRepository.addGreeting(1L, "Alice", "Hello Alice");
        GreetingModel updated = new GreetingModel();
        updated.setName("Alice");
        updated.setMessage("Updated message");
        GreetingModel result = greetingService.update(updated);
        assertEquals("Updated message", result.getMessage());
    }

    // --- Service Tests: delete() ---

    @Test
    public void testDeleteRemovesGreeting() {
        fakeRepository.addGreeting(1L, "Alice", "Hello Alice");
        greetingService.delete("Alice");
        assertEquals(0, fakeRepository.findAll().size());
    }


    // - Test exceptions

    @Test(expected = GreetingException.class)
    public void testExceptionOnCreateForDuplicateName() {
        GreetingModel greetingModel = new GreetingModel();
        greetingModel.setName("Alice");
        greetingModel.setMessage("No message");
        greetingService.create(greetingModel);  // this will be the pre-existing record
        greetingService.create(greetingModel);  // this will trigger the exception
    }

    @Test(expected = GreetingException.class)
    public void testExceptionOnUpdateForNameNotFound() {
        GreetingModel greetingModel = new GreetingModel();
        greetingModel.setName("Nobody");
        greetingModel.setMessage("No message");
        greetingService.update(greetingModel);
    }

    // --- Fake Repository Implementation ---

    /**
     * In-memory implementation of GreetingRepository for testing.
     * Implements the interface directly, avoiding any database dependencies.
     */

    static class FakeGreetingRepository implements GreetingRepository {
        private final List<GreetingModel> greetings = new ArrayList<>();
        private Long nextId = 1L;

        public void addGreeting(Long id, String name, String message) {
            GreetingModel g = new GreetingModel();
            g.setId(id);
            g.setName(name);
            g.setMessage(message);
            greetings.add(g);
            if (id >= nextId) {
                nextId = id + 1;
            }
        }

        @Override
        public List<GreetingModel> findAll() {
            return new ArrayList<>(greetings);
        }

        @Override
        public GreetingModel findByName(String name) {
            if (!greetings.isEmpty()) {
                return greetings.stream()
                        .filter(g -> g.getName().equals(name))
                        .findFirst().get();
            } else {
                return null;
            }
        }

        @Override
        public GreetingModel save(GreetingModel greetingModel) {
            greetingModel.setId(nextId++);
            greetings.add(greetingModel);
            return greetingModel;
        }

        @Override
        public int update(GreetingModel greetingModel) {
            for (int i = 0; i < greetings.size(); i++) {
                if (greetings.get(i).getId().equals(greetingModel.getId())) {
                    greetings.set(i, greetingModel);
                    return 1;
                }
            }
            return 0;
        }

        @Override
        public int delete(String name) {
            for (int i = 0; i < greetings.size(); i++) {
                if (greetings.get(i).getName().equals(name)) {
                    greetings.remove(i);
                    return 1;
                }
            }
            return 0;
        }
    }
}
