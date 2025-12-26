package Bookington2.demo.dto;

import java.time.LocalTime;

public interface OpenTimeDTO {
    LocalTime getOpenTime();
    LocalTime getCloseTime();
    
    // Helper methods to get hour integers
    default int getStartHour() {
        return getOpenTime() != null ? getOpenTime().getHour() : 0;
    }
    
    default int getEndHour() {
        return getCloseTime() != null ? getCloseTime().getHour() : 24;
    }
}
