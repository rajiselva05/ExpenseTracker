package com.expense.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
public class Expense {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	//@Column(name="expense_id")
	private Integer expenseId;
	private String category;
	private String debitAmount;
	private String creditAmount;
	private String description;
	private Date entryDate;
	private double total;
	
    @JsonIgnore
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public Integer getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(String debitAmount) {
		this.debitAmount = debitAmount;
	}

	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double currentTotal) {
		this.total = currentTotal;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Expense [expenseId=" + expenseId + ", category=" + category + ", debitAmount=" + debitAmount
				+ ", creditAmount=" + creditAmount + ", description=" + description + ", entryDate=" + entryDate
				+ ", total=" + total + ", user=" + user + "]";
	}
	
	
}
