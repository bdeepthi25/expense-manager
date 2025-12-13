package com.example.demo.service;

import java.time.LocalDate;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import com.example.demo.dto.ExpenseRequestDTO;
import com.example.demo.dto.ExpenseResponseDTO;
import com.example.demo.dto.UpdateExpenseRequestDTO;
import com.example.demo.model.Expenses;
import com.example.demo.model.Users;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.UserRepository;

import jakarta.validation.Valid;

import com.example.demo.exception.DuplicateExpenseException;
import com.example.demo.exception.ExpenseNotFoundException;
import com.example.demo.exception.InvalidAmountException;
import com.example.demo.exception.InvalidDateException;
import com.example.demo.exception.UnauthorizedAccessException;
import com.example.demo.exception.UserNotFoundException;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepo;
	@Autowired
	private  UserRepository userRepo;
	
	public ExpenseService(ExpenseRepository expenseRepo) {
		this.expenseRepo = expenseRepo;
	}
	
	public ExpenseResponseDTO createExpense(ExpenseRequestDTO expenseDto)
	{
//		removed as there are validations in ExpenseRequestDTO
//		if(expenseDto.getAmount() <= 0)
//		{
//			throw new InvalidAmountException("Amount must be greater than 0");
//		}
//		if(expenseDto.getExpenseDate().isAfter( LocalDate.now()))
//		{
//			throw new InvalidDateException( "Do not give future Date, Please fill with past Dates");
//		}
		
		Users user = userRepo.findById(expenseDto.getUserId())
				.orElseThrow( () -> new UserNotFoundException( "User not found"));
		
		
		boolean exists = expenseRepo.existsByExpenseTypeAndAmountAndExpenseDateAndUserId(
				expenseDto.getExpenseType(), 
				expenseDto.getAmount(), 
				expenseDto.getExpenseDate(), 
				expenseDto.getUserId());
		if(exists)
		{
			throw new DuplicateExpenseException("Same Expense already exists");
		}
		Expenses newExpense = new Expenses();
//		, expenseDto.getExpenseType(), expenseDto.getAmount(), expenseDto.getExpenseDate(), userRepo.findById(expenseDto.getUserId()) );
		newExpense.setExpenseType(expenseDto.getExpenseType());
		newExpense.setAmount(expenseDto.getAmount());
		newExpense.setExpenseDate(expenseDto.getExpenseDate());
		newExpense.setUsers(user);
		
		expenseRepo.save( newExpense);
		return new ExpenseResponseDTO(newExpense.getExpenseId(),
				newExpense.getExpenseType(), 
				newExpense.getAmount(), 
				newExpense.getExpenseDate());
	}

	public List<ExpenseResponseDTO> getExpensesOfUser(Long userId) {
				
		userRepo.findById(userId)
				.orElseThrow( () -> new  UserNotFoundException("User not found"));
		
		return	expenseRepo.findByUsers_Id(userId)
				.stream()
				.map(e -> new ExpenseResponseDTO(
						e.getExpenseId(),
						e.getExpenseType(),
						e.getAmount(),
						e.getExpenseDate()
						))
				.toList();
			
	
	}

	public ExpenseResponseDTO updateExpense(Long expenseId, Long userId, @Valid UpdateExpenseRequestDTO updateExpDto) {
//		Validate user
		Users user =  userRepo.findById(userId)
						.orElseThrow( () -> new UserNotFoundException("User not found"));
//		Validate expense
		Expenses expense = expenseRepo.findById(expenseId)
								.orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
//		Authorization check
		if(!expense.getUsers().getId().equals(user.getId()))
		{
			throw new UnauthorizedAccessException("YOu can not update this expense");
		}
		
		boolean exists = expenseRepo.existsByExpenseTypeAndAmountAndExpenseDateAndUsers_UserIdExpenseIdNot(
				updateExpDto.getExpenseType(),
				updateExpDto.getAmount(),
				updateExpDto.getExpenseDate(),
				userId,
				expenseId
				);
		
		if(exists)
		{
			throw new DuplicateExpenseException("Duplicate expense exists");
		}
		
		expense.setExpenseType(updateExpDto.getExpenseType());
		expense.setAmount(updateExpDto.getAmount());
		expense.setExpenseDate(updateExpDto.getExpenseDate());
		
		expenseRepo.save(expense);
		
		return new ExpenseResponseDTO(expense.getExpenseId(), 
										expense.getExpenseType(),
										expense.getAmount(), 
										expense.getExpenseDate());
	}

	public void deleteExpense(Long expenseId, Long userId) {
		
//		Validate user
		Users user =  userRepo.findById(userId)
						.orElseThrow( () -> new UserNotFoundException("User not found"));
//		Validate expense
		Expenses expense = expenseRepo.findById(expenseId)
								.orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
//		Authorization check
		if(!expense.getUsers().getId().equals(user.getId()))
		{
			throw new UnauthorizedAccessException("YOu can not delete this expense");
		}
//       Delete
	    expenseRepo.delete(expense);
	}
}
