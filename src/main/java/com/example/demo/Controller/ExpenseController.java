package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.dto.ExpenseRequestDTO;
import com.example.demo.dto.UpdateExpenseRequestDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final UserRepository userRepository;

	@Autowired
	private ExpenseService expService;

    ExpenseController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
//	Create Expense
	@PostMapping
	public ResponseEntity<String> createExpense(@Valid @RequestBody ExpenseRequestDTO expenseDto)
	{
		 expService.createExpense(expenseDto);
		 return ResponseEntity.status(HttpStatus.CREATED)
				 .body("Expense created Successfully");
	}
	
//	Get My Docs
	@GetMapping("/expenses/user/{userId}")
	public ResponseEntity<?> getMyDocs(@PathVariable Long userId)
	{
		return ResponseEntity.ok( expService.getExpensesOfUser(userId) );
	}
	
//	Update the Expense of particular User
	@PutMapping("/{expenseId}/user/{userId}")
	public ResponseEntity<?> updateExpense(
			@PathVariable Long expenseId,
			@PathVariable Long userId,
			@Valid @RequestBody UpdateExpenseRequestDTO updateExpDto
			)
	{
		return ResponseEntity.ok(
				expService.updateExpense(expenseId, userId, updateExpDto));
				
	}
	
//	Delete the Expense of User
	@DeleteMapping("/{expenseId}/user/{userId}")
	public ResponseEntity<?> deleteExpense(
			@PathVariable Long expenseId,
			@PathVariable Long userId
			)
	{
		expService.deleteExpense(expenseId , userId);
		return ResponseEntity.ok("Expense deleted successfully");
	}
	
}
