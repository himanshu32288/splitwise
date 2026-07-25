package com.lld.service.impl;

import com.lld.dto.ExpenseRequest;
import com.lld.enitity.Expense;
import com.lld.enitity.Group;
import com.lld.service.SplitStrategy;

public class PercentageSplit implements SplitStrategy {
    @Override
    public Expense splitExpense(ExpenseRequest expenseRequest, Group group) {
        return Expense.builder().build();
    }
}
