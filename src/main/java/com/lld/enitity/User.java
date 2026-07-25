package com.lld.enitity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {
    String userId;
    List<Group> groups;

    public User(String userId) {
        this.userId = userId;
        this.groups =new CopyOnWriteArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public List<Group> getGroups() {
        return new ArrayList<>(groups);  // Return defensive copy
    }

    public synchronized void addGroup(Group group) {
        if (group != null) {
            groups.add(group);
        }
    }
}
