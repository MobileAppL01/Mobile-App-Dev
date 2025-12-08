package Bookington2.demo.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {
    private Integer id;
    private String name;
    private String address;
    private String description;
    private String image;
    private Float ratingAvg;
    private Integer pricePerHour;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String status;
    private Integer courtCount;
}

