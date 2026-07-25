package com.lld.repository;

import com.lld.enitity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users;

    public InMemoryUserRepository() {
        this.users = new ConcurrentHashMap<>();
    }

    @Override
    public void addUser(User user) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("User and userId cannot be null");
        }
        users.put(user.getUserId(), user);
    }

    @Override
    public User getUserById(String userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
