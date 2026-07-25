package com.lld.enitity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Expense {
    private double amount;
    private User paidBy;
    private List<Balance> balances;
}
