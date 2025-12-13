package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Expenses;

import java.time.LocalDate;
import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expenses, Long>{

//	users → is the field name in the entity
//_Id → means Spring should match it to the PK of the Users entity
//So Spring generates:
//SELECT * FROM expenses WHERE user_id = ?
	public  List<Expenses> findByUsers_Id(Long userId);

	public boolean existsByExpenseTypeAndAmountAndExpenseDateAndUserId(
			String expenseType,
	        double amount,
	        LocalDate expenseDate,
	        Long userId);
	
	public boolean existsByExpenseTypeAndAmountAndExpenseDateAndUsers_UserIdExpenseIdNot(
			String expenseType,
			double amount,
			LocalDate expenseDate,
			Long userId,
			Long expenseId
			);
}
