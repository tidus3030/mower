package com.publicis.sapient.mower.exception;

public class ParsingException extends RuntimeException {
	
	public ParsingException(String message) {
		super(message);
	}
	
	public ParsingException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
