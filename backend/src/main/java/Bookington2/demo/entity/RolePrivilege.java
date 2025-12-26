package Bookington2.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePrivilege {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;
    @ManyToOne
    @JoinColumn(name="privilege_id")
    private Privilege privilege;
}
