package com.lld.service;

import com.lld.dto.ExpenseRequest;
import com.lld.enitity.Balance;
import com.lld.enitity.Expense;
import com.lld.enitity.Group;
import com.lld.enitity.User;
import com.lld.enums.SplitType;
import com.lld.factory.SplitStrategyFactory;
import com.lld.observer.ExpenseNotificationService;
import com.lld.repository.GroupRepository;
import com.lld.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public class SplitWiseService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final EnumMap<SplitType, SplitStrategy> splitStrategies;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ExpenseNotificationService notificationService = new ExpenseNotificationService();

    public SplitWiseService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.splitStrategies = new EnumMap<>(SplitType.class);
        splitStrategies.put(SplitType.EQUAL, SplitStrategyFactory.createSplitStrategy(SplitType.EQUAL));
        splitStrategies.put(SplitType.EXACT, SplitStrategyFactory.createSplitStrategy(SplitType.EXACT));
        splitStrategies.put(SplitType.PERCENTAGE, SplitStrategyFactory.createSplitStrategy(SplitType.PERCENTAGE));
    }

    public void createExpense(ExpenseRequest expenseRequest) {
        lock.writeLock().lock();
        try {
            groupRepository.getGroupById(expenseRequest.getGroupId())
                    .ifPresent(group -> {
                        Expense expense = splitStrategies.get(expenseRequest.getSplitType())
                                .splitExpense(expenseRequest, group);
                        group.addExpense(expense);
                        notificationService.notifyExpenseCreated(group, expense);
                    });
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void deleteExpense(ExpenseRequest expenseRequest) {
        lock.writeLock().lock();
        try {
            groupRepository.getGroupById(expenseRequest.getGroupId())
                    .ifPresent(group -> {
                        Expense expense = splitStrategies.get(expenseRequest.getSplitType())
                                .splitExpense(expenseRequest, group);
                        group.removeExpense(expense.getExpenseId());
                    });
        } finally {
            lock.writeLock().unlock();
        }
    }


    public List<Balance> getBalances(String userId) {
        lock.readLock().lock();
        try {
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
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Get list of all expenses added to a group (audit/history view).
     * Returns complete expense records including who paid and who owes.
     *
     * @param groupId the group to get expenses for
     * @return list of all expenses in the group
     */
    public List<Expense> getGroupExpenses(String groupId) {
        lock.readLock().lock();
        try {
            Optional<Group> groupOptional = groupRepository.getGroupById(groupId);
            return groupOptional
                    .map(Group::getExpenses)
                    .map(ArrayList::new)  // Defensive copy
                    .orElseGet(ArrayList::new);
        } finally {
            lock.readLock().unlock();
        }
    }

    // Read operation
    public List<Expense> getUserExpenseHistory(String userId) {
        lock.readLock().lock();
        try {
            List<Expense> userExpenses = new ArrayList<>();

            for (Group group : groupRepository.getAllGroups()) {
                for (Expense expense : group.getExpenses()) {
                    if (expense.getPaidBy().getUserId().equals(userId) ||
                            expense.getBalances().stream()
                                    .anyMatch(balance -> balance.getPaidTo().getUserId().equals(userId))) {
                        userExpenses.add(expense);
                    }
                }
            }

            return userExpenses;
        } finally {
            lock.readLock().unlock();
        }
    }
}