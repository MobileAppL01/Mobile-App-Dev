package Bookington2.demo.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationRevenueResponse {
    private Integer locationId;
    private String locationName;
    private String locationAddress;
    private Long totalRevenue;
}
