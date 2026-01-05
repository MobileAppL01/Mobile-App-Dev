package Bookington2.demo.entity;

import Bookington2.demo.converter.IntegerListConverter;
import Bookington2.demo.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    // JSON array of start hours: [17, 18, 19] means 17h-18h, 18h-19h, 19h-20h
    @Convert(converter = IntegerListConverter.class)
    @Column(name = "start_hours", columnDefinition = "text")
    private List<Integer> startHours;

    // Legacy fields for backward compatibility
    @Column(name = "start_time_slot")
    private Integer startTimeSlot;

    @Column(name = "end_time_slot")
    private Integer endTimeSlot;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "transaction_id")
    private String transactionId;


    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "payment_method")
    private String paymentMethod; // VNPAY, CASH

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private User player;

    @ManyToOne
    @JoinColumn(name = "court_id", referencedColumnName = "id")
    private Court court;

    @Version
    private Long version; // For optimistic locking

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
