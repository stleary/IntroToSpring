package com.example;

public class ErrorResponse {
	String message;
	int status;
	
	public ErrorResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}
}
