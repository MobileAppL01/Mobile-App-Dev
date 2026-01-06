package Bookington2.demo.repository;

import Bookington2.demo.entity.LocationImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationImageRepository extends JpaRepository<LocationImage, Integer> {

    List<LocationImage> findByLocationIdOrderByIsPrimaryDescCreatedAtDesc(Integer locationId);

    Optional<LocationImage> findByLocationIdAndIsPrimaryTrue(Integer locationId);

    @Query("SELECT li FROM LocationImage li WHERE li.location.id = :locationId ORDER BY li.createdAt DESC LIMIT 1")
    Optional<LocationImage> findFirstByLocationIdOrderByCreatedAtDesc(@Param("locationId") Integer locationId);

    Optional<LocationImage> findByPublicId(String publicId);

    long countByLocationId(Integer locationId);

    @Modifying
    @Query("UPDATE LocationImage li SET li.isPrimary = false WHERE li.location.id = :locationId")
    void resetPrimaryForLocation(@Param("locationId") Integer locationId);

    @Modifying
    @Query("DELETE FROM LocationImage li WHERE li.location.id = :locationId")
    void deleteAllByLocationId(@Param("locationId") Integer locationId);
}
