package com.lld.observer;

import com.lld.enitity.Expense;
import com.lld.enitity.Group;
import com.lld.enitity.User;

/**
 * Owns the "who gets notified, and with what message" logic for expense
 * events. Kept separate from {@link Group} on purpose: notification is a
 * cross-cutting concern, not part of the group's core data (users + expenses).
 * <p>
 * Recipients are derived from the group's existing membership list rather
 * than a separately maintained observer list, so there's no duplicate state
 * to keep in sync. Adding a new event type (e.g. "user added to group",
 * "user left") means adding a new method here, not touching Group at all.
 */
public class ExpenseNotificationService {

    public void notifyExpenseCreated(Group group, Expense expense) {
        String payerId = expense.getPaidBy().getUserId();
        String message = String.format("%s added $%.2f expense in %s group",
                payerId, expense.getAmount(), group.getGrpupName());

        notifyAllExcept(group, payerId, message);
    }

    private void notifyAllExcept(Group group, String excludedUserId, String message) {
        for (User user : group.getUsers()) {
            if (!user.getUserId().equals(excludedUserId)) {
                user.update(message);
            }
        }
    }
}
