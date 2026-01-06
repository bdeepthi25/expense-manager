package com.example.demo.exception;

public class InvalidExpenseStateException extends RuntimeException {
	public InvalidExpenseStateException(String message)
	{
		super(message);
	}
}
