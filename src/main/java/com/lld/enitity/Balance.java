package com.lld.enitity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Balance {
    private User paidTo;
    private double amount;
}
