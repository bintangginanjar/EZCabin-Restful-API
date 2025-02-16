package restful.api.ezcabin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long>{

    Optional<RoleEntity> findByName(String name);

}
