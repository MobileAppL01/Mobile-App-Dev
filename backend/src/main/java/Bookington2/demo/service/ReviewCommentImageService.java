package Bookington2.demo.service;

import Bookington2.demo.dto.ReviewCommentImageDTO;
import Bookington2.demo.entity.ReviewComment;
import Bookington2.demo.entity.ReviewCommentImage;
import Bookington2.demo.entity.User;
import Bookington2.demo.repository.ReviewCommentImageRepository;
import Bookington2.demo.repository.ReviewCommentRepository;
import Bookington2.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewCommentImageService {

    @Autowired
    private ReviewCommentImageRepository reviewCommentImageRepository;

    @Autowired
    private ReviewCommentRepository reviewCommentRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserRepository userRepository;

    private static final int MAX_IMAGES_PER_COMMENT = 5;

    /**
     * Upload image for a comment (PLAYER only for their comments)
     */
    @Transactional
    public ReviewCommentImageDTO uploadCommentImage(Integer commentId, MultipartFile file) throws IOException {
        User currentUser = getCurrentUser();

        // Validate comment exists
        ReviewComment comment = reviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Check ownership
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only upload images for your own comments");
        }

        // Check max images limit
        long currentCount = reviewCommentImageRepository.countByCommentId(commentId);
        if (currentCount >= MAX_IMAGES_PER_COMMENT) {
            throw new RuntimeException("Maximum " + MAX_IMAGES_PER_COMMENT + " images allowed per comment");
        }

        // Upload to Cloudinary
        Map result = cloudinaryService.uploadImage(file, "comments/" + commentId);
        String publicId = (String) result.get("public_id");
        String secureUrl = (String) result.get("secure_url");

        // Save to database
        ReviewCommentImage image = ReviewCommentImage.builder()
                .comment(comment)
                .publicId(publicId)
                .secureUrl(secureUrl)
                .build();

        ReviewCommentImage saved = reviewCommentImageRepository.save(image);

        return toDTO(saved);
    }

    /**
     * Get all images for a comment (no pagination)
     */
    public List<ReviewCommentImageDTO> getCommentImages(Integer commentId) {
        return reviewCommentImageRepository.findByCommentIdOrderByCreatedAtAsc(commentId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get images for a comment with pagination
     */
    public Page<ReviewCommentImageDTO> getCommentImagesPaginated(Integer commentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewCommentImageRepository.findByCommentId(commentId, pageRequest)
                .map(this::toDTO);
    }

    /**
     * Get all images for a review (from all comments) with pagination
     */
    public Page<ReviewCommentImageDTO> getReviewImagesPaginated(Integer reviewId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewCommentImageRepository.findByReviewId(reviewId, pageRequest)
                .map(this::toDTO);
    }

    /**
     * Get image by ID
     */
    public ReviewCommentImageDTO getImageById(Integer imageId) {
        ReviewCommentImage image = reviewCommentImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        return toDTO(image);
    }

    /**
     * Get images for multiple comments (batch query for performance)
     */
    public Map<Integer, List<ReviewCommentImageDTO>> getImagesForComments(List<Integer> commentIds) {
        return reviewCommentImageRepository.findByCommentIdIn(commentIds)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.groupingBy(ReviewCommentImageDTO::getCommentId));
    }

    /**
     * Delete an image
     */
    @Transactional
    public void deleteImage(Integer imageId) throws IOException {
        User currentUser = getCurrentUser();

        ReviewCommentImage image = reviewCommentImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Check ownership
        if (!image.getComment().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own comment images");
        }

        // Delete from Cloudinary
        cloudinaryService.deleteImage(image.getPublicId());

        // Delete from database
        reviewCommentImageRepository.delete(image);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    private ReviewCommentImageDTO toDTO(ReviewCommentImage image) {
        return ReviewCommentImageDTO.builder()
                .id(image.getId())
                .commentId(image.getComment().getId())
                .publicId(image.getPublicId())
                .secureUrl(image.getSecureUrl())
                .createdAt(image.getCreatedAt())
                .build();
    }
}
