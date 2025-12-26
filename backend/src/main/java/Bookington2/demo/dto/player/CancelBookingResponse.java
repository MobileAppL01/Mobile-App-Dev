package Bookington2.demo.dto.player;

import Bookington2.demo.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelBookingResponse {
    private String message;
    private Integer bookingId;
    private BookingStatus newStatus;
}

