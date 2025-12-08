package Bookington2.demo.dto.player;

import Bookington2.demo.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBookingResponse {
    private Integer id;
    private String courtName;
    private String locationName;
    private String locationAddress;
    private LocalDate bookingDate;
    private List<Integer> startHours;
    private Float totalPrice;
    private BookingStatus status;
}

