package supermarket.exceptions;

public class ProductOutOfStockException extends Exception {
    public ProductOutOfStockException(String productName, int available, int requested) {
        super("Not enough stock for " + productName + 
              ". Available: " + available + ", Requested: " + requested);
    }
}