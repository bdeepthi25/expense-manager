package com.example.demo.exception;

public class ExpenseNotFoundException extends RuntimeException {
		public ExpenseNotFoundException(String message)
		{
			super(message);
		}
}
