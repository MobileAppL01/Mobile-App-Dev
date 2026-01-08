package Bookington2.demo.service;

import Bookington2.demo.dto.CommentResponseDTO;
import Bookington2.demo.dto.ReviewDTO;
import Bookington2.demo.dto.request.CreateCommentRequestDTO;
import Bookington2.demo.dto.request.CreateReviewRequestDTO;
import Bookington2.demo.entity.*;
import Bookington2.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Bookington2.demo.dto.ReviewCommentImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewCommentService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ReviewImageRepository reviewImageRepository;

// Nếu comment không có ảnh => bỏ hẳn cái này đi
// @Autowired
// private ReviewCommentImageService reviewCommentImageService;


    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewCommentRepository reviewCommentRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewCommentImageService reviewCommentImageService;


    // ========================================
    // REVIEW METHODS
    // ========================================

    @Transactional
    public void uploadReviewImages(Integer reviewId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;

        if (files.size() > 5) {
            throw new RuntimeException("Maximum 5 images allowed per review");
        }

        User currentUser = getCurrentUser();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        // chỉ chủ review mới upload
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only upload images to your own review");
        }

        long currentCount = reviewImageRepository.countByReviewId(reviewId);
        long newCount = files.stream().filter(f -> f != null && !f.isEmpty()).count();

        if (currentCount + newCount > 5) {
            throw new RuntimeException("Total images per review cannot exceed 5");
        }

        for (MultipartFile f : files) {
            if (f == null || f.isEmpty()) continue;

            try {
                Map result = cloudinaryService.uploadImage(f, "reviews/" + reviewId);
                String publicId = (String) result.get("public_id");
                String secureUrl = (String) result.get("secure_url");

                reviewImageRepository.save(ReviewImage.builder()
                        .review(review)
                        .publicId(publicId)
                        .secureUrl(secureUrl)
                        .build());

            } catch (IOException e) {
                throw new RuntimeException("Upload review image failed: " + e.getMessage(), e);
            }
        }
    }


    @Transactional
    public ReviewDTO createReview(CreateReviewRequestDTO request, List<MultipartFile> files) {
        User currentUser = getCurrentUser();

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + request.getLocationId()));

        if (reviewRepository.findByUserIdAndLocationId(currentUser.getId(), request.getLocationId()).isPresent()) {
            throw new RuntimeException("You have already reviewed this location");
        }

        Review savedReview = reviewRepository.save(Review.builder()
                .user(currentUser)
                .location(location)
                .rating(request.getRating())
                .content(request.getContent())
                .build());

        if (files != null && !files.isEmpty()) {
            if (files.size() > 5) throw new RuntimeException("Maximum 5 images allowed per review");

            for (MultipartFile f : files) {
                if (f == null || f.isEmpty()) continue;

                try {
                    Map result = cloudinaryService.uploadImage(f, "reviews/" + savedReview.getId());
                    String publicId = (String) result.get("public_id");
                    String secureUrl = (String) result.get("secure_url");

                    reviewImageRepository.save(ReviewImage.builder()
                            .review(savedReview)
                            .publicId(publicId)
                            .secureUrl(secureUrl)
                            .build());
                } catch (IOException e) {
                    throw new RuntimeException("Upload review image failed: " + e.getMessage(), e);
                }
            }
        }

        return toReviewDTO(savedReview);
    }



    public List<ReviewDTO> getUserReviews(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByUserId(userId, pageable);

        return reviews.getContent().stream()
                .map(this::toReviewDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getCurrentUserReviews(int page, int size) {
        User currentUser = getCurrentUser();
        return getUserReviews(currentUser.getId(), page, size);
    }

    public List<ReviewDTO> getLocationReviews(Integer locationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByLocationId(locationId, pageable);

        return reviews.getContent().stream()
                .map(this::toReviewDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        return toReviewDTO(review);
    }

    @Transactional
    public ReviewDTO updateReview(Integer reviewId, CreateReviewRequestDTO request) {
        User currentUser = getCurrentUser();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        // Check ownership
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only edit your own reviews");
        }

        review.setRating(request.getRating());
        review.setContent(request.getContent());

        Review savedReview = reviewRepository.save(review);

        return toReviewDTO(savedReview);
    }

    @Transactional
    public void deleteReview(Integer reviewId) {
        User currentUser = getCurrentUser();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        // Check ownership
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    public Double getAverageRating(Integer locationId) {
        return reviewRepository.getAverageRatingByLocationId(locationId);
    }

    public Long getTotalReviews(Integer locationId) {
        return reviewRepository.countByLocationId(locationId);
    }

    public Map<Integer, Long> getRatingCounts(Integer locationId) {
        List<Object[]> queryResult = reviewRepository.countReviewsByRating(locationId);
        Map<Integer, Long> counts = new HashMap<>();
        // Initialize with 0
        for (int i = 1; i <= 5; i++) {
            counts.put(i, 0L);
        }

        for (Object[] row : queryResult) {
            Integer rating = (Integer) row[0];
            Long count = (Long) row[1];
            if (rating != null && rating >= 1 && rating <= 5) {
                counts.put(rating, count);
            }
        }
        return counts;
    }


    private CommentResponseDTO toCommentDTO(ReviewComment comment) {
        // comment mới tạo thì chưa có ảnh, hoặc bạn có thể load ảnh từ DB ở đây nếu muốn
        return toCommentDTO(comment, List.of());
    }

    // ========================================
    // COMMENT METHODS
    // ========================================

    @Transactional
    public CommentResponseDTO createComment(CreateCommentRequestDTO request) {
        User currentUser = getCurrentUser();

        // Validate review exists
        Review review = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + request.getReviewId()));

        // Validate parent comment if reply
        ReviewComment parentComment = null;
        if (request.getParentCommentId() != null) {
            parentComment = reviewCommentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));

            // Ensure parent belongs to same review
            if (!parentComment.getReview().getId().equals(request.getReviewId())) {
                throw new RuntimeException("Parent comment does not belong to this review");
            }
        }

        // Create comment
        ReviewComment comment = ReviewComment.builder()
                .review(review)
                .user(currentUser)
                .parentComment(parentComment)
                .content(request.getContent())
                .build();

        ReviewComment savedComment = reviewCommentRepository.save(comment);

        return toCommentDTO(savedComment);
    }

    public List<CommentResponseDTO> getCommentsByReview(Integer reviewId) {
        List<ReviewComment> allComments =
                reviewCommentRepository.findByReviewIdOrderByCreatedAtAsc(reviewId);

        return buildCommentTree(allComments);
    }

    @Transactional
    public CommentResponseDTO updateComment(Integer commentId, String content) {
        User currentUser = getCurrentUser();

        ReviewComment comment = reviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        // Check ownership
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only edit your own comments");
        }

        comment.setContent(content);
        ReviewComment savedComment = reviewCommentRepository.save(comment);

        return toCommentDTO(savedComment);
    }

    @Transactional
    public void deleteComment(Integer commentId) {
        User currentUser = getCurrentUser();

        ReviewComment comment = reviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        // Check ownership
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own comments");
        }

        reviewCommentRepository.delete(comment);
    }

    public Long getCommentCountByReviewId(Integer reviewId) {
        return reviewCommentRepository.countByReviewId(reviewId);
    }

    // ========================================
    // HELPER METHODS
    // ========================================

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    private ReviewDTO toReviewDTO(Review review) {
        // comments (text-only)
        List<ReviewComment> allComments =
                reviewCommentRepository.findByReviewIdOrderByCreatedAtAsc(review.getId());

        // nếu comment không có ảnh => imagesByCommentId = Map.of()
        List<CommentResponseDTO> commentTree = buildCommentTree(allComments);

        // ✅ ảnh của review
        List<String> reviewImages = reviewImageRepository
                .findByReviewIdOrderByCreatedAtAsc(review.getId())
                .stream()
                .map(ReviewImage::getSecureUrl)
                .toList();

        return ReviewDTO.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .userName(review.getUser().getFullName())
                .userAvatar(review.getUser().getAvatar())
                .locationId(review.getLocation().getId())
                .locationName(review.getLocation().getName())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .comments(commentTree)
                .images(reviewImages) // đổi type images trong ReviewDTO thành List<String> (hoặc tạo ReviewImageDTO)
                .build();
    }




    private CommentResponseDTO toCommentDTO(
            ReviewComment comment,
            List<ReviewCommentImageDTO> images
    ) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .reviewId(comment.getReview().getId())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getFullName())
                .userAvatar(comment.getUser().getAvatar())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .replies(new ArrayList<>())
                .build();
    }


    private List<CommentResponseDTO> buildCommentTree(List<ReviewComment> allComments) {
        Map<Integer, CommentResponseDTO> nodeMap = new HashMap<>();
        List<CommentResponseDTO> rootComments = new ArrayList<>();

        for (ReviewComment c : allComments) {
            CommentResponseDTO dto = CommentResponseDTO.builder()
                    .id(c.getId())
                    .reviewId(c.getReview().getId())
                    .userId(c.getUser().getId())
                    .userName(c.getUser().getFullName())
                    .userAvatar(c.getUser().getAvatar())
                    .content(c.getContent())
                    .createdAt(c.getCreatedAt())
                    .parentCommentId(c.getParentComment() != null ? c.getParentComment().getId() : null)
                    .replies(new ArrayList<>())
                    .build();
            nodeMap.put(c.getId(), dto);
        }

        for (ReviewComment c : allComments) {
            CommentResponseDTO dto = nodeMap.get(c.getId());
            if (c.getParentComment() == null) rootComments.add(dto);
            else nodeMap.get(c.getParentComment().getId()).getReplies().add(dto);
        }

        return rootComments;
    }


}
