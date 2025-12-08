package Bookington2.demo.repository;

import Bookington2.demo.entity.Court;
import Bookington2.demo.enums.CourtStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourtRepository extends JpaRepository<Court, Integer> {
    List<Court> findAllByLocation_Id(Integer locationId);

    List<Court> findAllByLocation_IdAndDeletedFalse(Integer locationId);

    Optional<Court> findByIdAndDeletedFalse(Integer id);

    Optional<Court> findByIdAndLocation_Owner_IdAndDeletedFalse(Integer courtId, String ownerId);

    List<Court> findAllByLocation_Owner_IdAndDeletedFalse(String ownerId);

    int countByLocation_IdAndDeletedFalse(Integer locationId);
}
