package Bookington2.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private Integer id;
    private Integer reviewId;
    private Integer userId;
    private String userName; // firstName + lastName
    private String userAvatar;
    private String content;
    private LocalDateTime createdAt;
    private Integer parentCommentId; // null if root comment

//    private List<ReviewCommentImageDTO> images;

    @Builder.Default
    private List<CommentResponseDTO> replies = new ArrayList<>(); // nested replies
}
