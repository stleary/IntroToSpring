package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FormalGreetingServiceTest {
    @Mock
    private GreetingRepository repository;


    @InjectMocks
    private FormalGreetingService service;
    private GreetingModel alice;
    private GreetingModel bob;


    @BeforeEach
    void setUp() {
        alice = new GreetingModel("Alice", "Good morning, Alice!");
        bob = new GreetingModel("Bob", "Good morning, Bob!");
    }


    @Test
    void findAll_returnsAllGreetings() {
        when(repository.findAll()).thenReturn(Arrays.asList(alice, bob));
        List<GreetingModel> result = service.findAll();
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(repository, times(1)).findAll();
    }


    @Test
    void findByName_existingName_returnsGreeting() {
        when(repository.findByName("Alice")).thenReturn(alice);
        GreetingModel result = service.findByName("Alice");
        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertEquals("Good morning, Alice!", result.getMessage());
    }


    @Test
    void findByName_nonExistingName_returnsNull() {
        when(repository.findByName("Nobody")).thenReturn(null);
        GreetingModel result = service.findByName("Nobody");
        assertNull(result);
    }


    @Test
    void save_delegatesToRepository() {
        when(repository.save(any(GreetingModel.class))).thenReturn(alice);
        GreetingModel result = service.create(alice);
        assertEquals("Alice", result.getName());
        verify(repository).save(alice);
    }


    @Test
    void deleteByName_delegatesToRepository() {
        when(repository.delete("Alice")).thenReturn(1);
        service.delete("Alice");
        verify(repository, times(1)).delete("Alice");
    }
}