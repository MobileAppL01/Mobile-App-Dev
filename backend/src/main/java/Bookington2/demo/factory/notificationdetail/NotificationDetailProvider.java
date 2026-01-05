package Bookington2.demo.factory.notificationdetail;

import Bookington2.demo.dto.notification.NotificationDetailResponse;
import Bookington2.demo.enums.NotificationType;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class NotificationDetailProvider {
    private final Map<NotificationType, NotificationDetailFactory> notificationFactoryMap;
    public NotificationDetailProvider(List<NotificationDetailFactory<?>>  notificationFactoryList) {
        this.notificationFactoryMap = notificationFactoryList.stream().collect(Collectors.toMap(NotificationDetailFactory::getNotificationType, notificationDetailFactory -> notificationDetailFactory));
    }
    @SuppressWarnings("unchecked")
    public <T extends NotificationDetailResponse> NotificationDetailFactory<T> getNotificationDetailFactory(NotificationType notificationType) {
        return (NotificationDetailFactory<T>) notificationFactoryMap.get(notificationType);
    }
}
