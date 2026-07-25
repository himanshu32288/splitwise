package com.lld.service;

import com.lld.dto.ExpenseRequest;
import com.lld.enitity.Balance;
import com.lld.enitity.Expense;
import com.lld.enitity.Group;
import com.lld.enitity.User;
import com.lld.enums.SplitType;
import com.lld.factory.SplitStrategyFactory;
import com.lld.repository.GroupRepository;
import com.lld.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SplitWiseService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private Map<SplitType, SplitStrategy> splitStrategies;

    public SplitWiseService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        splitStrategies = new HashMap<>();
        splitStrategies.put(SplitType.EQUAL, SplitStrategyFactory.createSplitStrategy(SplitType.EQUAL));
        splitStrategies.put(SplitType.PERCENTAGE, SplitStrategyFactory.createSplitStrategy(SplitType.PERCENTAGE));
        splitStrategies.put(SplitType.EXACT, SplitStrategyFactory.createSplitStrategy(SplitType.EXACT));
    }

    public synchronized void createExpense(ExpenseRequest expenseRequest) {
        groupRepository.getGroupById(expenseRequest.getGroupId())
                .ifPresent(group -> {
                    Expense expense = splitStrategies.get(expenseRequest.getSplitType()).splitExpense(expenseRequest, group);
                    group.addExpense(expense);
                });
    }

    public double getBalance(String userA, String userB, String groupId) {
        Optional<Group> groupOptional = groupRepository.getGroupById(groupId);
        if (!groupOptional.isPresent()) {
            throw new IllegalArgumentException("Group not found");
        }
        Group group = groupOptional.get();
        return group.getExpenses()
                .stream()
                .filter(expense -> expense.getPaidBy().getUserId().equals(userA))
                .map(expense -> expense.getBalances()
                        .stream()
                        .filter(balance -> balance.getPaidTo().getUserId().equals(userB))
                        .map(Balance::getAmount)
                        .reduce(0.0, Double::sum)
                )
                .reduce(0.0, Double::sum);
    }

    /**
     * Get net balances for a user across all groups they belong to (aggregate/global view).
     * Returns a list of Balance objects where each balance represents money owed to/from another user.
     * Positive amount means the other user owes this user, negative means this user owes the other.
     *
     * @param userId the user to get balances for
     * @return list of net balances across all groups
     */
    public List<Balance> getBalances(String userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        Map<String, Double> aggregatedBalances = new HashMap<>();

        for (Group group : user.getGroups()) {
            for (Expense expense : group.getExpenses()) {
                if (expense.getPaidBy().getUserId().equals(userId)) {
                    for (Balance balance : expense.getBalances()) {
                        String paidToUserId = balance.getPaidTo().getUserId();
                        aggregatedBalances.put(paidToUserId,
                                aggregatedBalances.getOrDefault(paidToUserId, 0.0) + balance.getAmount());
                    }
                } else {
                    for (Balance balance : expense.getBalances()) {
                        if (balance.getPaidTo().getUserId().equals(userId)) {
                            String paidByUserId = expense.getPaidBy().getUserId();
                            aggregatedBalances.put(paidByUserId,
                                    aggregatedBalances.getOrDefault(paidByUserId, 0.0) - balance.getAmount());
                        }
                    }
                }
            }
        }

        return aggregatedBalances.entrySet().stream()
                .filter(entry -> Math.abs(entry.getValue()) > 0.01)
                .map(entry -> Balance.builder()
                        .paidTo(userRepository.getUserById(entry.getKey()))
                        .amount(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get list of all expenses added to a group (audit/history view).
     * Returns complete expense records including who paid and who owes.
     *
     * @param groupId the group to get expenses for
     * @return list of all expenses in the group
     */
    public List<Expense> getGroupExpenses(String groupId) {
        Optional<Group> groupOptional = groupRepository.getGroupById(groupId);
        return groupOptional.map(Group::getExpenses).orElseGet(ArrayList::new);
    }

    /**
     * Get all expenses a user was part of, across all groups (history view).
     * Returns expenses where the user either paid or owes money.
     *
     * @param userId the user to get expense history for
     * @return list of all expenses involving this user
     */
    public List<Expense> getUserExpenseHistory(String userId) {
        List<Expense> userExpenses = new ArrayList<>();

        for (Group group : groupRepository.getAllGroups()) {
            for (Expense expense : group.getExpenses()) {
                if (expense.getPaidBy().getUserId().equals(userId)) {
                    userExpenses.add(expense);
                } else if (expense.getBalances().stream()
                        .anyMatch(balance -> balance.getPaidTo().getUserId().equals(userId))) {
                    userExpenses.add(expense);
                }
            }
        }

        return userExpenses;
    }
}
