package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.enums.ExpenseStatus;

public class ExpenseResponseDTO {

	private Long expenseId;
	private String expenseType;
	private double amount;
	private LocalDate expenseDate;
	private LocalDateTime submittedDate;
	
	private ExpenseStatus status;
	private String approverEmail;
	private LocalDateTime approvedDate;
	
	
	public ExpenseResponseDTO(Long expenseId, String expenseType, double amount, LocalDate expenseDate,
			LocalDateTime submittedDate, ExpenseStatus status, String approverEmail, LocalDateTime approvedDate) {
		super();
		this.expenseId = expenseId;
		this.expenseType = expenseType;
		this.amount = amount;
		this.expenseDate = expenseDate;
		this.submittedDate = submittedDate;
		this.status = status;
		this.approverEmail = approverEmail;
		this.approvedDate = approvedDate;
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
	public LocalDateTime getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(LocalDateTime submittedDate) {
		this.submittedDate = submittedDate;
	}



	public ExpenseStatus getStatus() {
		return status;
	}

	public void setStatus(ExpenseStatus status) {
		this.status = status;
	}

	public String getApproverEmail() {
		return approverEmail;
	}

	public void setApproverEmail(String approverEmail) {
		this.approverEmail = approverEmail;
	}
	public LocalDateTime getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(LocalDateTime approvedDate) {
		this.approvedDate = approvedDate;
	}
}
