package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.SeatClassEntity;

public interface SeatClassRepository extends JpaRepository<SeatClassEntity, Long>{

}
