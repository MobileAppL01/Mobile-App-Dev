package Bookington2.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotDTO {
    private Integer startHour;
    private Integer endHour;
    private String startTimeDisplay;
    private String endTimeDisplay;
    private Float price;
    private Boolean available;
    private String status;
}
