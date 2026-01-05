package Bookington2.demo.factory.notification;

import Bookington2.demo.dto.notification.CreatePromotionNotificationPayload;
import Bookington2.demo.entity.Notification;
import Bookington2.demo.enums.NotificationType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class PromotionNotificationFactory implements NotificationFactory<CreatePromotionNotificationPayload> {
    @Override
    public NotificationType getType() {
        return NotificationType.PROMOTION;
    }

    @Override
    public Notification createNotification(CreatePromotionNotificationPayload payload) {
        Notification notification = new Notification();
        notification.setType(NotificationType.PROMOTION);
        notification.setPromotionMessage(payload.getMessage());
        notification.setStartDate(payload.getStartDate());
        notification.setEndDate(payload.getEndDate());
        notification.setCreatedAt(LocalDateTime.now());
        return notification;
    }
}
