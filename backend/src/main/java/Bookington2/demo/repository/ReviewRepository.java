package Bookington2.demo.repository;

import Bookington2.demo.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    
    List<Review> findByUserId(Integer userId);
    
    Page<Review> findByUserId(Integer userId, Pageable pageable);
    
    List<Review> findByLocationId(Integer locationId);
    
    Page<Review> findByLocationId(Integer locationId, Pageable pageable);
    
    Optional<Review> findByUserIdAndLocationId(Integer userId, Integer locationId);
    
    // Location-based queries
    @Query("SELECT r FROM Review r WHERE r.location.id = :locationId ORDER BY r.createdAt DESC")
    List<Review> findByLocationIdOrderByCreatedAtDesc(@Param("locationId") Integer locationId);
    
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Review> findByUserIdOrderByCreatedAtDesc(@Param("userId") Integer userId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.location.id = :locationId")
    Long countByLocationId(@Param("locationId") Integer locationId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.location.id = :locationId")
    Double getAverageRatingByLocationId(@Param("locationId") Integer locationId);
    
    @Query("SELECT r FROM Review r WHERE r.location.id = :locationId AND r.rating >= :minRating ORDER BY r.createdAt DESC")
    List<Review> findByLocationIdAndMinRating(@Param("locationId") Integer locationId, @Param("minRating") Integer minRating);
}
