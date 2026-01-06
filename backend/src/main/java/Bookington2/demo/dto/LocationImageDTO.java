package Bookington2.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationImageDTO {
    private Integer id;
    private Integer locationId;
    private String publicId;
    private String secureUrl;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
}
