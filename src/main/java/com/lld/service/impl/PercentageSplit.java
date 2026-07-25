package com.lld.service.impl;

import com.lld.dto.ExpenseRequest;
import com.lld.enitity.Balance;
import com.lld.enitity.Expense;
import com.lld.enitity.Group;
import com.lld.enitity.User;
import com.lld.service.SplitStrategy;

import java.util.ArrayList;
import java.util.List;

public class PercentageSplit implements SplitStrategy {
    @Override
    public Expense splitExpense(ExpenseRequest expenseRequest, Group group) {
        validatePercentage(expenseRequest);
        double totalAmount = expenseRequest.getAmount();
        User paidBy = group.getUsers()
                .stream().filter(user -> user.getUserId().equals(expenseRequest.getPaidBy()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found in group"));
        List<Balance> balancesList = new ArrayList<>();
        for (var paidTo : group.getUsers()) {
            expenseRequest.getParticipants()
                    .forEach((key, value) -> {
                        if (key.equals(paidTo.getUserId())) {
                            balancesList.add(Balance.builder()
                                    .amount(totalAmount*value/100)
                                    .paidTo(paidTo)
                                    .build());
                        }
                    });
        }
        return Expense.builder()
                .balances(balancesList)
                .paidBy(paidBy)
                .amount(totalAmount)
                .build();
    }

private void validatePercentage(ExpenseRequest expenseRequest) {
        double totalPercentage = expenseRequest.getParticipants().values().stream().mapToDouble(Double::doubleValue).sum();
        if (totalPercentage != 100) {
            throw new IllegalArgumentException("Total percentage must be equal to 100");
        }
    }
}
