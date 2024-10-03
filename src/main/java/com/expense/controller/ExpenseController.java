package com.expense.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.expense.entity.Expense;
import com.expense.entity.User;
import com.expense.service.ExpenseService;
import com.expense.service.CustomUserDetailService;

@RestController
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CustomUserDetailService userDetailService;

    // Helper method to get the logged-in user
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userDetailService.loadUserEntityByUsername(username);
    }

    // Get all expenses for the logged-in user
    @GetMapping("/expense")
    public ResponseEntity<List<Expense>> getAllExpenseDetails() {
        User user = getAuthenticatedUser();
        List<Expense> userExpenses = expenseService.getAllExpenses().stream()
                .filter(expense -> expense.getUser() != null && expense.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userExpenses);
    }

    // Get total expenses for the logged-in user
    @GetMapping("/total")
    public ResponseEntity<Expense> getTotal() {
        User user = getAuthenticatedUser();
        Expense totalExpense = expenseService.getTotalForUser(user.getId());
        return ResponseEntity.ok(totalExpense);
    }

    // Add a new expense for the logged-in user
    @PostMapping("/addexpense")
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        User user = getAuthenticatedUser();
        expense.setUser(user);
        Expense savedExpense = expenseService.saveExpenses(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    // Update an existing expense
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Integer id, @RequestBody Expense updatedExpense) {
        User user = getAuthenticatedUser();
        Expense existingExpense = expenseService.findById(id);
        if (existingExpense == null || !existingExpense.getUser().getId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }

        updatedExpense.setUser(user);
        Expense updated = expenseService.updateExpense(id, updatedExpense);
        return ResponseEntity.ok(updated);
    }

    // Delete an expense
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Integer id) {
        User user = getAuthenticatedUser();
        Expense existingExpense = expenseService.findById(id);
        if (existingExpense == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existingExpense.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to delete this expense.");
        }

        // Recalculate total before deletion
        expenseService.recalculateTotalOnDelete(existingExpense, user.getId());
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
