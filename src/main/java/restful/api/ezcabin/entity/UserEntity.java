package restful.api.ezcabin.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
import jakarta.persistence.OneToMany;
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
@Table(name = "users")
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, length = 64, nullable = false)
    private String username;

    private String password;    
    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;

    private Boolean isActive;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(updatable = true, name = "updated_at")
    private Date updatedAt;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)    
    private ProfileEntity profileEntity;
    
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<CountryEntity> countries;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<CityEntity> cities;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<AirportEntity> airports;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<FlightEntity> flights;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<GateEntity> gates;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<SeatClassEntity> seatClasses;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<FareSeatEntity> fareSeats;    

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<PlaneEntity> planes;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<BookingEntity> bookings;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<BookingStatusEntity> bookStatuses;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<TravellerEntity> travellers;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<TravellerTripEntity> travellerTrips;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<FlightTripEntity> flightTrips;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<DiscountEntity> discounts;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<TaxEntity> taxes;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<FareEntity> fares;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<SeatEntity> seats;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<AirlineEntity> airlines;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<RoleEntity> roles;
}
