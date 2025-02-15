package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.DiscountEntity;

public interface DiscountRepository extends JpaRepository<DiscountEntity, Long>{

}
