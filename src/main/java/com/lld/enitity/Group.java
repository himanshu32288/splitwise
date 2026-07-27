package com.lld.enitity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Pure domain entity: just membership and expense data.
 * No notification/observer logic lives here — see
 * {@link com.lld.observer.ExpenseNotificationService} for that concern.
 */
public class Group {
    private final String groupId;
    private final String grpupName;
    private final List<User> users;
    private final List<Expense> expenses;

    public Group(String groupId, String grpupName) {
        this.users = new CopyOnWriteArrayList<>();
        expenses = new CopyOnWriteArrayList<>();
        this.groupId = groupId;
        this.grpupName = grpupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGrpupName() {
        return grpupName;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

    public void removeExpense(String expenseId) {
        this.expenses.removeIf(e -> e.getExpenseId().equals(expenseId));
    }
}
