package com.lld.service;

import com.lld.dto.ExpenseRequest;
import com.lld.enitity.Expense;
import com.lld.enitity.Group;

public interface SplitStrategy {
    Expense splitExpense(ExpenseRequest expenseRequest, Group group);
}
