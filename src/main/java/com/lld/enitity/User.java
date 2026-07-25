package com.lld.enitity;

import java.util.ArrayList;
import java.util.List;

public class User {
    String userId;
    List<Group> groups;

    public User(String userId) {
        this.userId = userId;
        this.groups = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }
}
