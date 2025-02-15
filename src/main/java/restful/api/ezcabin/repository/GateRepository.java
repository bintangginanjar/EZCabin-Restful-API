package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.GateEntity;

public interface GateRepository extends JpaRepository<GateEntity, Long>{

}
