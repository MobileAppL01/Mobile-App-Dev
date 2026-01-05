package Bookington2.demo.dto.notification;

import Bookington2.demo.enums.NotificationType;
import lombok.Data;

@Data
public class CreatePaymentSuccessNotificationPayload implements CreateNotificationPayload {
    private NotificationType type;
    private Integer bookingId;
    private Integer userId;
    @Override
    public NotificationType getType() {
        return this.type;
    }
}
