package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class UpdateExpenseRequestDTO {

			@NotBlank(message = "Expense type is required")
			private String expenseType;
			@Positive(message = "Amount must be greater than 0")
			private double amount;
			@PastOrPresent(message = "Expense date cannot be in future")
			@NotNull(message = "Expense date is required") 
			private LocalDate expenseDate;
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
