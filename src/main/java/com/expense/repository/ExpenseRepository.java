package com.expense.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.expense.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense,Integer>{
    List<Expense> findByUserId(Long userId); // New method to find expenses by user ID
    
    @Query("SELECT SUM(e.total) FROM Expense e WHERE e.user.id = :userId")
    Float findTotalByUserId(@Param("userId") Long userId);

}
