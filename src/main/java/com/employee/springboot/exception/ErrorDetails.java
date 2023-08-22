package com.employee.springboot.exception;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorDetails {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Asia/Kolkata")
	private Date timestamp;
	private String message;
	private String details;
	
	public ErrorDetails(Date date, String message, String details) {
		super();
		this.timestamp = date;
		this.message = message;
		this.details = details;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getDetails() {
		return details;
	}
	

}
