package com.example.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.config.AppConfig;
import com.example.controller.GreetingController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.HashMap;
import java.util.Map;

public class GreetingLambdaHandler implements
        RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final AnnotationConfigApplicationContext context;
    private static final GreetingController controller;
    static {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        controller = context.getBean(GreetingController.class);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            APIGatewayProxyRequestEvent request, Context lambdaContext) {
        String method = request.getHttpMethod();
        String path = request.getPath();;
        try {
            // Route the request to the controller
            System.out.println("Method: " + method);
            switch (method) {
                case "GET":
                    System.out.println("GET path: " + path);
                    if (path.equals("/greetings")) {
                        return controller.getAllGreetings();
                    } else if (path.equals("/greeting")){
                        // must be a single get request
                        Map<String, String> queryParams = request.getQueryStringParameters();
                        if (queryParams != null) {
                            System.out.println("queryParams: " + queryParams);
                            String name = queryParams.get("name");
                            System.out.println("name: " + name);
                            if (name != null) {
                                return controller.getGreeting(name);
                            } else {
                                return buildResponse(400, "{\"error\": \"GET requires query param: name=(value)\"}");
                            }
                        } else {
                            return buildResponse(400, "{\"error\": \"GET requires /greeting?name=(value)\"}");
                        }
                    } else {
                        return buildResponse(400, "{\"error\": \"GET with unexpected path: ${path}\"}");
                    }
                case "POST":
                    return controller.createGreeting(request.getBody());
                case "PUT":
                    return controller.updateGreeting(request.getBody());
                case "DELETE":
                    Map<String, String> queryParams = request.getQueryStringParameters();
                    String name = queryParams.get("name");
                    if (name != null) {
                        return controller.deleteGreeting(name);
                    } else {
                        return buildResponse(400, "{\"error\": \"DELETE requires /greeting?name=(value)\"}");
                    }
                default:
                    return buildResponse(405, "{\"error\": \"Method not allowed\"}");
            }
        } catch (NumberFormatException e) {
            return buildResponse(400, "{\"error\": \"Invalid ID format\"}");
        } catch (Exception e) {
            lambdaContext.getLogger().log("Error: " + e.getMessage());
            return buildResponse(500, "{\"error\": \"Internal server error\"}");
        }
    }

    public static APIGatewayProxyResponseEvent buildResponse(int statusCode, String body) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(statusCode);
        response.setBody(body);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        return response;
    }
}
