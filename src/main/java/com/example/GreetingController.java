package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
public class GreetingController {
    private final GreetingService greetingService;

    @Autowired
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    /**
     * GET /greetings
     * Returns all greetings.
     */
    @GetMapping("/greetings")
    public List<GreetingModel> getAllGreetings() {
        return greetingService.findAll();
    }

    /**
     * GET /greeting/name=myName
     * Returns a single greeting by name.
     */
    @GetMapping("/greeting")
    public GreetingModel getGreeting(@RequestParam(value="name") String name) {
        return greetingService.findByName(name);
    }

    /**
     * POST /greeting
     * Creates a new greeting.
     * Request body should be JSON: {"name": "Alice", "message": "Hello, Alice!"}
     * Returns 201 Created with Location header pointing to the new resource.
     */
    @PostMapping("/greeting")
    public ResponseEntity<GreetingModel> createGreeting(@RequestBody GreetingModel greetingModel) {
        GreetingModel saved = greetingService.create(greetingModel);
        URI location = URI.create("/greetings/name=" + saved.getName());
        return ResponseEntity
                .created(location)
                .body(saved);
    }

    /**
     * PUT /greeting
     * Updates an existing greeting.
     * Request body should be JSON: {"name": "Alice", "message": "Updated message!"}
     */
    @PutMapping("/greeting")
    public GreetingModel updateGreeting(@RequestBody GreetingModel greetingModel) {
        return greetingService.update(greetingModel);
    }

    /**
     * DELETE /greeting?name=myName
     * Deletes a greeting.
     * Returns 204 No Content on success.
     */
    @DeleteMapping("/greeting")
    public ResponseEntity<Void> deleteGreeting(@RequestParam(value="name") String name) {
        greetingService.delete(name);
        return ResponseEntity.noContent().build();
    }
}


