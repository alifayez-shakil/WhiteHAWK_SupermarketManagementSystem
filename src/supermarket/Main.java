package supermarket;

import supermarket.database.CustomerDatabase;
import supermarket.database.ProductDatabase;
import supermarket.exceptions.CustomerNotFoundException;
import supermarket.exceptions.ProductOutOfStockException;
import supermarket.services.ReportService;
import supermarket.user.*;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ProductDatabase productDB = new ProductDatabase();
        CustomerDatabase customerDB = new CustomerDatabase(productDB);
        ReportService reportService = new ReportService(productDB, customerDB);

        boolean running = true;
        while (running) {
            System.out.println("\n=== Supermarket Management System ===");
            System.out.println("1. View Products");
            System.out.println("2. View Customers by Tier");
            System.out.println("3. Place Order");
            System.out.println("4. Generate Reports");
            System.out.println("5. Add New Customer");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    productDB.printAllProducts();
                    break;
                case 2:
                    customerDB.printCustomersByTier();
                    break;
                case 3:
                    try {
                        placeOrder(productDB, customerDB);
                    } catch (CustomerNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }
                    break;

                case 4:
                    generateReports(reportService);
                    break;
                case 5:
                    addCustomer(customerDB);
                    break;
                case 6:
                    running = false;
                    System.out.println("Thanks for coming, goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }

        scanner.close();
    }

    private static void addCustomer(CustomerDatabase customerDB) {
        System.out.print("Enter Customer ID: ");
        int id = getIntInput();

        if (customerDB.getCustomerById(id) != null) {
            System.out.println("Customer ID already exists!");
            return;
        }

        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();

        Customer c = new Customer(id, name);
        customerDB.addCustomer(c);
        System.out.println("Customer added successfully: " + c.getName());
    }

private static void placeOrder(ProductDatabase productDB, CustomerDatabase customerDB)
        throws CustomerNotFoundException {

    System.out.print("Enter Customer ID: ");
    int custId = getIntInput();
    Customer customer = customerDB.getCustomerById(custId);

    if (customer == null) {
        throw new CustomerNotFoundException(custId);
    }

    System.out.println("Placing order for Customer: " + customer.getName());

    Transaction transaction = new Transaction((int) (Math.random() * 10000), customer);

    boolean ordering = true;
    while (ordering) {
        System.out.println("\n=== Product Catalog ===");
        productDB.printAllProducts();
        System.out.print("Enter Product ID to add (0 to checkout): ");
        int prodId = getIntInput();

        if (prodId == 0) {
            ordering = false;
            continue;
        }

        Product product = productDB.getProductById(prodId);
        if (product == null) {
            System.out.println("Invalid product ID!");
            continue;
        }

        System.out.print("Enter Quantity: ");
        int qty = getIntInput();

        if (qty <= 0) {
            System.out.println("Quantity must be positive!");
            continue;
        }

        try {
            if (product.getStock() < qty) {
                throw new ProductOutOfStockException(product.getName(), product.getStock(), qty);
            }

            transaction.addItem(product, qty);
            System.out.println(qty + " x " + product.getName() + " added.");
            
        } catch (ProductOutOfStockException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please try again with a different quantity or product.");
        }
    }

    if (transaction.getItems().isEmpty()) {
        System.out.println("No items were selected — order cancelled.");
        return;
    }

    transaction.finalizeTransaction();
    System.out.println("\n=== Transaction Receipt ===");
    System.out.println(transaction.printReceipt());
    System.out.println("Order placed successfully for " + customer.getName() + "!");
    
    // Update customer points based on purchase amount
    customer.addPoints((int) transaction.getTotalAmount());
}
    private static void generateReports(ReportService reportService) {
        System.out.println("\n=== Reporting Menu ===");
        System.out.println("1. Sold Products");
        System.out.println("2. Customer Receipts");
        System.out.print("Enter report choice: ");
        int reportChoice = getIntInput();

        switch (reportChoice) {
            case 1:
                reportService.generateTopSellingProductsReport();
                break;
            case 2:
                System.out.print("Enter Customer ID (0 for all): ");
                int custId = getIntInput();
                if (custId == 0) {
                    reportService.generateCustomerReceiptsReport();
                } else {
                    reportService.generateCustomerReceiptsReport(custId);
                }
                break;
            default:
                System.out.println("Invalid report choice.");
        }
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number!");
            scanner.nextLine();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline
        return value;
    }
}