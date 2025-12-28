package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.enums.ExpenseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
	private LocalDate expenseDate;  // Date of the actual expense
	private LocalDateTime submittedDate;  // Date when the expense was submitted
	private LocalDateTime approvedDate; // Date when the expense was approved/rejected
	@Enumerated(EnumType.STRING)
	@Column(name="status",nullable = false)
	private ExpenseStatus  status ;

	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "userId")
	private Users users;
//		→ This means the column created is user_id, not the full Users object
	
	@ManyToOne
	@JoinColumn(name = "approver_id")
	private Users approver;
	
	@ManyToOne
	@JoinColumn(name = "approved_by")
	private Users approvedBy;
	
	@Column(length = 500)
	private String rejectionReason;

	
	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public Users getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Users approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDateTime getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(LocalDateTime submittedDate) {
		this.submittedDate = submittedDate;
	}

	public LocalDateTime getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDateTime approvedDate) {
		this.approvedDate = approvedDate;
	}



	public ExpenseStatus getStatus() {
		return status;
	}

	public void setStatus(ExpenseStatus status) {
		this.status = status;
	}

	public Users getApprover() {
		return approver;
	}

	public void setApprover(Users approver) {
		this.approver = approver;
	}

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
