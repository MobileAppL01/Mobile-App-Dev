package Bookington2.demo.factory.notification;

import Bookington2.demo.dto.notification.CreatePaymentSuccessNotificationPayload;
import Bookington2.demo.entity.Booking;
import Bookington2.demo.entity.Notification;
import Bookington2.demo.entity.User;
import Bookington2.demo.enums.NotificationType;
import Bookington2.demo.repository.BookingRepository;
import Bookington2.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentSuccessNotificationFactory implements NotificationFactory<CreatePaymentSuccessNotificationPayload> {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public NotificationType getType() {
        return NotificationType.PAYMENT_SUCCESS;
    }

    @Override
    public Notification createNotification( CreatePaymentSuccessNotificationPayload payload) {
        Notification notification = new Notification();
        Booking booking = bookingRepository.getReferenceById(payload.getBookingId());
        User user = userRepository.getReferenceById(payload.getUserId());
        notification.setType(NotificationType.PAYMENT_SUCCESS);
        notification.setBooking(booking);
        notification.setUser(user);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setBriefMessage("Bạn đã đặt sân thành công");
        return notification;
    }
}
