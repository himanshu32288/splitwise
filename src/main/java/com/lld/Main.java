package com.lld;

import com.lld.dto.ExpenseRequest;
import com.lld.enitity.Balance;
import com.lld.enitity.Expense;
import com.lld.enitity.Group;
import com.lld.enitity.User;
import com.lld.enums.SplitType;
import com.lld.repository.GroupRepository;
import com.lld.repository.InMemoryGroupRepository;
import com.lld.repository.InMemoryUserRepository;
import com.lld.repository.UserRepository;
import com.lld.service.SplitWiseService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           🏠 SPLITWISE APPLICATION DEMO 🏠                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // Initialize in-memory repositories
        UserRepository userRepository = new InMemoryUserRepository();
        GroupRepository groupRepository = new InMemoryGroupRepository();
        SplitWiseService splitWiseService = new SplitWiseService(userRepository, groupRepository);

        // Create users
        System.out.println("📝 Creating users...");
        User alice = new User("alice");
        User bob = new User("bob");
        User charlie = new User("charlie");
        User diana = new User("diana");

        userRepository.addUser(alice);
        userRepository.addUser(bob);
        userRepository.addUser(charlie);
        userRepository.addUser(diana);
        System.out.println("✓ Users created: alice, bob, charlie, diana\n");

        // Create groups
        System.out.println("📝 Creating groups...");
        Group vacationGroup = new Group("vacation-trip-2024", "Vacation Trip 2024");
        Group flatGroup = new Group("apartment-rent", "Apartment Rent");

        vacationGroup.addUser(alice);
        vacationGroup.addUser(bob);
        vacationGroup.addUser(charlie);

        flatGroup.addUser(alice);
        flatGroup.addUser(bob);
        flatGroup.addUser(diana);

        groupRepository.addGroup(vacationGroup);
        groupRepository.addGroup(flatGroup);

        alice.addGroup(vacationGroup);
        alice.addGroup(flatGroup);
        bob.addGroup(vacationGroup);
        bob.addGroup(flatGroup);
        charlie.addGroup(vacationGroup);
        diana.addGroup(flatGroup);

        System.out.println("✓ Group created: vacation-trip-2024 (alice, bob, charlie)");
        System.out.println("✓ Group created: apartment-rent (alice, bob, diana)\n");

        // Create expenses - Vacation Trip
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("💰 VACATION TRIP - Equal Split\n");

        ExpenseRequest expense1 = ExpenseRequest.builder()
                .groupId("vacation-trip-2024")
                .paidBy("alice")
                .amount(300.0)
                .splitType(SplitType.EQUAL)
                .build();

        splitWiseService.createExpense(expense1);
        System.out.println("✓ Alice paid $300 for hotel (equal split among 3 people)");
        System.out.println("  → Each person pays: $100\n");

        ExpenseRequest expense2 = ExpenseRequest.builder()
                .groupId("vacation-trip-2024")
                .paidBy("bob")
                .amount(150.0)
                .splitType(SplitType.EQUAL)
                .build();

        splitWiseService.createExpense(expense2);
        System.out.println("✓ Bob paid $150 for dinner (equal split among 3 people)");
        System.out.println("  → Each person pays: $50\n");

        // Create expenses - Apartment Rent
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("💰 APARTMENT RENT - Equal Split\n");

        ExpenseRequest expense3 = ExpenseRequest.builder()
                .groupId("apartment-rent")
                .paidBy("diana")
                .amount(600.0)
                .splitType(SplitType.EQUAL)
                .build();

        splitWiseService.createExpense(expense3);
        System.out.println("✓ Diana paid $600 for utilities (equal split among 3 people)");
        System.out.println("  → Each person pays: $200\n");

        // ═════════════════════════════════════════════════════════════════════════════
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    📊 QUERY RESULTS 📊                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // 1. Get Balances - Global View
        System.out.println("1️⃣  getBalances(userId) - NET BALANCES ACROSS ALL GROUPS\n");
        printUserBalances(splitWiseService, "alice");
        printUserBalances(splitWiseService, "bob");
        printUserBalances(splitWiseService, "charlie");
        printUserBalances(splitWiseService, "diana");

        // 2. Get Group Expenses
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("2️⃣  getGroupExpenses(groupId) - EXPENSE HISTORY/AUDIT\n");
        printGroupExpenses(splitWiseService, "vacation-trip-2024", "Vacation Trip");
        printGroupExpenses(splitWiseService, "apartment-rent", "Apartment Rent");

        // 3. Get User Expense History
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("3️⃣  getUserExpenseHistory(userId) - USER EXPENSE HISTORY\n");
        printUserHistory(splitWiseService, "alice");
        printUserHistory(splitWiseService, "bob");

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                   ✅ DEMO COMPLETED ✅                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    private static void printUserBalances(SplitWiseService service, String userId) {
        List<Balance> balances = service.getBalances(userId);
        System.out.printf("👤 %s's Net Balances:%n", userId.toUpperCase());

        if (balances.isEmpty()) {
            System.out.println("   (No balances)");
        } else {
            for (Balance balance : balances) {
                String arrow = balance.getAmount() > 0 ? "←" : "→";
                String direction = balance.getAmount() > 0 ? "owes to" : "is owed by";
                System.out.printf("   %s $%.2f %s %s%n",
                        arrow,
                        Math.abs(balance.getAmount()),
                        direction,
                        balance.getPaidTo().getUserId());
            }
        }
        System.out.println();
    }

    private static void printGroupExpenses(SplitWiseService service, String groupId, String groupName) {
        List<Expense> expenses = service.getGroupExpenses(groupId);
        System.out.printf("📋 %s - Expenses:%n", groupName.toUpperCase());
        System.out.printf("   Total expenses: %d%n", expenses.size());

        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            System.out.printf("   Expense #%d: %s paid $%.2f%n",
                    i + 1,
                    expense.getPaidBy().getUserId(),
                    expense.getAmount());
            System.out.printf("      Splits: ");
            for (Balance balance : expense.getBalances()) {
                System.out.printf("%s: $%.2f | ",
                        balance.getPaidTo().getUserId(),
                        balance.getAmount());
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printUserHistory(SplitWiseService service, String userId) {
        List<Expense> history = service.getUserExpenseHistory(userId);
        System.out.printf("📜 %s's Expense History (across all groups):%n", userId.toUpperCase());
        System.out.printf("   Total expenses involved: %d%n", history.size());

        for (int i = 0; i < history.size(); i++) {
            Expense expense = history.get(i);
            System.out.printf("   #%d: ", i + 1);
            if (expense.getPaidBy().getUserId().equals(userId)) {
                System.out.printf("Paid $%.2f (splits among %d people)%n",
                        expense.getAmount(),
                        expense.getBalances().size());
            } else {
                for (Balance balance : expense.getBalances()) {
                    if (balance.getPaidTo().getUserId().equals(userId)) {
                        System.out.printf("Owes $%.2f to %s%n",
                                balance.getAmount(),
                                expense.getPaidBy().getUserId());
                        break;
                    }
                }
            }
        }
        System.out.println();
    }
}