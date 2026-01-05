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
public class CreateCommentRequestDTO {
    @NotNull(message = "Review ID is required")
    private Integer reviewId;

    @NotBlank(message = "Comment content is required")
    @Size(max = 1000, message = "Comment content must not exceed 1000 characters")
    private String content;

    private Integer parentCommentId; // Optional, null = root comment, not null = reply
}
