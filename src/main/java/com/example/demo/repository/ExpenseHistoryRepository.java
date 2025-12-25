package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ExpenseHistory;
import com.example.demo.model.Expenses;

public interface ExpenseHistoryRepository extends JpaRepository<ExpenseHistory, Long>
{

	List<ExpenseHistory> findByExpenseOrderByActionDateAsc(Expenses expense);
	
}
