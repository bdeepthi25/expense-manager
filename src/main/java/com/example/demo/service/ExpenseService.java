package com.example.demo.service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ExpenseDTO;
import com.example.demo.model.Expenses;
import com.example.demo.model.Users;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.exception.DuplicateExpenseException;
import com.example.demo.exception.InvalidAmountException;
import com.example.demo.exception.InvalidDateException;
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
	
	public String createExpense(ExpenseDTO expenseDto)
	{
		if(expenseDto.getAmount() <= 0)
		{
			throw new InvalidAmountException("Amount must be greater than 0");
		}
		if(expenseDto.getExpenseDate().isAfter( LocalDate.now()))
		{
			throw new InvalidDateException( "Do not give future Date, Please fill with past Dates");
		}
		
		Optional<Users> user = userRepo.findById(expenseDto.getUserId());
		if (user.isEmpty()) {
            throw new UserNotFoundException( "User not found");
        }
		
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
		newExpense.setUsers(user.get());
		
		expenseRepo.save( newExpense);
		
		return "Expense saved successfully";
	}

	public ResponseEntity<?> getExpensesOfUser(Long userId) {

		Optional<Users> user = userRepo.findById(userId);
		if (user.isEmpty()) {
			return	ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body( "User not found");
        }
		else {
			List<Expenses> userExpenses= expenseRepo.findByUsers_Id(userId);
			return ResponseEntity.ok(userExpenses);
		}
	
	}
}
