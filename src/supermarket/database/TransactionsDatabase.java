package supermarket.database;

import supermarket.user.Transaction;
import supermarket.user.Customer;

import java.util.*;

/**
 * Handles all transaction records in the supermarket.
 */
public class TransactionsDatabase {

    // Store transactions by transactionId
    private final Map<Integer, Transaction> transactionMap;

    // Store transactions by customerId
    private final Map<Integer, List<Transaction>> customerTransactions;

    private int transactionCounter;

    public TransactionsDatabase() {
        this.transactionMap = new HashMap<>();
        this.customerTransactions = new HashMap<>();
        this.transactionCounter = 1; // auto-increment transaction ID
    }

    /**
     * Adds a transaction to the database.
     */
    public void addTransaction(Transaction t) {
        if (t == null) return;

        // Ensure unique transactionId
        if (t.getTransactionId() <= 0) {
            try {
                java.lang.reflect.Field idField = Transaction.class.getDeclaredField("transactionId");
                idField.setAccessible(true);
                idField.set(t, transactionCounter++);
            } catch (Exception e) {
                throw new RuntimeException("Failed to assign transaction ID", e);
            }
        }

        transactionMap.put(t.getTransactionId(), t);

        // Add to customer-specific history
        Customer c = t.getCustomer();
        if (c != null) {
            customerTransactions
                .computeIfAbsent(c.getCustomerId(), k -> new ArrayList<>())
                .add(t);
        }
    }

    /**
     * Retrieve a transaction by its ID.
     */
    public Transaction getTransactionById(int id) {
        return transactionMap.get(id);
    }

    /**
     * Retrieve all transactions for a customer.
     */
    public List<Transaction> getTransactionsByCustomer(int customerId) {
        return customerTransactions.getOrDefault(customerId, Collections.emptyList());
    }

    /**
     * Retrieve all transactions in the system.
     */
    public Collection<Transaction> getAllTransactions() {
        return transactionMap.values();
    }

    /**
     * Prints all transactions in a clean format.
     */
    public void printAllTransactions() {
        System.out.println("\n=== All Transactions ===");
        if (transactionMap.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }
        for (Transaction t : transactionMap.values()) {
            System.out.println(t);
        }
    }
}
