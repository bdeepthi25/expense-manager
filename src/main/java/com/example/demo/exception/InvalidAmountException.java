package com.example.demo.exception;

public class InvalidAmountException extends RuntimeException{
	public InvalidAmountException(String message)
	{
		super(message);
	}
}
