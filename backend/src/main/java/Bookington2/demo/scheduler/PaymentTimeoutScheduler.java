package Bookington2.demo.scheduler;

import Bookington2.demo.service.EnhancedBookingPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentTimeoutScheduler {

    @Autowired
    private EnhancedBookingPaymentService bookingPaymentService;

    /**
     * Process expired payments every 5 minutes
     * Runs every 5 minutes to check for payments that have expired (15 minutes timeout)
     */
    @Scheduled(fixedRate = 300000) // 5 minutes in milliseconds
    public void processExpiredPayments() {
        try {
            bookingPaymentService.processExpiredPayments();
        } catch (Exception e) {
            // Log error but don't throw to prevent scheduler from stopping
            System.err.println("Error processing expired payments: " + e.getMessage());
        }
    }

    /**
     * Process expired payments every minute for more frequent checking
     * This can be used for more critical time-sensitive operations
     */
    @Scheduled(fixedRate = 60000) // 1 minute in milliseconds
    public void processHighPriorityExpiredPayments() {
        try {
            // Could implement more frequent checks for specific payment methods
            // or high-value transactions
            bookingPaymentService.processExpiredPayments();
        } catch (Exception e) {
            System.err.println("Error in high priority expired payment processing: " + e.getMessage());
        }
    }
}
