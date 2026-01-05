package Bookington2.demo.factory.notification;

import Bookington2.demo.dto.notification.CreatePaymentSuccessNotificationPayload;
import Bookington2.demo.enums.NotificationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class NotificationFactoryProvider {
private final Map<NotificationType, NotificationFactory<?>> notificationFactoryMap;

    public NotificationFactoryProvider(List<NotificationFactory<?>> notificationFactoryList) {

        this.notificationFactoryMap = notificationFactoryList.stream().collect(Collectors.toMap(NotificationFactory::getType, notificationFactory -> notificationFactory));
    }
    @SuppressWarnings("unchecked")
    public <T extends CreatePaymentSuccessNotificationPayload> NotificationFactory<T> getNotificationFactory(NotificationType notificationType) {
        return (NotificationFactory<T>) notificationFactoryMap.get(notificationType);
    }
}
