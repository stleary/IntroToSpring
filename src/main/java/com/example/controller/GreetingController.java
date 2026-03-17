package com.example.controller;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.handler.GreetingLambdaHandler;
import com.example.model.GreetingModel;
import com.example.service.GreetingService;
import com.google.gson.Gson;
import java.util.List;

public class GreetingController {
    private final GreetingService greetingService;
    private final Gson gson = new Gson();

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public APIGatewayProxyResponseEvent getAllGreetings() {
        List<GreetingModel> greetings = greetingService.findAll();
        return GreetingLambdaHandler.buildResponse(200, gson.toJson(greetings));
    }

    public APIGatewayProxyResponseEvent getGreeting(String name) {
        GreetingModel greeting = greetingService.findByName(name);
        if (greeting != null) {
            return GreetingLambdaHandler.buildResponse(200, gson.toJson(greeting));
        }
        return GreetingLambdaHandler.buildResponse(404,
                "{\"error\": \"Greeting not found with name: " + name + "\"}");
    }

    public APIGatewayProxyResponseEvent createGreeting(String requestBody) {
        GreetingModel input = gson.fromJson(requestBody, GreetingModel.class);
        // Manual validation (replaces @Valid/@NotBlank)
        String error = validateGreeting(input);
        if (error != null) {
            return GreetingLambdaHandler.buildResponse(400, "{\"error\": \"" + error + "\"}");
        }
        GreetingModel created = greetingService.create(input);
        return GreetingLambdaHandler.buildResponse(201, gson.toJson(created));
    }

    public APIGatewayProxyResponseEvent updateGreeting(String requestBody) {
        GreetingModel input = gson.fromJson(requestBody, GreetingModel.class);
        String error = validateGreeting(input);
        if (error != null) {
            return GreetingLambdaHandler.buildResponse(400, "{\"error\": \"" + error + "\"}");
        }
        GreetingModel updated = greetingService.update(input);
        if (updated != null) {
            return GreetingLambdaHandler.buildResponse(200, gson.toJson(updated));
        }
        return GreetingLambdaHandler.buildResponse(404,
                "{\"error\": \"Greeting not found with name: " + input.getName() + "\"}");
    }

    public APIGatewayProxyResponseEvent deleteGreeting(String name) {
        int deleted = greetingService.delete(name);
        if (deleted == 1) {
            return GreetingLambdaHandler.buildResponse(204, "");
        }
        return GreetingLambdaHandler.buildResponse(404,
                "{\"error\": \"Greeting not found with name: " + name + "\"}");
    }

    private String validateGreeting(GreetingModel greeting) {
        if (greeting == null) return "Request body is required";
        if (greeting.getName() == null || greeting.getName().trim().isEmpty()) {
            return "name is required";
        }

        if (greeting.getMessage() == null || greeting.getMessage().trim().isEmpty()) {
            return "message is required";
        }

        if (greeting.getName().length() > 100) {
            return "name must be 100 characters or fewer";
        }

        if (greeting.getMessage().length() > 500) {
            return "message must be 500 characters or fewer";
        }

        return null;
    }
}
