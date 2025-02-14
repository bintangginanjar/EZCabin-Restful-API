package restful.api.ezcabin.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "airports")
@Builder
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 4, nullable = false)
    private String code;

    @Column(unique = true, length = 64, nullable = false)
    private String name;

    @Column(name = "postal_code")
    private String postalCode;
    
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false, referencedColumnName = "id")
    private CityEntity cityEntity;

    @OneToMany(mappedBy = "originAirport", cascade = CascadeType.ALL)
    private List<FlightEntity> origins;

    @OneToMany(mappedBy = "destinationAirport", cascade = CascadeType.ALL)
    private List<FlightEntity> destinations;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "airports_gates",
        joinColumns = @JoinColumn(name = "airport_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "gate_id", referencedColumnName = "id")
    )
    private List<GateEntity> gates;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserEntity userEntity;
}
