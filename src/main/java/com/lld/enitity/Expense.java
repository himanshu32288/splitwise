package com.lld.enitity;

import lombok.Builder;

@Builder
public class Expense {
    private String expenseId;
    private User paidBy;
    private User paidTo;
    private double amount;

    public String getExpenseId() {
        return expenseId;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public User getPaidTo() {
        return paidTo;
    }

    public double getAmount() {
        return amount;
    }
}
