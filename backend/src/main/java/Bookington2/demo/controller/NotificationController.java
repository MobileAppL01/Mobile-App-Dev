package Bookington2.demo.controller;

import Bookington2.demo.dto.notification.CreatePromotionNotificationPayload;
import Bookington2.demo.dto.notification.CreatePromotionNotificationResponse;
import Bookington2.demo.enums.NotificationType;
import Bookington2.demo.service.NotificationService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/api/v1/admin")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @PostMapping("/notifications")
    public ResponseEntity<CreatePromotionNotificationResponse> createPromotionNotification(CreatePromotionNotificationPayload createPromotionNotificationPayload) {
        return notificationService.createPromotionNotification(createPromotionNotificationPayload);
    }
}
