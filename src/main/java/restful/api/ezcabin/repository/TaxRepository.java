package restful.api.ezcabin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.TaxEntity;
import restful.api.ezcabin.entity.UserEntity;

public interface TaxRepository extends JpaRepository<TaxEntity, Long>{

    Optional<TaxEntity> findFirstByUserEntityAndId(UserEntity user, Integer taxId);

    List<TaxEntity> findAllByUserEntity(UserEntity user);

}
