package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.enums.ExpenseActivity;
import com.example.demo.enums.ExpenseStatus;
import com.example.demo.exception.ExpenseAlreadyLockedException;
import com.example.demo.exception.IllegalAuditActionException;
import com.example.demo.exception.InvalidExpenseStateException;
import com.example.demo.model.ExpenseHistory;
import com.example.demo.model.Expenses;
import com.example.demo.model.Users;
import com.example.demo.repository.ChargeCodeRepository;
import com.example.demo.repository.ExpenseHistoryRepository;
import com.example.demo.repository.ExpenseRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WorkflowServiceImpl implements WorkflowService{


    private final ExpenseRepository expenseRepo;
    private final ExpenseHistoryRepository historyRepo;
    private final ChargeCodeRepository chargeCodeRepo;
    
	public WorkflowServiceImpl(ExpenseRepository expenseRepo, ExpenseHistoryRepository historyRepo, ChargeCodeRepository chargeCodeRepo) {
		
		this.expenseRepo = expenseRepo;
		this.historyRepo = historyRepo;
		this.chargeCodeRepo = chargeCodeRepo;
	}

	@Override
	public void proceed(Expenses expense, Users actor, ExpenseStatus action) 
	{
		ExpenseActivity currentActivity = expense.getCurrentActivity();
		switch(currentActivity)
		{
			case ER_MANAGER_REVIEW -> handleManagerReview(expense, actor, action);
			case ER_CHARGECODE_REVIEW -> handleChargeCodeReview(expense, actor, action);
			case ER_AUDIT_REVIEW -> handleAuditReview(expense, actor, action);
			default ->   throw new IllegalStateException(
			                  "Invalid activity for workflow: " + currentActivity
			              );
		}
		
	}


	private void handleManagerReview(Expenses expense, Users actor, ExpenseStatus action) 
	{
		 if (expense.getStatus() != ExpenseStatus.PENDING &&
			        expense.getStatus() != ExpenseStatus.RESUBMITTED) 
		 {
			        throw new InvalidExpenseStateException("Expense not eligible for manager review");
	     }
		ExpenseActivity oldActivity = expense.getCurrentActivity();
		expense.setPreviousActivity(oldActivity);
	;
		if(action == ExpenseStatus.REJECTED)
		{
			expense.setStatus(ExpenseStatus.REJECTED);
	        expense.setCurrentActivity(ExpenseActivity.ER_CREATE);
	        expense.setApprover(expense.getUsers());
		}
		else 
		{
			expense.setStatus(ExpenseStatus.PENDING);
	        expense.setCurrentActivity(ExpenseActivity.ER_CHARGECODE_REVIEW);
	        
	    	expense.setApprover(chargeCodeRepo.findByChargeCode(expense.getChargeCode())
	    										.getChargeCodeApprover());
		}
		
		saveHistory(expense, actor, action);
	}


	private void handleChargeCodeReview(Expenses expense, Users actor, ExpenseStatus action) 
	{
		 if (action != ExpenseStatus.APPROVED &&
				 action != ExpenseStatus.REJECTED) 
			 {
				        throw new InvalidExpenseStateException("Invalid Chargecode action: " + action);
			 } 
		 
			ExpenseActivity oldActivity = expense.getCurrentActivity();
			expense.setPreviousActivity(oldActivity);
		if (action == ExpenseStatus.REJECTED) {
	        expense.setStatus(ExpenseStatus.REJECTED);
	        expense.setCurrentActivity(ExpenseActivity.ER_CREATE);
	        expense.setApprover(expense.getUsers());
	    } else {
	        expense.setStatus(ExpenseStatus.PENDING);
	        expense.setCurrentActivity(ExpenseActivity.ER_AUDIT_REVIEW);
	        expense.setApprover(null);
	    }

	    saveHistory(expense, actor, action);
	}
	
	private void handleAuditReview(
	        Expenses expense,
	        Users auditor,
	        ExpenseStatus action
	) 
	{
		 // Validate allowed action
		 if (action != ExpenseStatus.APPROVED &&
			 action != ExpenseStatus.REJECTED) 
		 {
			   throw new IllegalAuditActionException("Invalid audit action: " + action);
		 } 
		ExpenseActivity oldActivity = expense.getCurrentActivity();
		expense.setPreviousActivity(oldActivity);
	    // If already locked, prevent others
	    if (expense.getLockedBy() != null &&
	        !expense.getLockedBy().getId().equals(auditor.getId())) 
	    {
	    	throw new ExpenseAlreadyLockedException("This expense is already being reviewed by another auditor");
	    }

	    // Lock it permanently when action happens
	    expense.setLockedBy(auditor);
	    expense.setLockedAt(LocalDateTime.now());

	    if (action == ExpenseStatus.REJECTED) 
	    {
	        expense.setStatus(ExpenseStatus.REJECTED);
	        expense.setCurrentActivity(ExpenseActivity.ER_CREATE);
	        expense.setApprover(expense.getUsers());
	    } 
	    else 
	    {
	        expense.setStatus(ExpenseStatus.APPROVED);
	        expense.setCurrentActivity(ExpenseActivity.ER_EXPORT);
	        expense.setApprover(null); 
	    }
	    expense.setLockedBy(null);
	    expense.setLockedAt(null);

	    saveHistory(expense, auditor, action);
	}

	private void saveHistory(Expenses expense, Users actor, ExpenseStatus action) {
		ExpenseHistory history = new ExpenseHistory();
	    history.setExpense(expense);
	    history.setPreviousActivity(expense.getPreviousActivity());
	    history.setCurrentActivity(expense.getCurrentActivity());
	    history.setAction(action);
	    history.setActionBy(expense.getUsers());
	    if (expense.getPreviousActivity() != ExpenseActivity.ER_EXPORT) {
	        history.setReviewer(actor);
	    }
	    history.setActionDate(LocalDateTime.now());

	    historyRepo.save(history);
		
	}

}
