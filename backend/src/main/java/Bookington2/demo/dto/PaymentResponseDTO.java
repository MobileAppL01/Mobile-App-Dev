package Bookington2.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private String paymentId;
    private String bookingId;
    private String paymentUrl;
    private String qrCodeUrl;
    private String status;
    private String message;
    private String bankAccount;
    private String bankName;
    private String accountName;
    private String amount;
    private String content;
    private Long expiryTime;
}
