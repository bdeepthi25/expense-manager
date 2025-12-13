package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
//(for create/update)
public class ExpenseRequestDTO {
	

	@NotBlank(message = "Expense type is required")
	private String expenseType;
	@Positive(message = "Amount must be greater than 0")
	private double amount;
	@NotNull(message = "Expense date is required")
	@PastOrPresent(message = "Expense date cannot be in future")
	private LocalDate expenseDate;
	@NotNull(message = "UserId is required")
	private Long userId;
	
	public ExpenseRequestDTO( String expenseType, double amount, LocalDate expenseDate, Long userId) {
		
		this.expenseType = expenseType;
		this.amount = amount;
		this.expenseDate = expenseDate;
		this.userId = userId;
	}
	
	public ExpenseRequestDTO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ExpenseDTO [  expenseType=" + expenseType + ", amount=" + amount
				+ ", expenseDate=" + expenseDate + ", userId=" + userId + "]";
	}

	
	
	public String getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public LocalDate getExpenseDate() {
		return expenseDate;
	}
	public void setExpenseDate(LocalDate expenseDate) {
		this.expenseDate = expenseDate;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
