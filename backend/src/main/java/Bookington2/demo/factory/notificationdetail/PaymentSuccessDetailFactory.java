package Bookington2.demo.factory.notificationdetail;

import Bookington2.demo.dto.notification.NotificationDetailResponse;
import Bookington2.demo.dto.notification.PaymentSuccessNotificationDetailResponse;
import Bookington2.demo.entity.Notification;
import Bookington2.demo.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class PaymentSuccessDetailFactory implements NotificationDetailFactory<PaymentSuccessNotificationDetailResponse> {
    @Override
    public NotificationType getNotificationType() {
        return NotificationType.PAYMENT_SUCCESS;
    }

    @Override
    public PaymentSuccessNotificationDetailResponse createNotificationDetailResponse(Notification notification) {
        PaymentSuccessNotificationDetailResponse response = new PaymentSuccessNotificationDetailResponse();
        response.setNotificationId(notification.getId());
        response.setBookingDate(notification.getBooking().getBookingDate());
        response.setCourtName(notification.getBooking().getCourt().getName());
        response.setType(NotificationType.PAYMENT_SUCCESS);
        response.setPaymentMethod(notification.getBooking().getPaymentMethod());
        response.setLocationAddress(notification.getBooking().getCourt().getLocation().getAddress());
        response.setLocationName(notification.getBooking().getCourt().getLocation().getName());
        response.setLocationImage(notification.getBooking().getCourt().getLocation().getImage());
        response.setTotalPrice(notification.getBooking().getTotalPrice());
        response.setStartHours(notification.getBooking().getStartHours());
        return response;
    }
}
