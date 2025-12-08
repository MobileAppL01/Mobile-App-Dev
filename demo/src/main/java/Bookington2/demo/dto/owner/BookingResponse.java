package Bookington2.demo.dto.owner;

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
public class BookingResponse {
    private Integer id;
    private String courtName;
    private String locationName;
    private String playerName;
    private String playerPhone;
    private List<Integer> startHours; // New: JSON array of hours
    private Integer startTimeSlot; // Legacy
    private Integer endTimeSlot; // Legacy
    private Float totalPrice;
    private BookingStatus status;
    private LocalDate bookingDate;
    private String paymentMethod;
}
