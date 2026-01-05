package Bookington2.demo.service;

import Bookington2.demo.dto.CommentResponseDTO;
import Bookington2.demo.dto.ReviewDTO;
import Bookington2.demo.dto.request.CreateCommentRequestDTO;
import Bookington2.demo.dto.request.CreateReviewRequestDTO;
import Bookington2.demo.entity.Location;
import Bookington2.demo.entity.Review;
import Bookington2.demo.entity.ReviewComment;
import Bookington2.demo.entity.User;
import Bookington2.demo.repository.LocationRepository;
import Bookington2.demo.repository.ReviewRepository;
import Bookington2.demo.repository.ReviewCommentRepository;
import Bookington2.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewCommentService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewCommentRepository reviewCommentRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    // ========================================
    // REVIEW METHODS
    // ========================================

    @Transactional
    public ReviewDTO createReview(CreateReviewRequestDTO request) {
        User currentUser = getCurrentUser();

        // Validate location exists
        Location location = locationRepository.findById(request.getLocationId())
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + request.getLocationId()));

        // Check if user already reviewed this location
        if (reviewRepository.findByUserIdAndLocationId(currentUser.getId(), request.getLocationId()).isPresent()) {
            throw new RuntimeException("You have already reviewed this location");
        }

        // Create review
        Review review = Review.builder()
            .user(currentUser)
            .location(location)
            .rating(request.getRating())
            .content(request.getContent())
            .build();

        Review savedReview = reviewRepository.save(review);
        
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
        // Get all comments for review
        List<ReviewComment> allComments = reviewCommentRepository.findByReviewIdOrderByCreatedAtAsc(reviewId);
        
        // Build tree structure
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
        // Get comments as tree
        List<ReviewComment> allComments = reviewCommentRepository.findByReviewIdOrderByCreatedAtAsc(review.getId());
        List<CommentResponseDTO> commentTree = buildCommentTree(allComments);
        
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
            .build();
    }

    private CommentResponseDTO toCommentDTO(ReviewComment comment) {
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
        // Map to store nodes by ID
        Map<Integer, CommentResponseDTO> nodeMap = new HashMap<>();
        List<CommentResponseDTO> rootComments = new ArrayList<>();

        // Create all nodes
        for (ReviewComment comment : allComments) {
            CommentResponseDTO dto = toCommentDTO(comment);
            nodeMap.put(comment.getId(), dto);
        }

        // Build tree structure
        for (ReviewComment comment : allComments) {
            CommentResponseDTO dto = nodeMap.get(comment.getId());
            
            if (comment.getParentComment() == null) {
                // Root comment
                rootComments.add(dto);
            } else {
                // Reply - add to parent's replies
                CommentResponseDTO parentDto = nodeMap.get(comment.getParentComment().getId());
                if (parentDto != null) {
                    parentDto.getReplies().add(dto);
                }
            }
        }

        return rootComments;
    }
}
