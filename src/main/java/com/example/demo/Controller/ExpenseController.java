package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ExpenseDTO;
import com.example.demo.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

	@Autowired
	private ExpenseService expService;
	
//	Create Expense
	@PostMapping
	public String createExpense(@RequestBody ExpenseDTO expenseDto)
	{
		return expService.createExpense(expenseDto);
		
	}
	
//	Get My Docs
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getMyDocs(@PathVariable Long userId)
	{
		return expService.getExpensesOfUser(userId);
	}
}
