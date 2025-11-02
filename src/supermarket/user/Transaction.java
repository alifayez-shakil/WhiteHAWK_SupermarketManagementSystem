package supermarket.user;

import supermarket.exceptions.ProductOutOfStockException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Transaction {
    private final int transactionId;
    private final Customer customer;
    private final List<TransactionItem> items;
    private double totalAmount;
    private double discountAmount;
    private boolean finalized = false;

    public Transaction(int transactionId, Customer customer) {
        this.transactionId = transactionId;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
        this.discountAmount = 0.0;
    }

    public void addItem(Product product, int quantity) throws ProductOutOfStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        
        if (product.getStock() < quantity) {
            throw new ProductOutOfStockException(
                    product.getName(), quantity, product.getStock());
        }

        product.reduceStock(quantity);
        TransactionItem item = new TransactionItem(product, quantity);
        items.add(item);
        totalAmount += product.getPrice() * quantity;
    }

    public double getTotalAmount() { return totalAmount; }
    public List<TransactionItem> getItems() { return Collections.unmodifiableList(items); }

    /**
     * Apply discounts based on customer tier and then record the transaction on the customer.
     */
    public void finalizeTransaction() {
        if (finalized) return;

        // Apply tier-based discounts
        double discountRate = customer.getTier().getDiscountRate();
        discountAmount = totalAmount * discountRate;
        totalAmount -= discountAmount;

        // Now add to customer record
        customer.addTransaction(this);
        finalized = true;
    }

    public Customer getCustomer() { return customer; }
    public int getTransactionId() { return transactionId; }
    public double getDiscountAmount() { return discountAmount; }

    @Override
    public String toString() {
        return "Transaction #" + transactionId + " for " +
                customer.getName() + " | Total: " + String.format("%.2f", totalAmount);
    }

    // Print a detailed receipt for this transaction
    public String printReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction ID: ").append(transactionId).append("\n");
        sb.append("Customer: ").append(customer.getName()).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-5s %-20s %-10s %-5s %-10s\n", "ID", "Product", "Price(Tk)", "Qty", "Subtotal"));

        for (TransactionItem item : items) {
            Product p = item.getProduct();
            int qty = item.getQuantity();
            double subtotal = p.getPrice() * qty;
            sb.append(String.format("%-5d %-20s %-10.2f %-5d %-10.2f\n",
                    p.getProductId(), p.getName(), p.getPrice(), qty, subtotal));
        }

        sb.append("--------------------------------------------------\n");
        
        if (discountAmount > 0) {
            sb.append(String.format("Discount Applied: %.0f%% (%s Tier)\n", 
                    customer.getTier().getDiscountRate() * 100, 
                    customer.getTier()));
            sb.append(String.format("Discount Amount: Tk %.2f\n", discountAmount));
        }
        
        sb.append(String.format("TOTAL: Tk %.2f", totalAmount));

        return sb.toString();
    }
}