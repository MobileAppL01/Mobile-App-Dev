package Bookington2.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private String courtName;
    private String locationName;
    private Integer startTime;
    private Integer endTime;
    private String status;
    private LocalDate bookingDate;
}
