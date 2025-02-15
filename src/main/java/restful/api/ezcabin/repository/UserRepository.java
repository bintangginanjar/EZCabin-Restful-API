package restful.api.ezcabin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    
}
