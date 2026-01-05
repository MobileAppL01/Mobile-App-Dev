package Bookington2.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Integer id;
    private Integer userId;
    private String userName; // firstName + lastName
    private String userAvatar;
    private Integer locationId;
    private String locationName;
    private Integer rating; // 1-5
    private String content;
    private LocalDateTime createdAt;
    private List<CommentResponseDTO> comments; // comment tree
}
