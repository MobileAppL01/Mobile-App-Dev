package Bookington2.demo.dto.player;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    @NotNull(message = "Court ID không được để trống")
    private Integer courtId;

    @NotNull(message = "Ngày đặt sân không được để trống")
    private LocalDate bookingDate;

    @NotEmpty(message = "Phải chọn ít nhất một khung giờ")
    private List<Integer> startHours; // Ví dụ: [17, 18, 19] = đặt 17h-20h (3 tiếng)

    private String paymentMethod; // VNPAY, CASH
}

