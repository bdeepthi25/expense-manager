package com.example.demo.exception;

public class UnauthorizedExpenseApprovalException extends RuntimeException{

	public UnauthorizedExpenseApprovalException(String msg) 
	{
		super(msg);
	}

}
