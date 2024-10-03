package com.expense.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense.entity.Expense;
import com.expense.entity.User;
import com.expense.repository.ExpenseRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense saveExpenses(Expense expense) {
        // Calculate total when saving a new expense
        double debitAmount = Double.parseDouble(expense.getDebitAmount());
        double creditAmount = Double.parseDouble(expense.getCreditAmount());
        double total = debitAmount - creditAmount; // Assuming total is calculated as debit - credit
        expense.setTotal(total); // Set the calculated total
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Integer id, Expense updatedExpense) {
        Optional<Expense> existingExpenseData = expenseRepository.findById(id);

        if (existingExpenseData.isPresent()) {
            Expense existingExpense = existingExpenseData.get();

            // Store previous debit and credit amounts for correct recalculation
            double previousDebitAmount = Double.parseDouble(existingExpense.getDebitAmount());
            double previousCreditAmount = Double.parseDouble(existingExpense.getCreditAmount());

            // Set updated fields for the expense
            existingExpense.setCategory(updatedExpense.getCategory());
            existingExpense.setDescription(updatedExpense.getDescription());
            existingExpense.setEntryDate(updatedExpense.getEntryDate());
            existingExpense.setCreditAmount(updatedExpense.getCreditAmount());
            existingExpense.setDebitAmount(updatedExpense.getDebitAmount());

            // Convert updated debit and credit strings to doubles
            double newDebitAmount = Double.parseDouble(updatedExpense.getDebitAmount());
            double newCreditAmount = Double.parseDouble(updatedExpense.getCreditAmount());

            // Calculate the difference from the previous total
            // Formula: New total = Previous total - (previous debit - previous credit) + (new credit - new debit)
            double newTotal = existingExpense.getTotal() - (previousDebitAmount - previousCreditAmount)
                    + (newCreditAmount - newDebitAmount);

            // Update the total
            existingExpense.setTotal(newTotal);

            // Save the updated expense
            return expenseRepository.save(existingExpense);
        }
        return null; // or throw an exception if preferred
    }


    public void deleteExpense(Integer id) {
        Expense expenseToDelete = findById(id);
        if (expenseToDelete != null) {
            // Optionally, call a method to recalculate the total before deletion
            // recalculateTotalOnDelete(expenseToDelete, expenseToDelete.getUser().getId());
            expenseRepository.deleteById(id);
        }
    }

    public Expense getTotalForUser(Long userId) {
        Float total = expenseRepository.findTotalByUserId(userId);
        Expense totalExpense = new Expense();
        totalExpense.setTotal(total != null ? total : 0.0f);
        return totalExpense;
    }

    public Expense findById(Integer id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        return expense.orElse(null); // Return the expense if found, otherwise null
    }

    public void recalculateTotalOnDelete(Expense expense, Long userId) {
        // Logic to update the user's total amount
        float debitAmount = Float.parseFloat(expense.getDebitAmount());
        float creditAmount = Float.parseFloat(expense.getCreditAmount());
        float currentTotal = expenseRepository.findTotalByUserId(userId);
        float newTotal = currentTotal + debitAmount - creditAmount; // Recalculate total
        // Logic to save the new total in the user's record, if applicable.
    }
}
