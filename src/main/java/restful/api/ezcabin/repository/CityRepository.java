package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.CityEntity;

public interface CityRepository extends JpaRepository<CityEntity, Long>{

}
