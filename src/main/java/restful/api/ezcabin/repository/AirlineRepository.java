package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.AirlineEntity;

public interface AirlineRepository extends JpaRepository<AirlineEntity, Long>{

}
