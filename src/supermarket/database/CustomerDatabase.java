package supermarket.database;

import supermarket.user.Customer;
import supermarket.user.CustomerTier;
import java.util.*;

public class CustomerDatabase {
    private final Map<Integer, Customer> customerMap;

    public CustomerDatabase(ProductDatabase productDB) {
        this.customerMap = new HashMap<>();
        seedCustomers(); // Only create customers, no transactions
    }

    private void seedCustomers() {
        // Create customers without any preloaded transactions
        String[] names = {
            "Aushfi", "Arpa", "Mehedi", "Fayez", "Shakil", 
            "Akash", "Zidan", "Sakib", "Nafi", "Abrar",
            "Rayhan", "Sami", "Rakibul", "Adnan", "Hasina", "Asif"
        };
        
        for (int i = 0; i < names.length; i++) {
            Customer c = new Customer(i + 1, names[i]);
            customerMap.put(c.getCustomerId(), c);
        }
    }

    public void addCustomer(Customer customer) { 
        customerMap.put(customer.getCustomerId(), customer); 
    }
    
    public Customer getCustomerById(int id) { 
        return customerMap.get(id); 
    }

    public Map<CustomerTier, List<Customer>> getCustomersByTier() {
        Map<CustomerTier, List<Customer>> customersByTier = new HashMap<>();
        
        // Initialize lists for each tier
        for (CustomerTier tier : CustomerTier.values()) {
            customersByTier.put(tier, new ArrayList<>());
        }
        
        // Group customers by tier
        for (Customer customer : customerMap.values()) {
            customersByTier.get(customer.getTier()).add(customer);
        }
        
        return customersByTier;
    }

    public void printCustomersByTier() {
        Map<CustomerTier, List<Customer>> byTier = getCustomersByTier();
        for (CustomerTier tier : CustomerTier.values()) {
            System.out.println("\n=== " + tier + " Customers ===");
            List<Customer> list = byTier.getOrDefault(tier, Collections.emptyList());
            if (list.isEmpty()) {
                System.out.println("No customers in this tier.");
            } else {
                System.out.printf("%-4s %-15s %-10s %-15s%n", "ID", "Name", "Tier", "Total Spent(Tk)");
                for (Customer c : list) {
                    System.out.printf("%-4d %-15s %-10s %-15.2f%n",
                            c.getCustomerId(), c.getName(), c.getTier(), c.getTotalSpent());
                }
            }
        }
    }

    public Collection<Customer> getAllCustomers() { 
        return customerMap.values(); 
    }
}