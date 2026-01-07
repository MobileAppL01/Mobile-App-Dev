package Bookington2.demo.service;

import Bookington2.demo.dto.notification.*;
import Bookington2.demo.entity.Notification;
import Bookington2.demo.factory.notification.NotificationFactory;
import Bookington2.demo.factory.notification.NotificationFactoryProvider;
import Bookington2.demo.factory.notificationdetail.NotificationDetailFactory;
import Bookington2.demo.factory.notificationdetail.NotificationDetailProvider;
import Bookington2.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    private NotificationFactoryProvider notificationFactoryProvider;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationDetailProvider notificationDetailProvider;

    public Notification createNotification(CreateNotificationPayload createNotificationPayload) {
        NotificationFactory factory = notificationFactoryProvider
                .getNotificationFactory(createNotificationPayload.getType());
        return notificationRepository.save(factory.createNotification(createNotificationPayload));
    }

    public ResponseEntity<CreatePromotionNotificationResponse> createPromotionNotification(
            CreatePromotionNotificationPayload createPromotionNotificationPayload) {
        Notification notification = createNotification(createPromotionNotificationPayload);
        CreatePromotionNotificationResponse response = new CreatePromotionNotificationResponse();
        response.setCreatedAt(notification.getCreatedAt());
        response.setMessage(notification.getPromotionMessage());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<BriefNotificationResponse>> getListNotification(Integer userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrIsPromotionType(userId);
        return ResponseEntity.ok(notifications.stream().map(this::toBriefNotificationResponse).toList());
    }

    public ResponseEntity<NotificationDetailResponse> getNotification(Integer notificationId, Integer userId) {
        Notification notification = notificationRepository.findById(notificationId).get();
        if (notification.getUser() != null && !Objects.equals(notification.getUser().getId(), userId)) {
            return ResponseEntity.badRequest().build();
        }
        notification.setChecked(true);
        notificationRepository.save(notification);
        NotificationDetailFactory<?> notificationDetailFactory = notificationDetailProvider
                .getNotificationDetailFactory(notification.getType());
        return ResponseEntity.status(201)
                .body(notificationDetailFactory.createNotificationDetailResponse(notification));
    }

    public BriefNotificationResponse toBriefNotificationResponse(Notification notification) {
        BriefNotificationResponse response = new BriefNotificationResponse();
        response.setCreatedAt(notification.getCreatedAt());
        response.setNotificationId(notification.getId());
        response.setBriefNotification(notification.getBriefMessage());
        response.setNotificationIsRead(notification.getChecked());
        response.setType(notification.getType());
        return response;
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteNotification(Integer notificationId, Integer userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (notification.getUser() != null && notification.getUser().getId().equals(userId)) {
            notificationRepository.delete(notification);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteAllNotifications(Integer userId) {
        notificationRepository.deleteByUserId(userId);
    }
}
