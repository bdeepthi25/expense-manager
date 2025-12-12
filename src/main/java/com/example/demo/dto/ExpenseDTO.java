package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class ExpenseDTO {
	
	@NotBlank
	private Long expenseId;
	@NotBlank
	private String expenseType;
	@Positive
	private double amount;
	@PastOrPresent
	private LocalDate expenseDate;
	@NotNull
	private Long userId;
	
	public ExpenseDTO(Long expenseId, String expenseType, double amount, LocalDate expenseDate, Long userId) {
		
		this.expenseId = expenseId;
		this.expenseType = expenseType;
		this.amount = amount;
		this.expenseDate = expenseDate;
		this.userId = userId;
	}
	
	public ExpenseDTO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ExpenseDTO [expenseId=" + expenseId + ", expenseType=" + expenseType + ", amount=" + amount
				+ ", expenseDate=" + expenseDate + ", userId=" + userId + "]";
	}

	public Long getExpenseId() {
		return expenseId;
	}
	public void setExpenseId(Long expenseId) {
		this.expenseId = expenseId;
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
