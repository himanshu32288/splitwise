package com.lld.repository;

import com.lld.enitity.Expense;
import com.lld.enitity.Group;
import com.lld.enitity.User;

import java.util.List;

public interface GroupRepository {
    void addGroup(Group group);
    Group getGroupById(String groupId);
    List<Group> getAllGroups();
    void addUserToGroup(String groupId, User user);
    void addExpenseToGroup(String groupId, Expense expense);
    void addExpenseToGroup(String groupId, List<Expense> expenses);
}
