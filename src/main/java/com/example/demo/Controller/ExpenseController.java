package com.example.demo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ExpenseRejectionDTO;
import com.example.demo.dto.ExpenseRequestDTO;
import com.example.demo.dto.ExpenseResponseDTO;
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
	@GetMapping
	public ResponseEntity<?> getMyExpenses() {
		List<ExpenseResponseDTO> expensesOfUser = expService.getMyExpenses();
		if(expensesOfUser.isEmpty())
		{
			return ResponseEntity.ok( Map.of("message", "You have no submitted documents to display"));
		}
	    return ResponseEntity.ok(expensesOfUser);
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
	

	@PostMapping("/{expenseId}/approve")
	public ResponseEntity<?> approveExpense(@PathVariable Long expenseId)
	{
		expService.approveExpense(expenseId);
		return ResponseEntity.ok("Expense approved successfully");
	}
	
	@PostMapping("/{expenseId}/reject")
	public ResponseEntity<?> rejectExpense(@PathVariable Long expenseId, 
			@RequestBody ExpenseRejectionDTO expRejectDto,
			@AuthenticationPrincipal UserDetails userDetails)
	{
		 expService.rejectExpense(expenseId, expRejectDto, userDetails);
		 return ResponseEntity.ok("Expense rejected successfully");
	}
	
//	Write Resubmit
	@PostMapping("/{expenseId}/resubmit")
	public ResponseEntity<?> resubmitExpense(@PathVariable Long expenseId,
			@RequestBody ExpenseRequestDTO expRequestDto,
			@AuthenticationPrincipal UserDetails userDetails)
	
	{
		expService.resubmitExpense(expenseId, expRequestDto, userDetails);
		return ResponseEntity.ok("Expense Resubmitted successfully");
	}
	
	@GetMapping("\review")
	public ResponseEntity<?> getDocumentsForMyReview(@RequestParam(defaultValue = "0") int page,
													@RequestParam(defaultValue = "5") int size	)
	{
		return ResponseEntity.ok(expService.getDocumentsForMyReview(page, size));
	}
	
	@GetMapping("/{expenseId}/get-history")
	public ResponseEntity<?> getExpenseHistory(@PathVariable Long expenseId)
	{
		return ResponseEntity.ok(expService.getExpenseHistory(expenseId));
	}
	
}
