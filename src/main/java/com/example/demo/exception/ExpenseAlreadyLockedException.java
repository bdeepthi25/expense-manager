package com.example.demo.exception;

public class ExpenseAlreadyLockedException extends RuntimeException {

	public ExpenseAlreadyLockedException(String message)
	{
		super(message);
	}
}
