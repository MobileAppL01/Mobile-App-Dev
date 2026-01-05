package Bookington2.demo.entity;

import Bookington2.demo.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String promotionMessage;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private String briefMessage;
    private Boolean checked=false;
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
