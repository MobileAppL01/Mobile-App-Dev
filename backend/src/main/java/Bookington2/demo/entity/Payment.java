package Bookington2.demo.entity;

import Bookington2.demo.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method")
    private String paymentMethod; // VNPAY, SePay, MoMo, Cash, etc.

    @Column(name = "payment_id")
    private String paymentId; // ID from payment gateway

    @Column(name = "transaction_id")
    private String transactionId; // Transaction ID from payment gateway

    @Column(name = "order_reference")
    private String orderReference; // Order reference for tracking

    @Column(name = "gateway_response", columnDefinition = "TEXT")
    private String gatewayResponse; // Raw response from payment gateway

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Column(name = "payment_url")
    private String paymentUrl;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version; // For optimistic locking

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Set expiry time to 15 minutes from creation
        if (expiredAt == null) {
            expiredAt = createdAt.plusMinutes(15);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public void markAsSuccessful(String transactionId, String gatewayResponse) {
        this.status = PaymentStatus.SUCCESS;
        this.transactionId = transactionId;
        this.gatewayResponse = gatewayResponse;
        this.paidAt = LocalDateTime.now();
    }

    public void markAsFailed(String transactionId, String gatewayResponse) {
        this.status = PaymentStatus.FAILED;
        this.transactionId = transactionId;
        this.gatewayResponse = gatewayResponse;
    }

    public void markAsExpired() {
        this.status = PaymentStatus.EXPIRED;
    }

    public void markAsRefunded(String transactionId, String gatewayResponse) {
        this.status = PaymentStatus.REFUNDED;
        this.transactionId = transactionId;
        this.gatewayResponse = gatewayResponse;
    }
}
