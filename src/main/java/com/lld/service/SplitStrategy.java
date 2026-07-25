package com.lld.service;

import com.lld.dto.ExpenseRequest;
import com.lld.enitity.Expense;
import com.lld.enitity.Group;

import java.util.List;

public interface SplitStrategy {
    List<Expense> splitExpense(ExpenseRequest expenseRequest, Group group);
}
