package supermarket.database;

import supermarket.user.Product;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProductDatabase {
    private final Map<Integer, Product> productMap;

    public ProductDatabase() {
        productMap = new HashMap<>();
        seedProducts();
    }

    private void seedProducts() {
        // Grocery
        addProduct(new Product(1, "Rice", 50.0, "Grocery", 100));
        addProduct(new Product(2, "Oil", 150.0, "Grocery", 50));
        addProduct(new Product(3, "Salt", 25.0, "Grocery", 200));
        addProduct(new Product(4, "Sugar", 60.0, "Grocery", 150));

        // Personal Care
        addProduct(new Product(5, "Soap", 30.0, "Personal Care", 200));
        addProduct(new Product(6, "Body Wash", 130.0, "Personal Care", 120));
        addProduct(new Product(7, "Shampoo", 70.0, "Personal Care", 100));
        addProduct(new Product(8, "Toothbrush", 20.0, "Personal Care", 80));
        addProduct(new Product(9, "Toothpaste", 50.0, "Personal Care", 90));

        // Dairy Products
        addProduct(new Product(10, "Milk", 100.0, "Dairy Product", 80));
        addProduct(new Product(11, "Cheese", 250.0, "Dairy Product", 40));
        addProduct(new Product(12, "Butter", 200.0, "Dairy Product", 30));

        // Fruits
        addProduct(new Product(13, "Apples", 80.0, "Fruits", 60));
        addProduct(new Product(14, "Bananas", 40.0, "Fruits", 100));
        addProduct(new Product(15, "Oranges", 90.0, "Fruits", 70));
        addProduct(new Product(16, "Mangoes", 120.0, "Fruits", 50));

        // Vegetables
        addProduct(new Product(17, "Potatoes", 40.0, "Vegetables", 200));
        addProduct(new Product(18, "Onions", 50.0, "Vegetables", 180));
        addProduct(new Product(19, "Tomatoes", 70.0, "Vegetables", 100));
        addProduct(new Product(20, "Carrots", 60.0, "Vegetables", 90));

        // Meat
        addProduct(new Product(21, "Chicken", 280.0, "Meat", 40));
        addProduct(new Product(22, "Beef", 550.0, "Meat", 30));
        addProduct(new Product(23, "Fish", 300.0, "Meat", 35));

        // Bakery
        addProduct(new Product(24, "Bread", 40.0, "Bakery", 100));
        addProduct(new Product(25, "Cake", 300.0, "Bakery", 20));
        addProduct(new Product(26, "Cookies", 150.0, "Bakery", 50));

        // Snacks & Beverages
        addProduct(new Product(27, "Biscuits", 50.0, "Snacks and Beverages", 70));
        addProduct(new Product(28, "Chips", 60.0, "Snacks and Beverages", 90));
        addProduct(new Product(29, "Soft Drink", 35.0, "Snacks and Beverages", 200));
        addProduct(new Product(30, "Juice", 120.0, "Snacks and Beverages", 60));

        // Electronics
        addProduct(new Product(31, "Blender", 4300.0, "Electronics", 20));
        addProduct(new Product(32, "Mixed Grinder", 4550.0, "Electronics", 10));
        addProduct(new Product(33, "Toaster", 1300.0, "Electronics", 35));
        addProduct(new Product(34, "Electric Kettle", 1070.0, "Electronics", 15));
    }

    public void addProduct(Product p) { productMap.put(p.getProductId(), p); }
    public Product getProductById(int id) { return productMap.get(id); }

    public void printAllProducts() {
        System.out.println("\n=== Product List ===");
        System.out.printf("%-4s %-20s %-15s %-10s %-10s%n", "ID", "Name", "Category", "Price(Tk)", "Stock");
        for (Product p : productMap.values()) {
            System.out.printf("%-4d %-20s %-15s %-10.2f %-10d%n",
                    p.getProductId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
        }
    }

    // Expose product collection (used when seeding transactions)
    public Collection<Product> getAllProducts() { return productMap.values(); }
}
