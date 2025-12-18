package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.dto.ExpenseRequestDTO;
import com.example.demo.dto.UpdateExpenseRequestDTO;
import com.example.demo.model.Users;
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
	
    @PostMapping
    public ResponseEntity<?> createExpense(
            @Valid @RequestBody ExpenseRequestDTO expDto) {

        return new ResponseEntity<>(
                expService.createExpense(expDto),
                HttpStatus.CREATED
        );
    }
	
//	Get My Docs
//	@GetMapping("/expenses/user/{userId}")
//	public ResponseEntity<?> getMyDocs(@PathVariable Long userId)
//	{
//		return ResponseEntity.ok( expService.getExpensesOfUser(userId) );
//	}
	@GetMapping
	public ResponseEntity<?> getMyExpenses() {
	    return ResponseEntity.ok(expService.getMyExpenses());
	}

//	Update the Expense of particular User
	@PutMapping("/{expenseId}")
	public ResponseEntity<?> updateExpense(
			@PathVariable Long expenseId,
			@Valid @RequestBody UpdateExpenseRequestDTO updateExpDto
			)
	{
		return ResponseEntity.ok(
				expService.updateExpense(expenseId, updateExpDto));
				
	}
	
//	Delete the Expense of User
	@DeleteMapping("/{expenseId}")
	public ResponseEntity<?> deleteExpense(@PathVariable Long expenseId)
	{
		expService.deleteExpense(expenseId );
		return ResponseEntity.ok("Expense deleted successfully");
	}
	
	
//	Pagination
	
	@GetMapping("/paginated")
	public ResponseEntity<?> getExpensesPaginated(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "expenseDate") String sortBy,
			@RequestParam(defaultValue = "desc" ) String direction
			)
	{
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
		return ResponseEntity.ok(
				expService.getExpensesOfUserPaginated(user.getId(), page, size, sortBy, direction)
				) ;
				
	}
	
	@PostMapping("/test")
	public String test() {
	    return "POST WORKS";
	}

	
}
