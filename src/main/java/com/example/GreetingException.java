package com.example;

public class GreetingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GreetingException(String msg) {
        super(msg);
    }
}