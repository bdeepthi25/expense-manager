package com.example.demo.service;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ExpenseHistoryDTO;
import com.example.demo.dto.ExpenseRejectionDTO;
import com.example.demo.dto.ExpenseRequestDTO;
import com.example.demo.dto.ExpenseResponseDTO;
import com.example.demo.dto.UpdateExpenseRequestDTO;
import com.example.demo.enums.ExpenseStatus;
import com.example.demo.model.ExpenseHistory;
import com.example.demo.model.ExpenseReceipt;
import com.example.demo.model.Expenses;
import com.example.demo.model.Users;
import com.example.demo.repository.ExpenseHistoryRepository;
import com.example.demo.repository.ExpenseReceiptRepository;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.UserRepository;


import jakarta.validation.Valid;

import com.example.demo.exception.DuplicateExpenseException;
import com.example.demo.exception.ExpenseAlreadyProcessedException;
import com.example.demo.exception.ExpenseNotFoundException;
import com.example.demo.exception.InvalidAmountException;
import com.example.demo.exception.InvalidDateException;
import com.example.demo.exception.ReceiptRequiredException;
import com.example.demo.exception.RejectionReasonRequiredException;
import com.example.demo.exception.UnauthorizedAccessException;
import com.example.demo.exception.UnauthorizedExpenseApprovalException;
import com.example.demo.exception.UserNotFoundException;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepo;
	@Autowired
	private  UserRepository userRepo;
	
	
	
	@Autowired
	private ExpenseHistoryRepository expHistoryRepo;
	
	@Autowired
	private ExpenseReceiptRepository expReceiptRepo;
	
	public ExpenseService(ExpenseRepository expenseRepo) {
		this.expenseRepo = expenseRepo;
	}
	
	private ExpenseResponseDTO mapToResponse(Expenses expense) {
	    return new ExpenseResponseDTO(
	            expense.getExpenseId(),
	            expense.getExpenseType(),
	            expense.getAmount(),
	            expense.getExpenseDate(),
	            expense.getSubmittedDate(),
	            expense.getStatus(),
	            expense.getApprover() != null ? expense.getApprover().getEmail() : null,
	            expense.getApprovedDate()
	    );
	}
	public ExpenseResponseDTO submitExpense(Long expenseId,
											UserDetails userDetails)
	{

		 // 1️⃣ Get logged-in user email from JWT
	    Users user = userRepo.findByEmail(userDetails.getUsername())
	            .orElseThrow(() -> new UserNotFoundException("User not found"));
		
	    Expenses expense = expenseRepo.findById(expenseId)
				.orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

	
//		, expenseDto.getExpenseType(), expenseDto.getAmount(), expenseDto.getExpenseDate(), userRepo.findById(expenseDto.getUserId()) );
//		if( user.getId().equals( expense.getApprover().getId()) )
//		{
//			throw new UnauthorizedExpenseApprovalException ("You can't approve this expense as you are not manager of this expense's owner");
//		}
		if( expense.getStatus() != ExpenseStatus.SAVEANDCLOSE)
		{
			throw new IllegalStateException("Only DRAFT expenses can be submitted");
		}
		

	    // Check if receipts exist
	    List<ExpenseReceipt> receipts = expReceiptRepo.findByExpense_ExpenseId(expenseId);
	    if (receipts.isEmpty()) {
	        throw new ReceiptRequiredException("Cannot submit expense without at least one receipt.");
	    }
	
		 expense.setSubmittedDate(LocalDateTime.now());
		 expense.setStatus(ExpenseStatus.SUBMITTED);
		 expense.setApprover(user.getManager());
		 expense.setUsers(user);
		
		 expenseRepo.save(expense);
		
		ExpenseHistory history = new ExpenseHistory();
		history.setExpense(expense);
		history.setActionBy(user);
		history.setAction(ExpenseStatus.SUBMITTED);
		history.setActionDate(LocalDateTime.now());
		expHistoryRepo.save(history);

		
		return mapToResponse(expense);
		
	}

	public List<ExpenseResponseDTO> getMyExpenses() {
				
		String email = SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getName();
		
		Users user = userRepo.findByEmail(email)
				.orElseThrow( () -> new  UserNotFoundException("User not found"));
		
		return	expenseRepo.findByUsers_Id(user.getId())
				.stream()
				.map(e -> new ExpenseResponseDTO(
						e.getExpenseId(),
						e.getExpenseType(),
						e.getAmount(),
						e.getExpenseDate(), 
						e.getSubmittedDate(), 
						e.getStatus(),
						e.getApprover().getEmail(),
						e.getApprovedDate()
						))
				.toList()
				;
			
	
	}

	public ExpenseResponseDTO updateExpense(Long expenseId, @Valid UpdateExpenseRequestDTO updateExpDto) {
//		Validate user
		 String email = SecurityContextHolder.getContext()
		            .getAuthentication()
		            .getName();
		Users user =  userRepo.findByEmail(email)
						.orElseThrow( () -> new UserNotFoundException("User not found"));
//		Validate expense
		Expenses expense = expenseRepo.findById(expenseId)
								.orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
//		Authorization check
		if(!expense.getUsers().getId().equals(user.getId()))
		{
			throw new UnauthorizedAccessException("YOu can not update this expense");
		}
		
		boolean exists = expenseRepo.existsByExpenseTypeAndAmountAndExpenseDateAndUsers_UserIdAndExpenseIdNot(
				updateExpDto.getExpenseType(),
				updateExpDto.getAmount(),
				updateExpDto.getExpenseDate(),
				user.getId(),
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
										expense.getExpenseDate(),
										expense.getSubmittedDate(), 
										expense.getStatus(), 
										expense.getApprover().getEmail(),
										expense.getApprovedDate()
										);
	}

	public void deleteExpense(Long expenseId) {
		
//		Validate user
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    Users user = userRepo.findByEmail(email)
	                .orElseThrow(() -> new UserNotFoundException("User not found"));
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
	
	public Page<ExpenseResponseDTO> getExpensesOfUserPaginated(
			Long userId, 
			int page,
			int size,
			String sortBy,
			String direction
			)
	{
		Users user = userRepo.findById(userId)
				.orElseThrow( () -> new UserNotFoundException( "User not found"));
//		Sorting logic
		Sort sort = direction.equalsIgnoreCase("desc")
					? Sort.by(sortBy).descending()
					: Sort.by(sortBy).ascending() ;
		
//		Pageable object
		
		Pageable pageable = PageRequest.of(page, size, sort);
//		Fetch data
		Page<Expenses> expensePage = expenseRepo.findByUsers_Id(userId, pageable);
		
		
		System.out.println("Total Elements: " + expensePage.getTotalElements());
		System.out.println("Total Pages: " + expensePage.getTotalPages());
		System.out.println("Current Page: " + expensePage.getNumber());
//		Map Entity → DTO
		return expensePage.map( e ->
				new ExpenseResponseDTO(
						e.getExpenseId(), 
						e.getExpenseType(), 
						e.getAmount(), 
						e.getExpenseDate(),
						e.getSubmittedDate(), 
						e.getStatus(),
						e.getApprover().getEmail(),
						e.getApprovedDate()
						
				)) ;
				
	}

	public void approveExpense(Long expenseId) {
		String email = SecurityContextHolder
							.getContext()
							.getAuthentication()
							.getName();
		Users loggedInUser = userRepo.findByEmail(email)
				.orElseThrow( () -> new UserNotFoundException("User not found"));
		Expenses expense = expenseRepo.findById(expenseId)
							.orElseThrow( ()-> new ExpenseNotFoundException("Expense not found") ) ;
		
		if( !loggedInUser.getId().equals( expense.getApprover().getId()) )
		{
			throw new UnauthorizedExpenseApprovalException ("You can't approve this expense as you are not manager of this expense's owner");
		}
		if( expense.getStatus() != ExpenseStatus.SUBMITTED)
		{
			throw new ExpenseAlreadyProcessedException ("This expense is already processed");
		}
		
		expense.setStatus(ExpenseStatus.APPROVED);
		expense.setApprovedBy(loggedInUser);
		expense.setApprovedDate(LocalDateTime.now());
		
		ExpenseHistory history = new ExpenseHistory();
		history.setExpense(expense);
		history.setActionBy(loggedInUser);
		history.setAction(ExpenseStatus.APPROVED); // APPROVED or REJECTED
		history.setActionDate(LocalDateTime.now());
		
		expHistoryRepo.save(history);

		
		 expenseRepo.save(expense);
		
	}

	public void rejectExpense(Long expenseId, ExpenseRejectionDTO expRejectDto, UserDetails userDetails) 
	{
		String email = SecurityContextHolder
				.getContext()
				.getAuthentication()
						.getName();
		Users loggedInUser = userRepo.findByEmail(email)
			.orElseThrow( () -> new UserNotFoundException("User not found"));
		Expenses expense = expenseRepo.findById(expenseId)
						.orElseThrow( ()-> new ExpenseNotFoundException("Expense not found") ) ;
		 if (expRejectDto.getRejectionReason() == null || expRejectDto.getRejectionReason().isEmpty()) {
		        throw new RejectionReasonRequiredException("Rejection reason is required to reject an expense");
		    }
		if( !loggedInUser.getId().equals( expense.getApprover().getId()))
		{
			throw new RuntimeException("You can't approve this expense as you are not manager of this expense's owner");
		}
		if( expense.getStatus() != ExpenseStatus.SUBMITTED &&
				expense.getStatus() != ExpenseStatus.RESUBMITTED)
		{
			throw new RuntimeException("This expense is already processed");
		}
		
		
		expense.setStatus(ExpenseStatus.REJECTED);
		expense.setRejectionReason(expRejectDto.getRejectionReason());
		expense.setApprovedBy(loggedInUser);
		expense.setApprovedDate(LocalDateTime.now());
		
		ExpenseHistory history = new ExpenseHistory();
		history.setExpense(expense);
		history.setActionBy(loggedInUser);
		history.setAction(ExpenseStatus.REJECTED); // APPROVED or REJECTED
		history.setActionDate(LocalDateTime.now());
		history.setComment(expRejectDto.getRejectionReason());
		expHistoryRepo.save(history);

		
		 expenseRepo.save(expense);
		
	}

	public Page<ExpenseResponseDTO> getDocumentsForMyReview(int page, int size) {
		String email = SecurityContextHolder.getContext()
						.getAuthentication().getName();
		Users loggedInUser = userRepo.findByEmail(email)
								.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		Pageable pageable = PageRequest.of(page, size,
							Sort.by("submittedDate").descending()
							);
		
		Page<Expenses> expensesPage = expenseRepo
							.findByApprover_IdAndStatusIn(
									loggedInUser.getId(),
									List.of( ExpenseStatus.SUBMITTED,
											ExpenseStatus.RESUBMITTED
											),
									pageable
									);
		1
		
		return expensesPage.map( e -> new ExpenseResponseDTO(
								e.getExpenseId(), 
								e.getExpenseType(),
								e.getAmount(),
								e.getExpenseDate(),
								e.getSubmittedDate(),
								e.getStatus(),
								e.getApprover().getEmail(), 
								e.getApprovedDate()
							));
		
	}

	public void resubmitExpense(Long expenseId, ExpenseRequestDTO expRequestDto, UserDetails userDetails) {
		String email = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		Users loggedInUser = userRepo.findByEmail(email)
								.orElseThrow(() -> new UserNotFoundException("User not found"));

		Expenses expense = expenseRepo.findById(expenseId)
				.orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
		  // 1. Ownership check
		if( !expense.getUsers().getId().equals(loggedInUser.getId()))
		{
			 throw new UnauthorizedAccessException("You can resubmit only your expenses");
		}
		  // 2. Status check
		if(expense.getStatus() != ExpenseStatus.REJECTED)
		{
			throw new IllegalStateException("Only rejected expenses can be resubmitted");
		}
		   // 3. Update editable fields
		expense.setExpenseType(expRequestDto.getExpenseType());
		expense.setAmount(expRequestDto.getAmount());
		expense.setExpenseDate(expRequestDto.getExpenseDate());
		// 4. Resubmission changes
		
		expense.setStatus(ExpenseStatus.RESUBMITTED);
		expense.setSubmittedDate(LocalDateTime.now());
		expense.setRejectionReason(null);
		expense.setApprover(null);
		expense.setApprovedBy(null);
		
		ExpenseHistory history = new ExpenseHistory();
		history.setExpense(expense);
		history.setActionBy(loggedInUser);
		history.setAction(ExpenseStatus.RESUBMITTED);
		history.setActionDate(LocalDateTime.now());
		expHistoryRepo.save(history);

		expenseRepo.save(expense);
	
	}
	
	public List<ExpenseHistoryDTO> getExpenseHistory(Long expenseId)
	{
		Expenses expense = expenseRepo.findById(expenseId)
				.orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
		List<ExpenseHistory> historyList = expHistoryRepo
									.findByExpenseOrderByActionDateAsc(expense);
		
		return historyList.stream()
				.map( h -> new ExpenseHistoryDTO(h.getActionDate(),
													h.getActionBy().getEmail(),
													h.getAction(), 
													h.getComment()
													))
				.toList();
	}

	public ExpenseResponseDTO saveAndCloseExpense(@Valid ExpenseRequestDTO expDto, UserDetails userDetails) {
		
		Users user = userRepo.findByEmail(userDetails.getUsername())
						.orElseThrow(()-> new UserNotFoundException("User not found"));
		
		boolean exists = expenseRepo.existsByExpenseTypeAndAmountAndExpenseDateAndUsers(
				expDto.getExpenseType(), 
				expDto.getAmount(), 
				expDto.getExpenseDate(), 
				user);
		if(exists)
		{
			throw new DuplicateExpenseException("Same Expense already exists");
		}
		  Expenses expense = new Expenses();
		    expense.setExpenseType(expDto.getExpenseType());
		    expense.setAmount(expDto.getAmount());
		    expense.setExpenseDate(expDto.getExpenseDate());
		    expense.setUsers(user);
		    expense.setStatus(ExpenseStatus.SAVEANDCLOSE);
		    Expenses saved = expenseRepo.save(expense);
		    return mapToResponse(saved);
	
	}
	
	@Value("${app.receipts.dir}")
	String appReceiptsDir;
	
	public  ExpenseReceipt uploadReceipt(MultipartFile file, Long expenseId, UserDetails userDetails) throws IOException {
		
		Expenses expense = expenseRepo.findById(expenseId)
				.orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
		 Users user = userRepo.findByEmail(userDetails.getUsername())
		            .orElseThrow(() -> new RuntimeException("User not found"));
		
		 // 1️⃣ Check ownership: the logged-in user must be the one who created the expense
		    if (!expense.getUsers().getId().equals(user.getId())) {
		        throw new UnauthorizedAccessException("You are not authorized to upload receipt for this expense");
		    }
		 
		 // 1️⃣ Generate file name and path
		
		 Path folderPath = Paths.get(appReceiptsDir, String.valueOf(user.getId()), String.valueOf(expense.getExpenseId()));
		    Files.createDirectories(folderPath);
		 
		 String originalFileName = file.getOriginalFilename();
		    String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
		    Path filePath = folderPath.resolve(uniqueFileName);
	    
		    // 3️⃣ Save file using Files.copy
		    try (InputStream is = file.getInputStream()) {
		        Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
		    }

		// 3️⃣ Save metadata in DB
	    ExpenseReceipt receipt = new ExpenseReceipt();
	    receipt.setFileName(originalFileName);
	    receipt.setFilePath(filePath.toString());
	    receipt.setExpense(expense);
	    receipt.setUploadedBy(user);
	    receipt.setUploadedAt(LocalDateTime.now());
		return  expReceiptRepo.save(receipt);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
