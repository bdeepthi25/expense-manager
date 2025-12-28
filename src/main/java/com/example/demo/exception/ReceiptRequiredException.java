package com.example.demo.exception;

public class ReceiptRequiredException extends RuntimeException{
	public ReceiptRequiredException(String msg) {
		super(msg);
	}
	
}
