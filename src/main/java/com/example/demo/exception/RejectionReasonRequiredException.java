package com.example.demo.exception;

public class RejectionReasonRequiredException extends RuntimeException{
	public RejectionReasonRequiredException(String msg) {
		super(msg);
	}
}
