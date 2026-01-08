package Bookington2.demo.repository;

import Bookington2.demo.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Integer> {
    List<ReviewImage> findByReviewIdOrderByCreatedAtAsc(Integer reviewId);


    long countByReviewId(Integer reviewId);

}
