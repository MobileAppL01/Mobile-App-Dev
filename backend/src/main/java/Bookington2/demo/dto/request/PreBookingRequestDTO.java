package Bookington2.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreBookingRequestDTO {
    @NotNull(message = "Court ID is required")
    private Integer courtId;

    @NotNull(message = "Booking date is required")
    @Future(message = "Booking date must be in the future")
    private java.time.LocalDate bookingDate;

    @NotNull(message = "Start time is required")
    @Min(value = 0, message = "Start time must be between 0 and 23")
    @Max(value = 23, message = "Start time must be between 0 and 23")
    private Integer startTimeSlot;

    @NotNull(message = "End time is required")
    @Min(value = 1, message = "End time must be between 1 and 24")
    @Max(value = 24, message = "End time must be between 1 and 24")
    private Integer endTimeSlot;

    @AssertTrue(message = "End time must be after start time")
    private boolean isEndTimeValid() {
        if (startTimeSlot == null || endTimeSlot == null) {
            return false;
        }
        return endTimeSlot > startTimeSlot;
    }
}
