package Bookington2.demo.dto.player;

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
public class CourtAvailabilityResponse {
    private Integer courtId;
    private String courtName;
    private LocalDate date;
    private List<Integer> bookedSlots; // Các giờ đã có người đặt
    private List<Integer> availableSlots; // Các giờ còn trống
    private Integer openHour;
    private Integer closeHour;
    private Integer pricePerHour;
}

