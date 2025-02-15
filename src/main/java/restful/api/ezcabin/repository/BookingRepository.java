package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long>{

}
