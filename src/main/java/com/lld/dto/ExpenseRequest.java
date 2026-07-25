package com.lld.dto;

import com.lld.enums.SplitType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class ExpenseRequest {
    private String paidBy;
    double amount;
    private String groupId;
    private SplitType splitType;
    Map<String, Double> participants;
}
