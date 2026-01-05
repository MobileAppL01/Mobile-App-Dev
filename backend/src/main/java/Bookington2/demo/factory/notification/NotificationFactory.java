package Bookington2.demo.factory.notification;

import Bookington2.demo.dto.notification.CreateNotificationPayload;
import Bookington2.demo.entity.Notification;
import Bookington2.demo.enums.NotificationType;

public interface NotificationFactory <T extends CreateNotificationPayload> {
    NotificationType getType();
    Notification createNotification(T payload);
}
