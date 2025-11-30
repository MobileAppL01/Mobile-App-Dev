package Bookington2.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking {
    @Id
    @GeneratedValue
    private int id;

    @Temporal(TemporalType.DATE)
    private LocalDate bookingDate;

    private int startTimeSlot;
    private int endTimeSlot;
    private float totalPrice;
    private String status;

    @OneToOne
    @JoinColumn(name="player_id", referencedColumnName = "id")
    private User player;
    @OneToOne
    @JoinColumn(name="court_id", referencedColumnName = "id")
    private Court court;





}
