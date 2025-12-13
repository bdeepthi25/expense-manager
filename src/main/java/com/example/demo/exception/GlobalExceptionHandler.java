package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidAmountException.class)
	public ResponseEntity<?> handleInvalidAmount(InvalidAmountException ex)
	{
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(InvalidDateException.class)
	public ResponseEntity<?> handleInvalidDate(InvalidDateException ex)
	{
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNOtFound(UserNotFoundException ex)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(DuplicateExpenseException.class)
	public ResponseEntity<?> handleDuplicateExpense(DuplicateExpenseException ex)
	{
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	
	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<?> handleDuplicateUser(DuplicateUserException ex)
	{
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<?> handleInvalidCredentials(InvalidCredentialsException ex)
	{
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}
	
	@ExceptionHandler(ExpenseNotFoundException.class)
	public ResponseEntity<?> handleInvalidExpense(ExpenseNotFoundException ex)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<?> handleUnauthorizedException(UnauthorizedAccessException ex)
	{
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
		
	}
}
