package Bookington2.demo.dto.player;

import Bookington2.demo.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailResponse {
    private Integer id;
    private Integer courtId;
    private String courtName;
    private Integer locationId;
    private String locationName;
    private String locationAddress;
    private String locationImage;
    private LocalDate bookingDate;
    private List<Integer> startHours;
    private Float totalPrice;
    private BookingStatus status;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private String paymentUrl; // For online payment
}

