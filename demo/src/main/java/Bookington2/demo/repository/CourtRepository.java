package Bookington2.demo.repository;

import Bookington2.demo.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourtRepository extends JpaRepository<Court,Integer> {
    List<Court> findAllByLocation_Id(Integer id);
}
