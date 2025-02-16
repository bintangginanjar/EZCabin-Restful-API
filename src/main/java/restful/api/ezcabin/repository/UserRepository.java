package restful.api.ezcabin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import restful.api.ezcabin.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findFirstByToken(String token);

}
