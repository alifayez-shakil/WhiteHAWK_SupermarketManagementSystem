package supermarket.user;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerId;
    private String name;
    private CustomerTier tier;
    private List<Transaction> transactions;
    private double totalSpent;
    private int points;

    public Customer(int customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        this.tier = CustomerTier.REGULAR;
        this.transactions = new ArrayList<>();
        this.totalSpent = 0.0;
        this.points = 0;
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public CustomerTier getTier() { return tier; }
    public double getTotalSpent() { return totalSpent; }
    public List<Transaction> getTransactions() { return transactions; }
    public int getPoints() { return points; }

    /**
     * Adds a transaction to this customer and updates total spent and tier.
     * Note: Transaction should have already applied any discounts before calling this.
     */
    public void addTransaction(Transaction t) {
        transactions.add(t);
        totalSpent += t.getTotalAmount();
        updateTier();
    }

    private void updateTier() {
        CustomerTier oldTier = tier;
        
        // Fixed tier upgrade logic - only upgrade when thresholds are met
        if (totalSpent >= 50000) {
            tier = CustomerTier.GOLD;
        } else if (totalSpent >= 20000) {
            tier = CustomerTier.SILVER;
        } else {
            tier = CustomerTier.REGULAR;
        }

        if (oldTier != tier) {
            System.out.println(name + " has been upgraded to " + tier + " tier!");
        }
    }

    public void addPoints(int amount) {
        this.points += amount;
        // Points don't affect tier directly - only spending does
    }

    @Override
    public String toString() {
        return name + " (" + tier + ") - Total Spent: " + String.format("%.2f", totalSpent) + 
               " - Points: " + points;
    }
}