package Bookington2.demo.dto.notification;

import Bookington2.demo.enums.NotificationType;
import lombok.Data;

import java.time.LocalDate;
@Data
public class PromotionNotificationDetailResponse implements NotificationDetailResponse {
    private NotificationType type;
    private Integer notificationId;
    private String promotionMessage;
    private LocalDate startDate;
    private LocalDate endDate;
}
