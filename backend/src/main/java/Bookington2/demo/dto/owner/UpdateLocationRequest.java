package Bookington2.demo.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLocationRequest {
    private String name;
    private String address;
    private String description;
    private Integer pricePerHour;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String image;
    private String status;
}

