package Bookington2.demo.dto;

import Bookington2.demo.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private String courtName;
    private String locationName;
    private Integer startTime;
    private Integer endTime;
    private BookingStatus status;
    private LocalDate bookingDate;
}
