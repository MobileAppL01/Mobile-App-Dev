package Bookington2.demo.enums;

public enum BookingStatus {
    PENDING("Booking created, waiting for payment"),
    CONFIRMED("Payment initiated, slot reserved"),
    COMPLETED("Payment successful, booking finalized"),
    CANCELED("Booking canceled by user"),
    EXPIRED("Payment timeout, booking expired");

    private final String description;

    BookingStatus(String description) {
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

