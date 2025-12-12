package com.example.demo.exception;

public class DuplicateExpenseException extends RuntimeException{
	public DuplicateExpenseException(String message) {
        super(message);
    }
}
