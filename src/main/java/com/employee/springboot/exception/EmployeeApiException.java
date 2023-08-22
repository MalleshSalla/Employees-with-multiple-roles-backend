package com.employee.springboot.exception;

import org.springframework.http.HttpStatus;

public class EmployeeApiException extends RuntimeException {
	
	private HttpStatus httpStatus;
	private String message;

	public EmployeeApiException(HttpStatus httpStatus, String message) {
		super();
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public EmployeeApiException(String message, HttpStatus httpStatus, String message1) {
		super(message);
		this.httpStatus = httpStatus;
		this.message = message1;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}

}
