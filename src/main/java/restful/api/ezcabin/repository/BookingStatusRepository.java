package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.BookingStatusEntity;

public interface BookingStatusRepository extends JpaRepository<BookingStatusEntity, Long>{

}
