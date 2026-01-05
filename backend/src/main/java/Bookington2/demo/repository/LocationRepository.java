package Bookington2.demo.repository;

import Bookington2.demo.dto.OpenTimeDTO;
import Bookington2.demo.entity.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query(value = """
            SELECT * FROM location 
            WHERE CAST(address AS TEXT) ILIKE CONCAT('%', :keyword, '%')
            """, nativeQuery = true)
    List<Location> searchByAddress(@Param("keyword") String keyword);

    @Query(value = """
            SELECT * FROM location l
            WHERE (:province IS NULL OR CAST(l.address AS TEXT) ILIKE CONCAT('%', :province, '%'))
            AND (:district IS NULL OR CAST(l.address AS TEXT) ILIKE CONCAT('%', :district, '%'))
            AND (:minPrice IS NULL OR l.price_per_hour >= :minPrice)
            AND (:maxPrice IS NULL OR l.price_per_hour <= :maxPrice)
            """, nativeQuery = true)
    List<Location> findLocationsWithFilters(@Param("province") String province,
                                           @Param("district") String district,
                                           @Param("minPrice") Integer minPrice,
                                           @Param("maxPrice") Integer maxPrice,
                                           Pageable pageable);

    OpenTimeDTO findLocationById(Integer id);

    // Owner APIs
    List<Location> findAllByOwner_Id(Integer ownerId);

    Optional<Location> findByIdAndOwner_Id(Integer id, Integer ownerId);

    boolean existsByIdAndOwner_Id(Integer id, Integer ownerId);
}
