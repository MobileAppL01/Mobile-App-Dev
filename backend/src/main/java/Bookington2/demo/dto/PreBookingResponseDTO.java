package Bookington2.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreBookingResponseDTO {
    private String bookingId;
    private Integer courtId;
    private String courtName;
    private String courtAddress;
    private LocalDate bookingDate;
    private Integer startTimeSlot;
    private Integer endTimeSlot;
    private String startTimeDisplay;
    private String endTimeDisplay;
    private Integer totalHours;
    private Float pricePerHour;
    private Float totalPrice;
    private Boolean available;
    private String status;
    private LocalTime courtOpenTime;
    private LocalTime courtCloseTime;
}
