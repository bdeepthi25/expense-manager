package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.enums.ExpenseStatus;
import com.example.demo.model.Expenses;
import com.example.demo.model.Users;

public interface WorkflowService {
	void proceed(
	        Expenses expense,
	        Users actor,
	        ExpenseStatus action   // APPROVED or REJECTED
	    );
}
