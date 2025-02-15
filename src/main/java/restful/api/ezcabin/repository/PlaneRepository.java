package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.PlaneEntity;

public interface PlaneRepository extends JpaRepository<PlaneEntity, Long>{

}
