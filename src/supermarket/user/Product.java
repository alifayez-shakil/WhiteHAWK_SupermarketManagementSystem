package supermarket.user;

import java.util.Objects;

public class Product {
    private final int productId;
    private final String name;
    private final double price;
    private final String category;
    private int stock;

    public Product(int productId, String name, double price, String category, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public synchronized int getStock() { return stock; }

    public synchronized void reduceStock(int quantity) { this.stock -= quantity; }
    public synchronized void increaseStock(int quantity) { this.stock += quantity; }

    @Override
    public String toString() {
        return name + " (" + category + ") - " + price + " Tk [" + stock + " in stock]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return productId == product.productId;
    }

    @Override
    public int hashCode() { return Objects.hash(productId); }
}
