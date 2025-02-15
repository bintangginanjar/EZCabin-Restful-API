package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.FareEntity;

public interface FareRepository extends JpaRepository<FareEntity, Long>{

}
