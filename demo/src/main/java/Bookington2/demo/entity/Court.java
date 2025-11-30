package Bookington2.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Court {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;
    private String name;
    private String status;
}
