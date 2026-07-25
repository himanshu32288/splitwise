package com.lld.service.impl;

import com.lld.dto.ExpenseRequest;
import com.lld.enitity.Expense;
import com.lld.enitity.Group;
import com.lld.enitity.User;
import com.lld.service.SplitStrategy;

import java.util.List;
import java.util.UUID;

public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public List<Expense> splitExpense(ExpenseRequest expenseRequest, Group group) {
        double totalAmount = expenseRequest.getAmount();
        int numberOfUsers = group.getUsers().size();
        double splitAmount = totalAmount / numberOfUsers;
        User paidBy = group.getUsers()
                .stream().filter(user -> user.getUserId().equals(expenseRequest.getPaidBy()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found in group"));
        for (var paidTo : group.getUsers()) {
            if (paidTo.getUserId().equals(paidBy.getUserId())) {
                continue;
            }
            Expense expense = Expense
                    .builder()
                    .expenseId(UUID.randomUUID().toString())
                    .amount(splitAmount)
                    .paidBy(paidBy)
                    .paidTo(paidTo)
                    .build();
            group.addExpense(expense);
        }

        return group.getExpenses();
    }
}
