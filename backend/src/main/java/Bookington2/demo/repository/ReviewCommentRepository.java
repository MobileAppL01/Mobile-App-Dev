package Bookington2.demo.repository;

import Bookington2.demo.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Integer> {
    
    // A) Get root comments (parent null) by review
    @Query("SELECT c FROM ReviewComment c WHERE c.review.id = :reviewId AND c.parentComment IS NULL ORDER BY c.createdAt ASC")
    List<ReviewComment> findByReviewIdAndParentCommentIsNullOrderByCreatedAtAsc(@Param("reviewId") Integer reviewId);
    
    // B) Get replies by parent comment
    List<ReviewComment> findByParentCommentIdOrderByCreatedAtAsc(Integer parentId);
    
    // Get all comments for a review (for building tree)
    List<ReviewComment> findByReviewIdOrderByCreatedAtAsc(Integer reviewId);
    
    // Get comments by user
    List<ReviewComment> findByUserId(Integer userId);
    
    // Count comments by review
    @Query("SELECT COUNT(c) FROM ReviewComment c WHERE c.review.id = :reviewId")
    Long countByReviewId(@Param("reviewId") Integer reviewId);
}
