package Bookington2.demo.factory.notificationdetail;

import Bookington2.demo.dto.notification.PromotionNotificationDetailResponse;
import Bookington2.demo.entity.Notification;
import Bookington2.demo.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class PromotionDetailFactory implements NotificationDetailFactory<PromotionNotificationDetailResponse> {
    @Override
    public NotificationType getNotificationType() {
        return NotificationType.PROMOTION;
    }

    @Override
    public PromotionNotificationDetailResponse createNotificationDetailResponse(Notification notification) {
        PromotionNotificationDetailResponse response = new PromotionNotificationDetailResponse();
        response.setNotificationId(notification.getId());
        response.setPromotionMessage(notification.getPromotionMessage());
        response.setStartDate(notification.getStartDate());
        response.setEndDate(notification.getEndDate());
        return response;
    }
}
