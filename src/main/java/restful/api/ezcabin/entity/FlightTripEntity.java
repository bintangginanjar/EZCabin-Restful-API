package restful.api.ezcabin.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flight_trip")
@Builder
public class FlightTripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "flightTripEntity", cascade = CascadeType.ALL)    
    private BookingEntity bookingEntity;

    @Column(name = "depart_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date departTime;

    @Column(name = "arrival_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date arrivalTime;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false, referencedColumnName = "id")
    private FlightEntity flightEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserEntity userEntity;

}
