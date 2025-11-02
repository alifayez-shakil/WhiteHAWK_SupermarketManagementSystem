package supermarket.user;

public enum CustomerTier {
    REGULAR(0.0),    // No discount
    SILVER(0.05),    // 5% discount
    GOLD(0.10);      // 10% discount

    private final double discountRate;

    CustomerTier(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}