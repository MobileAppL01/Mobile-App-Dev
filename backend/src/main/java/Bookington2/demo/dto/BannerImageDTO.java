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
public class BannerImageDTO {
    private Integer id;
    private String title;
    private String description;
    private String publicId;
    private String secureUrl;
    private String linkUrl;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Integer locationId;
    private String locationName;
}
