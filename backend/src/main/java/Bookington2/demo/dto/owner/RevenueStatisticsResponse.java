package Bookington2.demo.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueStatisticsResponse {
    private Long totalRevenue;
    private Long totalBookings;
    private Long canceledBookings;
    private Long completedBookings;
    private Long pendingBookings;
}

