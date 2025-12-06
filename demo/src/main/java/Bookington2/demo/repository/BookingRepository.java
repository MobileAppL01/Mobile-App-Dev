package Bookington2.demo.repository;

import Bookington2.demo.dto.TimeSlotDTO;
import Bookington2.demo.dto.TimeSlotView;
import Bookington2.demo.entity.Booking;
import Bookington2.demo.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    List<TimeSlotView> findAllByCourtIdAndBookingDate(Integer courtId, LocalDate bookingDate);
    List<Booking> findAllByPlayer_Id(String playerId);
}
