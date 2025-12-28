package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.enums.ExpenseStatus;

public class ExpenseHistoryDTO {

	private LocalDateTime date;
    private String performedBy;
    private ExpenseStatus action;
    private String comment;

    public ExpenseHistoryDTO(LocalDateTime date, String performedBy, ExpenseStatus action, String comment) {
        this.date = date;
        this.performedBy = performedBy;
        this.action = action;
        this.comment = comment;
    }

	public LocalDateTime getDate() {
		return date;
	}

	public String getPerformedBy() {
		return performedBy;
	}

	public ExpenseStatus getAction() {
		return action;
	}

	public String getComment() {
		return comment;
	}
    
    
}
