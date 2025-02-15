package restful.api.ezcabin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fare_seat")
@Builder
public class FareSeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private Integer price;

    private String description;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false, referencedColumnName = "id")
    private FlightEntity flightEntity;

    @ManyToOne
    @JoinColumn(name = "seat_class_id", nullable = false, referencedColumnName = "id")
    private SeatClassEntity seatClassEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserEntity userEntity;

}
