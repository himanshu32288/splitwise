package com.lld.enitity;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Builder
public class Expense {
    @Getter
    private double amount;
    @Getter
    private User paidBy;
    private List<Balance> balances;
    @Getter
    private String expenseId;

    public List<Balance> getBalances() {
        return Collections.unmodifiableList(balances);
    }
}
