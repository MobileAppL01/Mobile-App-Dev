package Bookington2.demo.factory.notificationdetail;

import Bookington2.demo.dto.notification.NotificationDetailResponse;
import Bookington2.demo.entity.Notification;
import Bookington2.demo.enums.NotificationType;

public interface NotificationDetailFactory <T extends NotificationDetailResponse> {
    NotificationType getNotificationType();
    T createNotificationDetailResponse(Notification notification);

}
