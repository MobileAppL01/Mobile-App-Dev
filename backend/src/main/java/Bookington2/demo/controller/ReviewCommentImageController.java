package Bookington2.demo.controller;

import Bookington2.demo.dto.ReviewCommentImageDTO;
import Bookington2.demo.service.ReviewCommentImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review Comment Image API", description = "API for managing comment images")
public class ReviewCommentImageController {

    @Autowired
    private ReviewCommentImageService reviewCommentImageService;

    // ========================================
    // COMMENT IMAGE APIS
    // ========================================

    @PostMapping("/comments/{commentId}/images")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Upload comment image", description = "Upload an image to a comment (max 5 per comment)")
    public ResponseEntity<?> uploadImage(
            @Parameter(description = "Comment ID")
            @PathVariable Integer commentId,

            @Parameter(description = "Image file")
            @RequestParam("file") MultipartFile file) {
        try {
            ReviewCommentImageDTO image = reviewCommentImageService.uploadCommentImage(commentId, file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", image
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/comments/{commentId}/images")
    @Operation(summary = "Get comment images with pagination", description = "Get images for a comment with pagination")
    public ResponseEntity<?> getCommentImages(
            @Parameter(description = "Comment ID")
            @PathVariable Integer commentId,
            
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "12") int size) {
        try {
            Page<ReviewCommentImageDTO> images = reviewCommentImageService.getCommentImagesPaginated(commentId, page, size);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "commentId", commentId,
                    "page", images.getNumber(),
                    "size", images.getSize(),
                    "totalPages", images.getTotalPages(),
                    "totalElements", images.getTotalElements(),
                    "items", images.getContent()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/comments/images/{imageId}")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Delete comment image", description = "Delete an image from a comment")
    public ResponseEntity<?> deleteImage(
            @Parameter(description = "Image ID")
            @PathVariable Integer imageId) {
        try {
            reviewCommentImageService.deleteImage(imageId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Image deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // ========================================
    // REVIEW IMAGE GALLERY APIS
    // ========================================

    @GetMapping("/{reviewId}/images")
    @Operation(summary = "Get review images gallery", description = "Get all images from all comments of a review with pagination")
    public ResponseEntity<?> getReviewImages(
            @Parameter(description = "Review ID")
            @PathVariable Integer reviewId,
            
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "12") int size) {
        try {
            Page<ReviewCommentImageDTO> images = reviewCommentImageService.getReviewImagesPaginated(reviewId, page, size);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "reviewId", reviewId,
                    "page", images.getNumber(),
                    "size", images.getSize(),
                    "totalPages", images.getTotalPages(),
                    "totalElements", images.getTotalElements(),
                    "items", images.getContent()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/images/{imageId}")
    @Operation(summary = "Get image detail", description = "Get detail of a specific image by ID")
    public ResponseEntity<?> getImageDetail(
            @Parameter(description = "Image ID")
            @PathVariable Integer imageId) {
        try {
            ReviewCommentImageDTO image = reviewCommentImageService.getImageById(imageId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", image
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
