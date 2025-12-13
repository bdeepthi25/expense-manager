package com.example.demo.dto;

import java.time.LocalDate;

public class ExpenseResponseDTO {

	private Long expenseId;
	private String expenseType;
	private double amount;
	private LocalDate expenseDate;
	
	
	public ExpenseResponseDTO(Long expenseId, String expenseType, double amount, LocalDate expenseDate) {
		super();
		this.expenseId = expenseId;
		this.expenseType = expenseType;
		this.amount = amount;
		this.expenseDate = expenseDate;
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
	
}
