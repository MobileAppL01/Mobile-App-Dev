package Bookington2.demo.dto.owner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLocationRequest {
    @NotBlank(message = "Tên sân không được để trống")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private String description;

    @NotNull(message = "Giá theo giờ không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    private Integer pricePerHour;

    @NotNull(message = "Giờ mở cửa không được để trống")
    private LocalTime openTime;

    @NotNull(message = "Giờ đóng cửa không được để trống")
    private LocalTime closeTime;

    private String image;
}

