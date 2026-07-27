package com.lld.enitity;

import com.lld.observer.Observer;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@EqualsAndHashCode
public class User implements Observer {
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

    @Override
    public void update(String message) {
        System.out.println("User " + userId + " received notification: " + message);
    }
}
