package Bookington2.demo.dto.notification;

import Bookington2.demo.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BriefNotificationResponse {
    Integer notificationId;
    NotificationType type;
    LocalDateTime createdAt;
    String briefNotification;
    Boolean notificationIsRead;
}
