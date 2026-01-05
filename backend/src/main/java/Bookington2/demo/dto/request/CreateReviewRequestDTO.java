package Bookington2.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequestDTO {
    @NotNull(message = "Location ID is required")
    private Integer locationId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Review content is required")
    @Size(max = 2000, message = "Review content must not exceed 2000 characters")
    private String content;
}
