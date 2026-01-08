package Bookington2.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourtDTO {
    private Integer id;
    private String name;
    private String status;
}
