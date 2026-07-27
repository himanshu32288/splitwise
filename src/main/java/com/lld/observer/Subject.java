package com.lld.observer;

import com.lld.enitity.Expense;
import com.lld.enitity.User;

public interface Subject {
    void addObserver(User user);
    void removeObserver(User user);
    void notifyObservers(Expense expense);
}
