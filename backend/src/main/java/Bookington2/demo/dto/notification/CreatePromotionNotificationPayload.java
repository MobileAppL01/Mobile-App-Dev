package Bookington2.demo.dto.notification;

import Bookington2.demo.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePromotionNotificationPayload implements CreateNotificationPayload {
    private NotificationType type;
    private String message;
    private String briefMessage;
    private LocalDate startDate;
    private LocalDate endDate;
    @Override
    public NotificationType getType() {
        return this.type;
    }
}
