package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.UserEntity;
import dental.flow.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);
        
}
