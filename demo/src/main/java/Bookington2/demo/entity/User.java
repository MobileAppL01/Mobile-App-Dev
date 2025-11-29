package Bookington2.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder            // <--- QUAN TRỌNG: Để dùng được User.builder()
@NoArgsConstructor  // <--- QUAN TRỌNG: Để JPA hoạt động
@AllArgsConstructor // <--- QUAN TRỌNG: Để @Builder hoạt động
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Các trường khác...
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}