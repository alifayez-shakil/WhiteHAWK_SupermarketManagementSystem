package supermarket.services;

import supermarket.database.CustomerDatabase;
import supermarket.database.ProductDatabase;
import supermarket.user.Customer;
import supermarket.user.Product;
import supermarket.user.Transaction;
import supermarket.user.TransactionItem;

import java.util.*;
import java.util.stream.Collectors;

public class ReportService {

    private final ProductDatabase productDB;
    private final CustomerDatabase customerDB;

    public ReportService(ProductDatabase productDB, CustomerDatabase customerDB) {
        this.productDB = productDB;
        this.customerDB = customerDB;
    }

    /** Top 5 selling products report (by units, then revenue). */
    public void generateTopSellingProductsReport() {
        System.out.println("\n=== Top 5 Selling Products Report ===");

        Map<Product, ProductSales> salesData = new HashMap<>();
        
        // Collect sales data from actual transactions
        for (Customer customer : customerDB.getAllCustomers()) {
            for (Transaction transaction : customer.getTransactions()) {
                for (TransactionItem item : transaction.getItems()) {
                    Product product = item.getProduct();
                    int quantity = item.getQuantity();
                    double revenue = product.getPrice() * quantity;
                    
                    ProductSales current = salesData.getOrDefault(product, new ProductSales(0, 0.0));
                    salesData.put(product, current.merge(new ProductSales(quantity, revenue)));
                }
            }
        }

        if (salesData.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }

        System.out.printf("%-6s %-25s %10s %12s%n", "ID", "Product", "Units", "Revenue");
        System.out.println("-----------------------------------------------------------");

        // Sort by units sold (descending) then by revenue (descending)
        salesData.entrySet().stream()
            .sorted((e1, e2) -> {
                int unitCompare = Integer.compare(e2.getValue().getUnits(), e1.getValue().getUnits());
                if (unitCompare != 0) return unitCompare;
                return Double.compare(e2.getValue().getRevenue(), e1.getValue().getRevenue());
            })
            .limit(5)
            .forEach(entry -> {
                Product p = entry.getKey();
                ProductSales ps = entry.getValue();
                System.out.printf("%-6d %-25s %10d %12.2f%n",
                        p.getProductId(), p.getName(), ps.getUnits(), ps.getRevenue());
            });
    }

    /** Print receipts for all customers */
    public void generateCustomerReceiptsReport() {
        System.out.println("\n=== Customer Receipts ===");
        boolean any = false;
        for (Customer c : customerDB.getAllCustomers()) {
            if (!c.getTransactions().isEmpty()) {
                any = true;
                for (Transaction t : c.getTransactions()) {
                    System.out.println(t.printReceipt());
                    System.out.println();
                }
            }
        }
        if (!any) {
            System.out.println("No transactions have been recorded yet.");
        }
    }

    /** Print receipts for a specific customer */
    public void generateCustomerReceiptsReport(int customerId) {
        Customer c = customerDB.getCustomerById(customerId);
        if (c == null) {
            System.out.println("Customer ID " + customerId + " not found.");
            return;
        }
        if (c.getTransactions().isEmpty()) {
            System.out.println("No receipts for " + c.getName());
            return;
        }
        System.out.println("\n=== Receipts for " + c.getName() + " ===");
        for (Transaction t : c.getTransactions()) {
            System.out.println(t.printReceipt());
            System.out.println();
        }
    }

    /** Helper class for sales aggregation. */
    private static class ProductSales {
        private final int units;
        private final double revenue;

        ProductSales(int units, double revenue) {
            this.units = units;
            this.revenue = revenue;
        }

        int getUnits() { return units; }
        double getRevenue() { return revenue; }

        ProductSales merge(ProductSales other) {
            return new ProductSales(this.units + other.units,
                                    this.revenue + other.revenue);
        }
    }
}