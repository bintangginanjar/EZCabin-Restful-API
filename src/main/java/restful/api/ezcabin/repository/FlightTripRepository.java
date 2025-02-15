package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.FlightTripEntity;

public interface FlightTripRepository extends JpaRepository<FlightTripEntity, Long>{

}
