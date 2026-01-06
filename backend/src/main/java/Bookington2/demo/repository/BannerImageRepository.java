package Bookington2.demo.repository;

import Bookington2.demo.entity.BannerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BannerImageRepository extends JpaRepository<BannerImage, Integer> {

    List<BannerImage> findByIsActiveTrueOrderByDisplayOrderAsc();

    List<BannerImage> findAllByOrderByDisplayOrderAsc();

    Optional<BannerImage> findByPublicId(String publicId);

    @Query("SELECT COALESCE(MAX(b.displayOrder), 0) FROM BannerImage b")
    Integer findMaxDisplayOrder();

    List<BannerImage> findByLocationIdAndIsActiveTrueOrderByDisplayOrderAsc(Integer locationId);

    List<BannerImage> findByLocationIdOrderByDisplayOrderAsc(Integer locationId);

    @Query("SELECT COALESCE(MAX(b.displayOrder), 0) FROM BannerImage b WHERE b.location.id = :locationId")
    Integer findMaxDisplayOrderByLocationId(Integer locationId);
}
