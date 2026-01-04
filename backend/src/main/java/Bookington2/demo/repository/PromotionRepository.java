package Bookington2.demo.repository;

import Bookington2.demo.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    @Query("SELECT p FROM Promotion p WHERE p.location.owner.id = :ownerId")
    List<Promotion> findAllByOwnerId(@Param("ownerId") Integer ownerId);

    @Query("SELECT p FROM Promotion p WHERE p.location.owner.id = :ownerId AND p.location.id = :locationId")
    List<Promotion> findAllByOwnerIdAndLocationId(@Param("ownerId") Integer ownerId, @Param("locationId") Integer locationId);

    @Query("SELECT p FROM Promotion p WHERE p.id = :promotionId AND p.location.owner.id = :ownerId")
    Optional<Promotion> findByIdAndOwnerId(@Param("promotionId") Integer promotionId, @Param("ownerId") Integer ownerId);

    boolean existsByCode(String code);

    Optional<Promotion> findByCodeAndActiveTrue(String code);
}

