package Bookington2.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {
    private Integer id;
    private Integer courtId;
    private String courtName;
    private String locationName;
    private String locationAddress;
    private LocalDate bookingDate;
    private Integer startTimeSlot;
    private Integer endTimeSlot;
    private String startTimeDisplay;
    private String endTimeDisplay;
    private Float totalPrice;
    private String status;
    private String paymentMethod;
    private String paymentMethodDisplay;
    private LocalDateTime createdAt;
    private String notes;
}
