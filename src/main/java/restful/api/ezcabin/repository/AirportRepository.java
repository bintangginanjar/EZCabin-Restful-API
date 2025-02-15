package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.AirportEntity;

public interface AirportRepository extends JpaRepository<AirportEntity, Long>{

}
