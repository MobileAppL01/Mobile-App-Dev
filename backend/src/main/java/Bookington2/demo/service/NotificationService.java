package Bookington2.demo.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    public NotificationService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public void sendPromotionNotification(String message) throws FirebaseMessagingException {
        Message msg = (Message) com.google.firebase.messaging.Message.builder().setTopic("Promotion").putData("Message","Giam gia 10%").build();
        firebaseMessaging.send((com.google.firebase.messaging.Message) msg);
    }
}
