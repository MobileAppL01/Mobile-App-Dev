package Bookington2.demo.enums;

public enum PaymentStatus {
    PENDING("Payment is pending"),
    SUCCESS("Payment completed successfully"),
    FAILED("Payment failed"),
    EXPIRED("Payment expired"),
    REFUNDED("Payment refunded");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
