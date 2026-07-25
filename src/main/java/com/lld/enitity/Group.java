package com.lld.enitity;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupId;
    private List<User> users;
    private List<Expense> expenses;

    public Group(String groupId) {
        this.users = new ArrayList<>();
        expenses = new ArrayList<>();
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

}
