package Bookington2.demo.dto.owner;

import Bookington2.demo.enums.CourtStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourtRequest {
    @NotBlank(message = "Tên sân không được để trống")
    private String name;

    private CourtStatus status = CourtStatus.ACTIVE;
}

