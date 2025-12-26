package Bookington2.demo.entity;

import Bookington2.demo.enums.CourtStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "court")
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private String name;

    @Enumerated(EnumType.STRING)
    private CourtStatus status = CourtStatus.ACTIVE;

    private Boolean deleted = false;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}
