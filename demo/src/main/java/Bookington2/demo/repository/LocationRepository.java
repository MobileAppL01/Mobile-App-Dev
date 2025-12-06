package Bookington2.demo.repository;

import Bookington2.demo.dto.LocationDTO;
import Bookington2.demo.dto.OpenTimeDTO;
import Bookington2.demo.dto.TimeSlotView;
import Bookington2.demo.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location,Integer> {

    @Query(value = """
    SELECT * FROM location 
    WHERE unaccent(lower(address)) 
          LIKE CONCAT('%', unaccent(lower(:keyword)), '%')
""", nativeQuery = true)
    List<Location> searchByAddress(@Param("keyword") String keyword);

    OpenTimeDTO findLocationById(Integer id);
}
