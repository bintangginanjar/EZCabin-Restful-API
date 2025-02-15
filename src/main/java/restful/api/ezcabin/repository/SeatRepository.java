package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.SeatEntity;

public interface SeatRepository extends JpaRepository<SeatEntity, Long>{

}
