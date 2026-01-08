package Bookington2.demo.controller;

import Bookington2.demo.dto.CommentResponseDTO;
import Bookington2.demo.dto.ReviewDTO;
import Bookington2.demo.dto.request.CreateCommentRequestDTO;
import Bookington2.demo.dto.request.CreateReviewRequestDTO;
import Bookington2.demo.service.ReviewCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
@Tag(name = "Review & Comment API", description = "API endpoints for reviews and comments (Facebook-like)")
public class ReviewCommentController {

    @Autowired
    private ReviewCommentService reviewCommentService;

    // ========================================
    // REVIEW ENDPOINTS
    // ========================================


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody CreateReviewRequestDTO data) {
        ReviewDTO review = reviewCommentService.createReview(data, null); // tạo review chưa có ảnh
        return ResponseEntity.ok(review);
    }

    @PostMapping(value = "/{reviewId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<ReviewDTO> uploadReviewImages(
            @PathVariable Integer reviewId,
            @RequestPart("files") List<MultipartFile> files
    ) {
        reviewCommentService.uploadReviewImages(reviewId, files);
        return ResponseEntity.ok(reviewCommentService.getReviewById(reviewId));
    }



    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user reviews", description = "Get all reviews written by a specific user")
    public ResponseEntity<List<ReviewDTO>> getUserReviews(
            @Parameter(description = "User ID") @PathVariable Integer userId,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        List<ReviewDTO> reviews = reviewCommentService.getUserReviews(userId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/my-reviews")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Get current user reviews", description = "Get all reviews written by the current user")
    public ResponseEntity<List<ReviewDTO>> getCurrentUserReviews(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        List<ReviewDTO> reviews = reviewCommentService.getCurrentUserReviews(page, size);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/location/{locationId}")
    @Operation(summary = "Get location reviews", description = "Get all reviews for a specific location")
    public ResponseEntity<List<ReviewDTO>> getLocationReviews(
            @Parameter(description = "Location ID") @PathVariable Integer locationId,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        List<ReviewDTO> reviews = reviewCommentService.getLocationReviews(locationId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "Get review by ID", description = "Get detailed review with comments tree")
    public ResponseEntity<ReviewDTO> getReviewById(
            @Parameter(description = "Review ID") @PathVariable Integer reviewId) {

        ReviewDTO review = reviewCommentService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Update a review", description = "Update an existing review (only by author)")
    public ResponseEntity<ReviewDTO> updateReview(
            @Parameter(description = "Review ID") @PathVariable Integer reviewId,

            @Valid @RequestBody CreateReviewRequestDTO request) {

        ReviewDTO review = reviewCommentService.updateReview(reviewId, request);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Delete a review", description = "Delete a review (only by author)")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "Review ID") @PathVariable Integer reviewId) {

        reviewCommentService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/location/{locationId}/stats")
    @Operation(summary = "Get location review stats", description = "Get average rating and total reviews for a location")
    public ResponseEntity<Map<String, Object>> getLocationReviewStats(
            @Parameter(description = "Location ID") @PathVariable Integer locationId) {

        Double averageRating = reviewCommentService.getAverageRating(locationId);
        Long totalReviews = reviewCommentService.getTotalReviews(locationId);
        Map<Integer, Long> counts = reviewCommentService.getRatingCounts(locationId);

        return ResponseEntity.ok(Map.of(
                "averageRating", averageRating != null ? averageRating : 0.0,
                "totalReviews", totalReviews != null ? totalReviews : 0,
                "counts", counts));
    }

    // ========================================
    // COMMENT ENDPOINTS
    // ========================================

    @PostMapping("/comments")
    @PreAuthorize("hasAnyRole('PLAYER', 'OWNER', 'MANAGER')")
    @Operation(summary = "Create a comment", description = "Create a new comment on a review (or reply to another comment)")
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CreateCommentRequestDTO request) {
        CommentResponseDTO comment = reviewCommentService.createComment(request);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{reviewId}/comments")
    @Operation(summary = "Get comments by review", description = "Get all comments for a review as a tree structure")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByReview(
            @Parameter(description = "Review ID") @PathVariable Integer reviewId) {

        List<CommentResponseDTO> comments = reviewCommentService.getCommentsByReview(reviewId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('PLAYER', 'OWNER', 'MANAGER')")
    @Operation(summary = "Update a comment", description = "Update an existing comment (only by author)")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @Parameter(description = "Comment ID") @PathVariable Integer commentId,

            @Parameter(description = "Comment content") @RequestParam String content) {

        CommentResponseDTO comment = reviewCommentService.updateComment(commentId, content);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('PLAYER', 'OWNER', 'MANAGER')")
    @Operation(summary = "Delete a comment", description = "Delete a comment (only by author)")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "Comment ID") @PathVariable Integer commentId) {

        reviewCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{reviewId}/comments/count")
    @Operation(summary = "Get comment count", description = "Get total number of comments for a review")
    public ResponseEntity<Map<String, Long>> getCommentCount(
            @Parameter(description = "Review ID") @PathVariable Integer reviewId) {

        Long count = reviewCommentService.getCommentCountByReviewId(reviewId);
        return ResponseEntity.ok(Map.of("count", count != null ? count : 0L));
    }

    @PostMapping("/comments/reply")
    @PreAuthorize("hasAnyRole('PLAYER', 'OWNER', 'MANAGER')")
    @Operation(summary = "Reply to a comment", description = "Create a reply to an existing comment")
    public ResponseEntity<CommentResponseDTO> replyToComment(@Valid @RequestBody CreateCommentRequestDTO request) {
        // Same as createComment, but parentCommentId should be set
        if (request.getParentCommentId() == null) {
            throw new RuntimeException("Parent comment ID is required for replies");
        }
        CommentResponseDTO comment = reviewCommentService.createComment(request);
        return ResponseEntity.ok(comment);
    }
}
