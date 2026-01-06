package Bookington2.demo.dto;

import lombok.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommentImageDTO {
    private Integer id;
    private Integer commentId;
    private String publicId;
    private String secureUrl;
    private LocalDateTime createdAt;
}
