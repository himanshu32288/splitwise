package com.lld.enitity;

import com.lld.observer.Subject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group implements Subject {
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

    @Override
    public void addObserver(User user) {
        this.users.add(user);
    }

    @Override
    public void removeObserver(User user) {
        this.users.remove(user);
    }

    @Override
    public void notifyObservers(Expense expense) {
        String payerId = expense.getPaidBy().getUserId();
        String message = String.format("%s added $%.2f expense in %s group",
                payerId, expense.getAmount(), grpupName);

        for (User user : users) {
            if (!user.getUserId().equals(payerId)) {
                user.update(message);
            }
        }
    }
}
