package Bookington2.demo.repository;

import Bookington2.demo.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserId(Integer userId);
    @Query("SELECT n FROM Notification n where n.user.id = :userId or n.type = 'PROMOTION'")
    List<Notification> findByUserIdOrIsPromotionType(Integer userId);
}
