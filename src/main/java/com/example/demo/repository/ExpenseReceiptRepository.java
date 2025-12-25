package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ExpenseReceipt;

@Repository
public interface ExpenseReceiptRepository extends JpaRepository<ExpenseReceipt, Long>{

	 List<ExpenseReceipt> findByExpense_ExpenseId(Long expenseId);
}
