package Bookington2.demo.dto;
import Bookington2.demo.enums.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDTO {
    // Court Information
    private String courtName;
    private String courtAddress;
    
    // Booking Information
    private LocalDate bookingDate;
    private Integer courtNumber;
    private Integer startTimeSlot;
    private Integer endTimeSlot;
    private String startTimeDisplay;
    private String endTimeDisplay;
    private Integer totalHours;
    private Float totalPrice;
    
    // Payment Information
    private String bookingId;
    private String status;
    private List<PaymentMethod> availablePaymentMethods;
    
    // Time Information
    private LocalTime courtOpenTime;
    private LocalTime courtCloseTime;
    
    // QR Code Information
    private String qrCodeUrl;
    private String paymentUrl;
    private String bankAccount;
    private String bankName;
    private String accountName;
    private String amount;
    private String content;
}
