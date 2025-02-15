package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.TravellerEntity;

public interface TravellerRepository extends JpaRepository<TravellerEntity, Long>{

}
