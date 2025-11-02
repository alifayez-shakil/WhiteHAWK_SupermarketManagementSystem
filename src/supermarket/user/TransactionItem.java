package supermarket.user;

public class TransactionItem {
    private final Product product;
    private final int quantity;

    public TransactionItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        return product.getName() + " x" + quantity;
    }
}
