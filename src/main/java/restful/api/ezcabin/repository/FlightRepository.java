package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.FlightEntity;

public interface FlightRepository extends JpaRepository<FlightEntity, Long>{

}
