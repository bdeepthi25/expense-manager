package com.example.demo.exception;

public class InvalidDateException extends RuntimeException{

	public InvalidDateException(String msg)
	{
		super(msg);
	}
}
