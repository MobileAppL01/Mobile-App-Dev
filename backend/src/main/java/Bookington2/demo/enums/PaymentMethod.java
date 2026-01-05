package Bookington2.demo.enums;

public enum PaymentMethod {
    CASH("Cash"),
    VNPAY("VNPAY"),
    MOMO("MoMo"),
    ZALO_PAY("ZaloPay"),
    BANK_TRANSFER("Bank Transfer"),
    CREDIT_CARD("Credit Card");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
