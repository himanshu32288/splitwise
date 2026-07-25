package com.lld.enitity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group {
    private String groupId;
    private String grpupName;
    private List<User> users;
    private List<Expense> expenses;

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

}
