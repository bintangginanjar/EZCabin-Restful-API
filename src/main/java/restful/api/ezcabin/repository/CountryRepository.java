package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.CountryEntity;

public interface CountryRepository extends JpaRepository<CountryEntity, Long>{

}
