package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class GreetingController {
    private final GreetingService greetingService;

    @Autowired
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    /**
     * Handles validation errors from @Valid.
     * Returns a 400 response with details about which fields failed.
     *
     * Note: @Valid throws MethodArgumentNotValidException BEFORE the method
     * body executes, so a local try/catch cannot catch it. This handler
     * method catches it at the controller level instead.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", "Validation Failed");
        body.put("messages", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
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
    public ResponseEntity<?> createGreeting(@Valid @RequestBody GreetingModel greetingModel) {
    	try {
	        GreetingModel saved = greetingService.create(greetingModel);
	        URI location = URI.create("/greetings/name=" + saved.getName());
	        return ResponseEntity
	                .created(location)
	                .body(saved);
    	} catch (GreetingException exception) {
    		return ResponseEntity
    				.status(HttpStatus.BAD_REQUEST)
    				.body(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    				
    	}
    }

    /**
     * PUT /greeting
     * Updates an existing greeting.
     * Request body should be JSON: {"name": "Alice", "message": "Updated message!"}
     */
    @PutMapping("/greeting")
    public ResponseEntity<?> updateGreeting(@Valid @RequestBody GreetingModel greetingModel) {
    	try {
    		GreetingModel findGreetingModel = greetingService.update(greetingModel);
    		return ResponseEntity
    				.status(HttpStatus.OK)
    				.body(findGreetingModel);
    	} catch (GreetingException exception) {
    		return ResponseEntity
    				.status(HttpStatus.BAD_REQUEST)
    				.body(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    				
    	}
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


