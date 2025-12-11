package com.example.demo.dto;

import java.time.LocalDate;

public class ExpenseDTO {
	private Long expenseId;
	private String expenseType;
	private double amount;
	private LocalDate expenseDate;
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
