package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long>{

}
