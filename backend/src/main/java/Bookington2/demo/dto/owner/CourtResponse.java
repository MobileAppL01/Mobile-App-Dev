package Bookington2.demo.dto.owner;

import Bookington2.demo.enums.CourtStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourtResponse {
    private Integer id;
    private String name;
    private CourtStatus status;
    private Integer locationId;
    private String locationName;
}

