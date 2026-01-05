package Bookington2.demo.dto.notification;

import Bookington2.demo.enums.BookingStatus;
import Bookington2.demo.enums.NotificationType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaymentSuccessNotificationDetailResponse implements NotificationDetailResponse {
    private NotificationType type;
    private Integer notificationId;
    private String courtName;
    private String locationName;
    private String locationAddress;
    private String locationImage;
    private LocalDate bookingDate;
    private List<Integer> startHours;
    private Float totalPrice;
    private String paymentMethod;
}
