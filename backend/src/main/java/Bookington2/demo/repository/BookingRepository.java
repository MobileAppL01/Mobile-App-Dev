package Bookington2.demo.repository;

import Bookington2.demo.dto.TimeSlotView;
import Bookington2.demo.entity.Booking;
import Bookington2.demo.enums.BookingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<TimeSlotView> findAllByCourtIdAndBookingDate(Integer courtId, LocalDate bookingDate);

    List<Booking> findAllByPlayer_Id(Integer playerId);

    // ==================== PLAYER BOOKING APIs ====================

    // Find bookings for a court on a specific date (excluding CANCELED)
    @Query("SELECT b FROM Booking b WHERE b.court.id = :courtId AND b.bookingDate = :date AND b.status <> 'CANCELED'")
    List<Booking> findActiveBookingsByCourtAndDate(@Param("courtId") Integer courtId, @Param("date") LocalDate date);

    // Find bookings with lock for preventing concurrent booking
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.court.id = :courtId AND b.bookingDate = :date AND b.status <> 'CANCELED'")
    List<Booking> findActiveBookingsByCourtAndDateWithLock(@Param("courtId") Integer courtId, @Param("date") LocalDate date);

    // Player's booking history
    @Query("SELECT b FROM Booking b WHERE b.player.id = :playerId ORDER BY b.bookingDate DESC, b.createdAt DESC")
    List<Booking> findAllByPlayerId(@Param("playerId") Integer playerId);

    // Player's booking history with filters
    @Query("SELECT b FROM Booking b WHERE b.player.id = :playerId " +
            "AND (:status IS NULL OR b.status = :status) " +
            "AND (:fromDate IS NULL OR b.bookingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR b.bookingDate <= :toDate) " +
            "ORDER BY b.bookingDate DESC, b.createdAt DESC")
    List<Booking> findAllByPlayerIdWithFilters(
            @Param("playerId") Integer playerId,
            @Param("status") BookingStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);

    // Find booking by ID and player ID
    Optional<Booking> findByIdAndPlayer_Id(Integer bookingId, Integer playerId);

    // ==================== OWNER BOOKING APIs ====================

    @Query("SELECT b FROM Booking b WHERE b.court.location.owner.id = :ownerId")
    List<Booking> findAllByOwnerId(@Param("ownerId") Integer ownerId);

    @Query("SELECT b FROM Booking b WHERE b.court.location.owner.id = :ownerId AND b.court.location.id = :locationId")
    List<Booking> findAllByOwnerIdAndLocationId(@Param("ownerId") Integer ownerId, @Param("locationId") Integer locationId);

    @Query("SELECT b FROM Booking b WHERE b.court.location.owner.id = :ownerId AND b.bookingDate = :date")
    List<Booking> findAllByOwnerIdAndDate(@Param("ownerId") Integer ownerId, @Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b WHERE b.court.location.owner.id = :ownerId AND b.status = :status")
    List<Booking> findAllByOwnerIdAndStatus(@Param("ownerId") Integer ownerId, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.court.location.owner.id = :ownerId " +
            "AND (:locationId IS NULL OR b.court.location.id = :locationId) " +
            "AND (:date IS NULL OR b.bookingDate = :date) " +
            "AND (:status IS NULL OR b.status = :status)")
    List<Booking> findAllByOwnerWithFilters(
            @Param("ownerId") Integer ownerId,
            @Param("locationId") Integer locationId,
            @Param("date") LocalDate date,
            @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.id = :bookingId AND b.court.location.owner.id = :ownerId")
    Optional<Booking> findByIdAndOwnerId(@Param("bookingId") Integer bookingId, @Param("ownerId") Integer ownerId);

    // ==================== STATISTICS ====================

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.court.location.owner.id = :ownerId " +
            "AND MONTH(b.bookingDate) = :month AND YEAR(b.bookingDate) = :year")
    Long countByOwnerIdAndMonth(@Param("ownerId") Integer ownerId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.court.location.owner.id = :ownerId " +
            "AND b.status = :status AND MONTH(b.bookingDate) = :month AND YEAR(b.bookingDate) = :year")
    Long countByOwnerIdAndStatusAndMonth(
            @Param("ownerId") Integer ownerId,
            @Param("status") BookingStatus status,
            @Param("month") int month,
            @Param("year") int year);

    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b WHERE b.court.location.owner.id = :ownerId " +
            "AND b.status = 'COMPLETED' AND MONTH(b.bookingDate) = :month AND YEAR(b.bookingDate) = :year")
    Long sumRevenueByOwnerIdAndMonth(@Param("ownerId") Integer ownerId, @Param("month") int month, @Param("year") int year);
}
