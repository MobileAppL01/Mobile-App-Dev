package Bookington2.demo.dto.request;

import Bookington2.demo.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmationRequestDTO {
    @NotBlank(message = "Booking ID is required")
    private String bookingId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10-11 digits")
    private String phoneNumber;

    private String notes;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
