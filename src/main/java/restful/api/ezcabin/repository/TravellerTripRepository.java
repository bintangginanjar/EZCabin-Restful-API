package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.TravellerTripEntity;

public interface TravellerTripRepository extends JpaRepository<TravellerTripEntity, Long>

}
