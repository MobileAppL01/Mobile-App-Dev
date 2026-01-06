package Bookington2.demo.repository;

import Bookington2.demo.entity.ReviewCommentImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewCommentImageRepository extends JpaRepository<ReviewCommentImage, Integer> {

    List<ReviewCommentImage> findByCommentIdOrderByCreatedAtAsc(Integer commentId);

    // Pagination by commentId
    Page<ReviewCommentImage> findByCommentId(Integer commentId, Pageable pageable);

    // Get all images for a review (through comments)
    @Query("SELECT rci FROM ReviewCommentImage rci WHERE rci.comment.review.id = :reviewId ORDER BY rci.createdAt DESC")
    List<ReviewCommentImage> findByReviewId(@Param("reviewId") Integer reviewId);

    // Pagination by reviewId
    @Query("SELECT rci FROM ReviewCommentImage rci WHERE rci.comment.review.id = :reviewId")
    Page<ReviewCommentImage> findByReviewId(@Param("reviewId") Integer reviewId, Pageable pageable);

    Optional<ReviewCommentImage> findByPublicId(String publicId);

    long countByCommentId(Integer commentId);

    @Modifying
    @Query("DELETE FROM ReviewCommentImage rci WHERE rci.comment.id = :commentId")
    void deleteAllByCommentId(@Param("commentId") Integer commentId);

    @Query("SELECT rci FROM ReviewCommentImage rci WHERE rci.comment.id IN :commentIds")
    List<ReviewCommentImage> findByCommentIdIn(@Param("commentIds") List<Integer> commentIds);
}
