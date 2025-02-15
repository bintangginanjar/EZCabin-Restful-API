package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.TaxEntity;

public interface TaxRepository extends JpaRepository<TaxEntity, Long>{

}
