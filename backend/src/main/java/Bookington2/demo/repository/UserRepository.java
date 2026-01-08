package Bookington2.demo.repository;

import Bookington2.demo.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  boolean existsByEmail(String username);
  User findByEmail(String email);
  
  List<User> findByRole(Bookington2.demo.enums.UserRole role);
}
