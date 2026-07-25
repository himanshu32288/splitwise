package com.lld.service.impl;

import com.lld.dto.ExpenseRequest;
import com.lld.enitity.Balance;
import com.lld.enitity.Expense;
import com.lld.enitity.Group;
import com.lld.enitity.User;
import com.lld.service.SplitStrategy;

import java.util.ArrayList;
import java.util.List;

public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public Expense splitExpense(ExpenseRequest expenseRequest, Group group) {
        double totalAmount = expenseRequest.getAmount();
        int numberOfUsers = group.getUsers().size();
        double splitAmount = totalAmount / numberOfUsers;
        User paidBy = group.getUsers()
                .stream().filter(user -> user.getUserId().equals(expenseRequest.getPaidBy()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found in group"));
        List<Balance> balancesList = new ArrayList<>();
        for (var paidTo : group.getUsers()) {
            if (paidTo.getUserId().equals(paidBy.getUserId())) {
                continue;
            }
            balancesList.add(Balance.builder()
                    .amount(splitAmount)
                    .paidTo(paidTo)
                    .build());
        }
        return Expense.builder()
                .balances(balancesList)
                .paidBy(paidBy)
                .amount(totalAmount)
                .build();
    }
}
