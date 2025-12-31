package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public class ExpenseRejectionDTO {

	@NotNull
	private String rejectionReason;

	public String getRejectionReason() {
		return rejectionReason;
	}

	public ExpenseRejectionDTO(@NotNull String rejectionReason) {
		
		this.rejectionReason = rejectionReason;
	}
	
	
}
