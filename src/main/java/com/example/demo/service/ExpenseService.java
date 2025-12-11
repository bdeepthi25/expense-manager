package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ExpenseDTO;
import com.example.demo.model.Expenses;
import com.example.demo.model.Users;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.UserRepository;

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
			return "Please Give valid Amount";
		}
		if(expenseDto.getExpenseDate().isAfter( LocalDate.now()))
		{
			return "Do not give future Date, Please fill with past Dates";
		}
		
		Optional<Users> user = userRepo.findById(expenseDto.getUserId());
		if (user.isEmpty()) {
            return "User not found";
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
