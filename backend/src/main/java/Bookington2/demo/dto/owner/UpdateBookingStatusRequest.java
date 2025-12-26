package Bookington2.demo.dto.owner;

import Bookington2.demo.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookingStatusRequest {
    @NotNull(message = "Trạng thái không được để trống")
    private BookingStatus status;
}

