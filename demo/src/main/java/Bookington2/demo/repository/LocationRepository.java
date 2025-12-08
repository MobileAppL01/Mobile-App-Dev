package Bookington2.demo.repository;

import Bookington2.demo.dto.OpenTimeDTO;
import Bookington2.demo.entity.Location;
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
            WHERE unaccent(lower(address)) 
                  LIKE CONCAT('%', unaccent(lower(:keyword)), '%')
            """, nativeQuery = true)
    List<Location> searchByAddress(@Param("keyword") String keyword);

    OpenTimeDTO findLocationById(Integer id);

    // Owner APIs
    List<Location> findAllByOwner_Id(String ownerId);

    Optional<Location> findByIdAndOwner_Id(Integer id, String ownerId);

    boolean existsByIdAndOwner_Id(Integer id, String ownerId);
}
