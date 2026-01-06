package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.enums.ExpenseActivity;
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
public class ExpenseHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "expense_id")
	private Expenses expense;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users actionBy;
	
	@ManyToOne
	@JoinColumn(name = "reviewer_id")
	private Users reviewer;

	
    public Users getReviewer() {
		return reviewer;
	}
	public void setReviewer(Users reviewer) {
		this.reviewer = reviewer;
	}
	@Enumerated(EnumType.STRING)
    private ExpenseActivity previousActivity;
    
    @Enumerated(EnumType.STRING)
    private ExpenseActivity currentActivity;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "action")
	private ExpenseStatus action;
	
	private LocalDateTime actionDate;
	private String comment;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ExpenseActivity getPreviousActivity() {
		return previousActivity;
	}
	public void setPreviousActivity(ExpenseActivity previousActivity) {
		this.previousActivity = previousActivity;
	}
	public ExpenseActivity getCurrentActivity() {
		return currentActivity;
	}
	public void setCurrentActivity(ExpenseActivity currentActivity) {
		this.currentActivity = currentActivity;
	}
	public Expenses getExpense() {
		return expense;
	}
	public void setExpense(Expenses expense) {
		this.expense = expense;
	}
	public Users getActionBy() {
		return actionBy;
	}
	public void setActionBy(Users actionBy) {
		this.actionBy = actionBy;
	}
	public ExpenseStatus getAction() {
		return action;
	}
	public void setAction(ExpenseStatus action) {
		this.action = action;
	}
	public LocalDateTime getActionDate() {
		return actionDate;
	}
	public void setActionDate(LocalDateTime actionDate) {
		this.actionDate = actionDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
