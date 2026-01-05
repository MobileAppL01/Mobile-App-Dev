package Bookington2.demo.repository;

import Bookington2.demo.entity.Payment;
import Bookington2.demo.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
    List<Payment> findByBookingId(Integer bookingId);
    
    Optional<Payment> findByBookingIdAndStatus(Integer bookingId, PaymentStatus status);
    
    Optional<Payment> findByPaymentId(String paymentId);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    Optional<Payment> findByOrderReference(String orderReference);
    
    List<Payment> findByStatus(PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.status = :status AND p.expiredAt < :now")
    List<Payment> findExpiredPayments(@Param("status") PaymentStatus status, @Param("now") LocalDateTime now);
    
    @Query("SELECT p FROM Payment p WHERE p.booking.id = :bookingId ORDER BY p.createdAt DESC")
    List<Payment> findByBookingIdOrderByCreatedAtDesc(@Param("bookingId") Integer bookingId);
    
    @Query("SELECT p FROM Payment p WHERE p.status = :status AND p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findByStatusAndDateRange(@Param("status") PaymentStatus status, 
                                           @Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    Long countByStatus(@Param("status") PaymentStatus status);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    java.math.BigDecimal sumAmountByStatus(@Param("status") PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = :paymentMethod ORDER BY p.createdAt DESC")
    List<Payment> findByPaymentMethodOrderByCreatedAtDesc(@Param("paymentMethod") String paymentMethod);
}
