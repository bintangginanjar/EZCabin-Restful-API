package restful.api.ezcabin.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flights")
@Builder
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", unique = true, length = 4, nullable = false)    
    private String flightNumber;

    @Column(name = "base_price", nullable = false)
    private Long basePrice;    

    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false, referencedColumnName = "id")
    private AirportEntity originAirport;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false, referencedColumnName = "id")
    private AirportEntity destinationAirport;

    @ManyToOne
    @JoinColumn(name = "plane_id", nullable = false, referencedColumnName = "id")
    private PlaneEntity planeEntity;

    @ManyToOne
    @JoinColumn(name = "airline_id", nullable = false, referencedColumnName = "id")
    private AirlineEntity airlineEntity;

    @OneToMany(mappedBy = "flightEntity", cascade = CascadeType.ALL)
    private List<FareSeatEntity> fareSeats;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserEntity userEntity;
}
