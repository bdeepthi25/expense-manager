package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Expenses {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long expenseId;
	private String expenseType;
	private double amount;
	private LocalDate expenseDate;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private Users users;
//		→ This means the column created is user_id, not the full Users object
	
	
	
	public Expenses(Long expenseId, String expenseType, double amount, LocalDate expenseDate, Users users) {
		super();
		this.expenseId = expenseId;
		this.expenseType = expenseType;
		this.amount = amount;
		this.expenseDate = expenseDate;
		this.users = users;
	}

	public Expenses() {
		// TODO Auto-generated constructor stub
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

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	
}
