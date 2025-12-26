package Bookington2.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeSlotDTO {
    private int startTimeSlot;
    private int endTimeSlot;
}
