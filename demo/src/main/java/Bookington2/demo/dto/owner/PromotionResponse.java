package Bookington2.demo.dto.owner;

import Bookington2.demo.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResponse {
    private Integer id;
    private Integer locationId;
    private String locationName;
    private String code;
    private DiscountType discountType;
    private Integer discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Boolean active;
}

