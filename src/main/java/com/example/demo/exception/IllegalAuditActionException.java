package com.example.demo.exception;

public class IllegalAuditActionException extends RuntimeException {

	public IllegalAuditActionException(String message)
	{
		super(message);
	}
}
