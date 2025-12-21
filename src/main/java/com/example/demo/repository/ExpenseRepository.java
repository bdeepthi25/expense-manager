package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.enums.ExpenseStatus;
import com.example.demo.model.Expenses;
import com.example.demo.model.Users;


import java.time.LocalDate;
import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expenses, Long>{

//	users → is the field name in the entity
//_Id → means Spring should match it to the PK of the Users entity
//So Spring generates:
//SELECT * FROM expenses WHERE user_id = ?
	public  List<Expenses> findByUsers_Id(Long userId);
	public  Page<Expenses> findByUsers_Id(Long userId,  Pageable pageable);

//	public boolean existsByExpenseTypeAndAmountAndExpenseDateAndUserId(
//			String expenseType,
//	        double amount,
//	        LocalDate expenseDate,
//	        Long userId);
	
	public boolean existsByExpenseTypeAndAmountAndExpenseDateAndUsers_UserIdAndExpenseIdNot(
			String expenseType,
			double amount,
			LocalDate expenseDate,
			Long userId,
			Long expenseId
			);
	
	public boolean existsByExpenseTypeAndAmountAndExpenseDateAndUsers(
            String expenseType,
            double amount,
            LocalDate expenseDate,
            Users user);
	public List<Expenses> findByApprover_IdAndStatus(Long id, ExpenseStatus submitted);
}
