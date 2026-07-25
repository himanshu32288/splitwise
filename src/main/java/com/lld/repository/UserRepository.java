package com.lld.repository;

import com.lld.enitity.User;

import java.util.List;

public interface UserRepository {
    void addUser(User user);
    User getUserById(String userId);
    List<User> getAllUsers();
}
