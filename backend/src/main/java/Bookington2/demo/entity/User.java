package Bookington2.demo.entity;

import Bookington2.demo.enums.CourtStatus;
import Bookington2.demo.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_user")
@Getter
@Setter
@Builder // <--- QUAN TRỌNG: Để dùng được User.builder()
@NoArgsConstructor // <--- QUAN TRỌNG: Để JPA hoạt động
@AllArgsConstructor // <--- QUAN TRỌNG: Để @Builder hoạt động
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // Các trường khác...
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String verificationCode;
    private java.time.LocalDateTime verificationCodeExpiresAt;
}