package com.example.demo.exception;

public class ExpenseAlreadyProcessedException extends RuntimeException{

	public ExpenseAlreadyProcessedException(String msg) {
		super(msg);
	}
	
	

}
